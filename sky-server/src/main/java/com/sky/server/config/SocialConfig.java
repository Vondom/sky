package com.sky.server.config;

import com.sky.server.social.user.SecurityContext;
import com.sky.server.social.user.SimpleConnectionSignUp;
import com.sky.server.social.user.SimpleSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;

import javax.sql.DataSource;

/**
 * Created by jcooky on 2014. 7. 20..
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {
  @Autowired
  private DataSource dataSource;

  @Autowired
  private SimpleConnectionSignUp simpleConnectionSignUp;


  @Override
  public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
    connectionFactoryConfigurer.addConnectionFactory(new GitHubConnectionFactory(environment.getProperty("github.clientId"), environment.getProperty("github.clientSecret")));
  }

  @Override
  public UserIdSource getUserIdSource() {
    return new UserIdSource() {
      @Override
      public String getUserId() {
        return SecurityContext.getCurrentUser().getId();
      }
    };
  }

  @Override
  public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
    JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    repository.setConnectionSignUp(simpleConnectionSignUp);
    return repository;
  }

  @Bean
  public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
    return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new SimpleSignInAdapter());
  }

  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
  public GitHub github(ConnectionRepository connectionRepository) {
    Connection<GitHub> connection = connectionRepository.findPrimaryConnection(GitHub.class);

    return connection != null ? connection.getApi() : null;
  }
}
