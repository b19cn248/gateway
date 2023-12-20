package com.lawman.gateway.authserver.configuration;

import com.lawman.gateway.authserver.facade.AddressFacadeService;
import com.lawman.gateway.authserver.facade.AuthenticationFacadeService;
import com.lawman.gateway.authserver.facade.CustomerFacadeService;
import com.lawman.gateway.authserver.facade.SellerFacadeService;
import com.lawman.gateway.authserver.facade.impl.AddressFacadeServiceImpl;
import com.lawman.gateway.authserver.facade.impl.AuthenticationFacadeServiceImpl;
import com.lawman.gateway.authserver.facade.impl.CustomerServiceFacadeImpl;
import com.lawman.gateway.authserver.facade.impl.SellerFacadeServiceImpl;
import com.lawman.gateway.authserver.repository.*;
import com.lawman.gateway.authserver.service.*;
import com.lawman.gateway.authserver.service.impl.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.ENCODING_UTF_8;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.MESSAGE_I18N_PATH;


@Configuration
public class GatewayConfiguration {

  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(MESSAGE_I18N_PATH);
    messageSource.setDefaultEncoding(ENCODING_UTF_8);
    return messageSource;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MessageService messageService(MessageSource messageSource) {
    return new MessageServiceImpl(messageSource);
  }

  @Bean
  public UserService userService(UserRepository repository) {
    return new UserServiceImpl(repository);
  }

  @Bean
  public AccountService accountService(
        AccountRepository accountRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        JavaMailSender mailSender,
        TemplateEngine templateEngine
  ) {
    return new AccountServiceImpl(accountRepository, roleRepository, passwordEncoder, mailSender, templateEngine);
  }

  @Bean
  public CustomerService customerService(CustomerRepository repository) {
    return new CustomerServiceImpl(repository);
  }


  @Bean
  public AddressFacadeService addressFacadeService() {
    return new AddressFacadeServiceImpl();
  }

  @Bean
  public SellerService sellerService(SellerRepository repository) {
    return new SellerServiceImpl(repository);
  }

  @Bean
  public CustomerFacadeService customerFacadeService(
        AccountService accountService,
        CustomerService customerService,
        SellerService sellerService,
        AddressFacadeService addressFacadeService
  ) {
    return new CustomerServiceFacadeImpl(
          accountService,
          customerService,
          sellerService,
          addressFacadeService
    );
  }

  @Bean
  public SellerFacadeService sellerFacadeService(
        AccountService accountService,
        SellerService sellerService,
        AddressFacadeService addressFacadeService
  ) {
    return new SellerFacadeServiceImpl(
          accountService,
          sellerService,
          addressFacadeService
    );
  }

  @Bean
  public JwtTokenService jwtTokenService(AccountService accountService) {
    return new JwtTokenServiceImpl(accountService);
  }

  @Bean
  public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
    return new RedisServiceImpl(redisTemplate);
  }

  @Bean
  public AuthenticationFacadeService authenticationFacadeService(
        AccountService accountService,
        JwtTokenService jwtTokenService,
        RedisService redisService
  ) {
    return new AuthenticationFacadeServiceImpl(
          accountService,
          jwtTokenService,
          redisService
    );
  }
}
