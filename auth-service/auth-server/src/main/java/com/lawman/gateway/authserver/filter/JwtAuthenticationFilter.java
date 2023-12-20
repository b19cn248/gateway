package com.lawman.gateway.authserver.filter;

import com.lawman.gateway.authserver.exception.auth.TokenExpiredException;
import com.lawman.gateway.authserver.exception.auth.TokenInvalidException;
import com.lawman.gateway.authserver.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;

  @Override
  protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    log.info(
          "(doFilterInternal)request: {}, response: {}, filterChain: {}",
          request,
          response,
          filterChain
    );

    String accessToken = request.getHeader(AUTHORIZATION);

    log.info("(accessToken) :{}", accessToken);

    if (Objects.isNull(accessToken) || !accessToken.startsWith(TYPE_TOKEN)) {
      filterChain.doFilter(request, response);
      return;
    }

    var jwtToken = accessToken.substring(AUTHORIZATION_TYPE_SIZE);

    log.info("(jwtToken) :{}", jwtToken);

    try {
      String userid = jwtTokenService.getSubjectFromToken(jwtToken);
      String username = jwtTokenService.getUsernameFromToken(jwtToken);
      List<GrantedAuthority> grantedAuthority = getAuthorities(jwtTokenService.getRoleFromToken(jwtToken));
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            username, userid, grantedAuthority
      );
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      filterChain.doFilter(request, response);
    } catch (TokenInvalidException e) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), INVALID_TOKEN);
    } catch (TokenExpiredException e) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), EXPIRED_TOKEN);
    }
  }

  private List<GrantedAuthority> getAuthorities(String role) {
    log.debug("(getAuthorities) role: {}", role);

    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

}
