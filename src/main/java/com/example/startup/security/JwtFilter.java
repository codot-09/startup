package com.example.startup.security;

import com.example.startup.entity.User;
import com.example.startup.exception.AccessDeniedException;
import com.example.startup.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@CrossOrigin
public class JwtFilter extends OncePerRequestFilter {

    @Value("${security.whitelist}")
    private String[] whiteList;

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    public String sessionToken;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        if (isWhiteListed(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header missing or invalid");
        }

        String token = authorization.substring(7);
        sessionToken = token;

        try {
            if (jwtProvider.isTokenValid(token)) {
                String key = jwtProvider.getKeyFromToken(token);
                User user = (User) userDetailsService.loadUserByUsername(key);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                throw new AccessDeniedException("Token is invalid or expired");
            }
        } catch (UnauthorizedException | AccessDeniedException e) {
            handleCustomException(response,new AccessDeniedException("Access denied"));
            return;
        } catch (Exception e) {
            handleCustomException(response, new UnauthorizedException("Invalid token"));
            return;
        }

        filterChain.doFilter(request, response);
    }



    private boolean isWhiteListed(String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        return Arrays.stream(whiteList).anyMatch(pattern -> matcher.match(pattern, path));
    }

    private void handleCustomException(HttpServletResponse response, RuntimeException ex) throws IOException {
        int statusCode = 500;
        if (ex instanceof UnauthorizedException) {
            statusCode = 401;
        } else if (ex instanceof AccessDeniedException) {
            statusCode = 403;
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        new ErrorMessage(ex.getMessage())
                )
        );
    }
}