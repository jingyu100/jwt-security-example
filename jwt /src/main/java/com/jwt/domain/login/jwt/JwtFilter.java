package com.jwt.domain.login.jwt;

import com.jwt.domain.login.dto.TokenValidationResult;
import com.jwt.domain.login.jwt.token.TokenProvider;
import com.jwt.domain.login.jwt.token.TokenStatus;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_REGEX = "Bearer ([a-zA-Z0-9_\\-\\+\\/=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]+)\\.([a-zA-Z0-9_.\\-\\+\\/=]*)";
    private static final Pattern BEARER_PATTERN = Pattern.compile(BEARER_REGEX);
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        // JWT 토큰 예외 구분 처리를 위해 request에 tokenValidationResult를 담아 EntryPoint에 전달
        // Authorization 헤더가 없는 경우
        if (!StringUtils.hasText(token)) {
            handleMissingToken(request, response, filterChain);
            return;
        }

        TokenValidationResult tokenValidationResult = tokenProvider.validateToken(token);

        // 잘못된 토큰일 경우
        if (!tokenValidationResult.isValid()) {
            handleWrongToken(request, response, filterChain, tokenValidationResult);
            return;
        }

        // 토큰이 블랙리스트인 경우
        if (tokenProvider.isAccessTokenBlackList(token)) {
            handleBlackListedToken(request, response, filterChain);
            return;
        }


        // 정상적인 토큰인 경우 SecurityContext에 Authentication 설정
        handleValidToken(token, tokenValidationResult);
        filterChain.doFilter(request, response);
    }

    private void handleValidToken(String token, TokenValidationResult tokenValidationResult) {
        Authentication authentication = tokenProvider.getAuthentication(token, tokenValidationResult.getClaims());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("AUTH SUCCESS : {},", authentication.getName());
    }

    private void handleWrongToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                  TokenValidationResult tokenValidationResult) throws IOException, ServletException {
        request.setAttribute("result", tokenValidationResult);
        filterChain.doFilter(request, response);
    }

    private void handleMissingToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        request.setAttribute("result",
                new TokenValidationResult(TokenStatus.WRONG_AUTH_HEADER, null, null, null)
        );
        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && BEARER_PATTERN.matcher(bearerToken).matches()) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleBlackListedToken(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws IOException, ServletException {
        request.setAttribute("result",
                new TokenValidationResult(TokenStatus.TOKEN_IS_BLACKLIST, null, null, null)
        );
        filterChain.doFilter(request, response);
    }



}
