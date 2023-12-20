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
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thymeleaf.TemplateEngine;

import javax.sql.DataSource;

@TestConfiguration
@EnableJpaRepositories(basePackages = {"com.lawman.gateway.authserver.repository"},
      entityManagerFactoryRef = "entityManagerFactoryTest",
      transactionManagerRef = "transactionManagerTest ")
@ComponentScan(basePackages = {"com.lawman.gateway.authserver.repository"})
@EnableTransactionManagement
public class GatewayConfigurationTest {
  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.H2).build();
  }

  @Bean
  public EntityManagerFactory entityManagerFactoryTest() {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.lawman.gateway.authserver.entity");
    factory.setDataSource(dataSource());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManagerTest() {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactoryTest());
    return txManager;
  }
  @Bean
  public UserService userService(UserRepository repository) {
    return new UserServiceImpl(repository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JavaMailSender javaMailSender() {
    return new JavaMailSenderImpl();
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


  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6378));
  }

  @Bean
  @Primary
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(
                authorize -> authorize.anyRequest().permitAll()
          )
          .build();
  }

  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedHeader("*");
    configuration.addAllowedOrigin("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
