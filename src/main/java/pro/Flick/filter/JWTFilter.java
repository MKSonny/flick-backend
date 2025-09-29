package pro.Flick.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.Flick.jwt.service.JwtService;
import pro.Flick.util.JWTUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService; // UserDetailsService 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Authorization 헤더를 확인합니다.
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // 토큰이 없으면 다음 필터로 진행
            return;
        }

        // 2. "Bearer " 접두사를 제거하고 토큰을 추출합니다.
        String accessToken = authorization.substring(7);

        // 3. JWTUtil.isValid()를 사용해 Access Token인지, 유효한지 검증합니다.
        //    (JWTUtil.isValid는 만료 여부도 내부적으로 확인합니다)
        if (JWTUtil.isValid(accessToken, true)) {

            // 4. 토큰에서 username을 추출합니다.
            String username = JWTUtil.getUsername(accessToken);

            // 5. username으로 UserDetailsService를 통해 UserDetails 객체를 조회합니다.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 6. UserDetails 객체를 사용하여 Authentication 객체를 생성합니다.
            //    (이때 Principal은 반드시 UserDetails 객체여야 합니다)
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // 7. SecurityContextHolder에 인증 정보를 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 8. 다음 필터로 요청-응답을 전달합니다.
        filterChain.doFilter(request, response);

    }

}
