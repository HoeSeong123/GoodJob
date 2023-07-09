package com.goodjob.core.global.security;


import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.rq.Rq;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.goodjob.core.global.base.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SECOND;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Rq rq;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();
        Member member = memberService.findByUsername(user.getUsername()).orElse(null);

        Map<String, String> tokens = jwtProvider.genAccessTokenAndRefreshToken(member);

        rq.setCookie("accessToken", tokens.get("accessToken"));
        rq.setRefreshCookie("refreshToken", tokens.get("refreshToken"));

        Cookie previousUrlCookie = rq.getCookie("previousUrl");

        if (previousUrlCookie != null) {
            String previousUrl = rq.getPreviousUrl(previousUrlCookie);

            response.sendRedirect(previousUrl);
        } else {
            response.sendRedirect("/");
        }
    }
}
