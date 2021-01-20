package com.antra.report.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthorizationServerTokenServices jwtServices;

    @Autowired
    public AuthorizationServerConfig(PasswordEncoder passwordEncoder, @Qualifier("JWTService") AuthorizationServerTokenServices jwtServices) {
        this.passwordEncoder = passwordEncoder;
        this.jwtServices = jwtServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("reporting_system")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all")
                .resourceIds("client")
                .autoApprove(false)
                .redirectUris("http://www.antra.com");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenServices(jwtServices);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()") // /oauth/token_key
                .checkTokenAccess("permitAll()") // /oauth/check_token
                .allowFormAuthenticationForClients();
    }
}
