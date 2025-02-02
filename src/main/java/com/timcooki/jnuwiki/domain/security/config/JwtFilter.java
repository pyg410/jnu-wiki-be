package com.timcooki.jnuwiki.domain.security.config;


import static com.timcooki.jnuwiki.util.JwtUtil.JwtUtil.cutTokenPrefix;

import com.timcooki.jnuwiki.domain.member.entity.Member;
import com.timcooki.jnuwiki.domain.member.entity.MemberRole;
import com.timcooki.jnuwiki.domain.security.service.MemberSecurityService;
import com.timcooki.jnuwiki.util.JwtUtil.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends BasicAuthenticationFilter {
    private final String secretKey = "test.test";

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    // 인증 필터
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtFilter 동작");
        final String toekn = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (toekn == null || !toekn.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = cutTokenPrefix(toekn);
            String email = JwtUtil.getMemberEmail(token, secretKey);
            MemberRole memberRole = JwtUtil.getMemberRole(token, secretKey);

            Member member = Member.builder()
                    .email(email)
                    .role(memberRole)
                    .build();

            MemberDetails userDetails = new MemberDetails(member);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}.", e.getMessage());
            throw new JwtException("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new JwtException("토큰 기한 만료");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new JwtException("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException("JWT token compact of handler are invalid.");
        }
    }
}
