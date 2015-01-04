package com.sky.server.config;

import com.sky.server.config.social.user.SecurityContext;
import com.sky.server.config.social.user.SimpleConnectionSignUp;
import com.sky.server.config.social.user.SimpleSignInAdapter;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
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
 * Created by jcooky on 2014. 9. 25..
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
  private String propertiesPrefix = "spring.social.github.";

  public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                     Environment environment) {
    configurer.addConnectionFactory(new GitHubConnectionFactory(
        environment.getRequiredProperty(propertiesPrefix + "appId"),
        environment.getRequiredProperty(propertiesPrefix + "appSecret")));
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


  @Bean
  public UsersConnectionRepository usersConnectionRepository(DataSource dataSource,
                                                                ConnectionFactoryLocator connectionFactoryLocator,
                                                                SimpleConnectionSignUp simpleConnectionSignUp) {
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
  public Connection<?>  connection(ConnectionRepository repository) {
    Connection<GitHub> connection = repository
        .findPrimaryConnection(GitHub.class);
    return connection != null ? connection : null;
  }

  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public GitHubClient gitHubClient(Connection<?> connection) {
    if (connection == null)
      return null;

    GitHubClient github = new GitHubClient();
    github.setOAuth2Token(connection.createData().getAccessToken());

    return github;
  }
}
