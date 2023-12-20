package com.lawman.gateway.authserver.service.impl;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.ChangePasswordRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.exception.account.*;
import com.lawman.gateway.authserver.repository.AccountRepository;
import com.lawman.gateway.authserver.repository.RoleRepository;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.base.BaseServiceImpl;
import com.lawman.gateway.authserver.utils.GenerateUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.*;

@Slf4j
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

  private final AccountRepository accountRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  public AccountServiceImpl(
        AccountRepository accountRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        JavaMailSender mailSender,
        TemplateEngine templateEngine
  ) {
    super(accountRepository);
    this.accountRepository = accountRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  @Override
  public AccountResponse create(AccountRequest request, String role) {
    log.info("(create) request:{}", request);

    checkExistedUsername(request.getUsername());

    Account account = accountRepository.save(toEntity(request, role));

    return toDTO(account, role);
  }

  @Override
  public Account getByUsername(String username) {
    log.info("(getByUsername) username:{}", username);
    return accountRepository.findAccountByUsername(username).orElseThrow(UsernameNotFoundException::new);
  }

  @Override
  public String getRole(String id) {
    log.info("(getRole) id:{}", id);
    return accountRepository.getRole(id);
  }

  @Override
  public boolean equalPassword(String passwordRaw, String passwordEncrypted) {
    log.info("(equalPassword) passwordRaw:{}, passwordEncrypted:{}", passwordRaw, passwordEncrypted);
    return passwordEncoder.matches(passwordRaw, passwordEncrypted);
  }

  @Override
  @Transactional
  public void increaseFailedAttempts(Account account) {
    log.info("(increaseFailedAttempt) account: {}", account);

    int newFailedAttempt = account.getFailedAttempt() + 1;

    log.info("(newFailedAttempt): {}", newFailedAttempt);

    accountRepository.increaseFailedAttempts(account.getId(), newFailedAttempt);
  }

  @Override
  @Transactional
  public void resetFailedAttemptsAndLockTime(String id) {
    log.info("(resetFailedAttempts) id:{}", id);

    accountRepository.reset(id);
  }

  @Override
  public void lock(Account account) {
    log.info("(lock) account:{}", account);

    account.setLockTime(System.currentTimeMillis() + LOCK_TIME);

    accountRepository.save(account);
  }

  @Override
  public Account findByEmail(String email) {
    return accountRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
  }

  @Override
  public Account getByResetPasswordToken(String token) {
    log.info("(getByResetPasswordToken) token:{}", token);

    return accountRepository.findByResetPasswordToken(token).orElseThrow(TokenInvalidException::new);
  }

  @Override
  @Transactional
  public void updatePassword(String id, String password) {
    log.info("(updatePassword) id:{}, password:{}", id, password);

    accountRepository.updatePassword(id, passwordEncoder.encode(password));
  }

  @Override
  public void forgotPassword(String email) {

    log.info("(forgotPassword) email: {}", email);

    String token = UUID.randomUUID().toString();

    resetPasswordToken(token, email);

    String link = GenerateUtils.generateUrlForgotPassword(token, email);

    try {
      sendEmail(email, link);
    } catch (MessagingException | UnsupportedEncodingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void changePassword(String id, ChangePasswordRequest request) {
    log.info("(changePassword) request: {}", request);
    Account account = find(id);
    comparePasswords(request.getPassword(), account.getPassword());
    account.setPassword(passwordEncoder.encode(request.getNewPassword()));
    accountRepository.save(account);
  }

  private void checkExistedUsername(String username) {
    log.info("(checkExistedUsername) username:{}", username);
    if (accountRepository.existsByUsername(username)) {
      log.error("(checkExistedUsername) ====================> UsernameAlreadyExistedException");
      throw new UsernameAlreadyExistedException();
    }
  }

  private void comparePasswords(String passwordRaw, String passwordEncrypted) {
    if (!passwordEncoder.matches(passwordRaw, passwordEncrypted)) {
      log.error("(changePassword) ====================> PasswordIncorrectException");
      throw new PasswordIncorrectException();
    }
  }

  private Account toEntity(AccountRequest request, String role) {
    return Account.builder()
          .username(request.getUsername())
          .password(passwordEncoder.encode(request.getPassword()))
          .roleId(roleRepository.getIdByRole(role))
          .isActive(false)
          .isDeleted(false)
          .build();
  }

  private AccountResponse toDTO(Account account, String role) {
    return AccountResponse.builder()
          .id(account.getId())
          .username(account.getUsername())
          .role(role)
          .build();

  }

  private void resetPasswordToken(String token, String email) {
    log.info("(resetPasswordToken) token:{}, email:{}", token, email);

    Account account = accountRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);

    account.setResetPasswordToken(token);

    accountRepository.save(account);

  }

  private void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
    log.info("(sendEmail) recipientEmail:{}, link: {}", recipientEmail, link);

    Context context = new Context();
    context.setVariable(LINK, link);

    String content = templateEngine.process("email-template", context);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(FROM_EMAIL, EMAIL_TITLE);
    helper.setTo(recipientEmail);

    helper.setSubject(SUBJECT);
    helper.setText(content, true);

    mailSender.send(message);
  }

  private Account find(String id) {
    log.info("(find) id: {}", id);
    return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
  }

}
