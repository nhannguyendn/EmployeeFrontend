package com.example.employee.security.filter;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.employee.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, java.io.IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/auth/")) { // permit all auth endpoints
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            unauthorizedResponse(response, "Missing or invalid Authorization header");
            return;
        }

        final String jwt = authHeader.substring(7);
        try {

            if (jwtService.blacklistedTokens.contains(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been revoked");
                return;
            }

            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    unauthorizedResponse(response, "Invalid JWT token");
                    return;
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            unauthorizedResponse(response, "Token expired");
            return;
        } catch (Exception e) {
            unauthorizedResponse(response, "Authentication error");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void unauthorizedResponse(HttpServletResponse response, String message) throws java.io.IOException {
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response.setContentType("application/json");
        // response.getWriter().write("{\"error\":\"" + message + "\"}");
         throw new InsufficientAuthenticationException(message);
    }
}
