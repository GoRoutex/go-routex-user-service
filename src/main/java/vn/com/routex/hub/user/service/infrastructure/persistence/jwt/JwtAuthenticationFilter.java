package vn.com.routex.hub.user.service.infrastructure.persistence.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.extractAllClaims(token);
            String userId = claims.getSubject();
            String email = claims.get("email", String.class);
            String tokenType = claims.get("type", String.class);
            String merchantId = claims.get("merchantId", String.class);

            if("refresh".equals(tokenType)) {
                filterChain.doFilter(request, response);
                return;
            }

            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

                Object rolesClaim = claims.get("roles");
                Set<String> roles = Collections.emptySet();

                if(rolesClaim instanceof Collection<?> roleClaims) {
                    roles = roleClaims.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .map(String::trim)
                            .filter(role -> !role.isBlank())
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .collect(Collectors.toSet());

                    roles.stream()
                            .filter(Objects::nonNull)
                            .map(SimpleGrantedAuthority::new)
                            .forEach(grantedAuthorities::add);
                }

                Object authoritiesClaim = claims.get("authorities");
                Set<String> authorities = Collections.emptySet();
                if(authoritiesClaim instanceof Collection<?> authorityClaims) {
                    authorities = authorityClaims.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .map(String::trim)
                            .filter(authority -> !authority.isBlank())
                            .collect(Collectors.toSet());

                    authorities.stream()
                            .filter(Objects::nonNull)
                            .map(SimpleGrantedAuthority::new)
                            .forEach(grantedAuthorities::add);
                }

                JwtAuthenticatedUser principal = new JwtAuthenticatedUser(
                        userId,
                        email,
                        merchantId,
                        roles,
                        authorities
                );

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                grantedAuthorities
                        );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        } catch(Exception ignore) {
        }

        filterChain.doFilter(request, response);
    }
}
