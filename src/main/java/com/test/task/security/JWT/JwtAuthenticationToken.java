package com.test.task.security.JWT;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private final String credentials;

    public JwtAuthenticationToken(String principal, String credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(String principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities); // Передаем коллекцию ролей (GrantedAuthority)
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);  // Устанавливаем как аутентифицированного, если переданы роли
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
