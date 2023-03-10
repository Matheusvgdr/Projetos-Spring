package com.jwt.jwtProject.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtValidarFilter extends BasicAuthenticationFilter {
    public static final String HEADER_ATRIBUTO = "Authorization";
    public static final String ATRIBTO_PREFIXO = "Bearer ";

    public JwtValidarFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String atributo = request.getHeader(HEADER_ATRIBUTO);

    if (atributo == null){
        chain.doFilter(request, response);
        return;
    }
    if (!atributo.startsWith(ATRIBTO_PREFIXO)){
        chain.doFilter(request, response);
        return;
    }
    String token = atributo.replace(ATRIBTO_PREFIXO, "");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(String token){
        String usuario = JWT.require(Algorithm.HMAC512(JwtAutenticarFilter.TOKEN_CHAVE)).build().verify(token).getSubject();

        if (usuario == null){
            return null;
        }

        return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
    }
}
