package com.antra.report.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
public class JWTTokenService {

    private final ClientDetailsService clientDetailsService;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final TokenStore jwtTokenStore;

    @Autowired
    public JWTTokenService(JwtAccessTokenConverter jwtAccessTokenConverter
            , ClientDetailsService clientDetailsService
            , @Qualifier("JWT") TokenStore tokenStore){
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.clientDetailsService = clientDetailsService;
        this.jwtTokenStore = tokenStore;
    }

    @Bean(name = "JWTService")
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setTokenStore(jwtTokenStore);
        services.setReuseRefreshToken(true);

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);

        services.setAccessTokenValiditySeconds(7200);
        services.setRefreshTokenValiditySeconds(259200);

        return services;
    }
}
