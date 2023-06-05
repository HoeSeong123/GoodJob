package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.dto.request.LoginRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.redis.RedisUt;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import com.goodjob.global.base.cookie.CookieUt;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final CookieUt cookieUt;
    private final RedisUt redisUt;

    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String showJoin(JoinRequestDto joinRequestDto) {
        return "member/join";
    }

    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid JoinRequestDto joinRequestDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult ={}", bindingResult);
            return "member/join";
        }

//        if (!joinRequestDto.getPassword().equals(joinRequestDto.getConfirmPassword())) {
//            bindingResult.rejectValue("passwordInCorrect",
//                    "2개의 패스워드가 일치하지 않습니다.");
//            return "member/join";
//        }

        // TODO: 중복확인 셋다 안되면 안넘어가게?

        RsData<Member> joinRsData = memberService.join(joinRequestDto);
        log.info("joinRsData ={}", joinRsData.toString());
        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("member/login", joinRsData);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLogin() {
        return "member/login";
    }
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@Valid LoginRequestDto loginRequestDto,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/login";
        }
        log.info("loginRequestDto= {}", loginRequestDto.toString());

        RsData loginRsData = memberService.genAccessToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        log.info("loginRsData.ResultCode ={}", loginRsData.getResultCode());
        log.info("loginRsData.Data ={}", loginRsData.getData());

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        // TODO: 리팩토링
        String username = loginRequestDto.getUsername();
        if (redisUt.hasKeyBlackList(username)) {
            // 블랙리스트에서 삭제
            redisUt.deleteTokenFromBlackList(username);
        }

        String data = (String) loginRsData.getData();
        Cookie accessTokenCookie = cookieUt.createCookie("accessToken", data);
        Cookie usernameCookie = cookieUt.createCookie("username", loginRequestDto.getUsername());

        rq.setCookie(accessTokenCookie);
        rq.setCookie(usernameCookie);

        return "redirect:/";
    }

    // TODO: 삭제안됨.. 추후 수정
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.info("id ={}", id);
        memberService.delete(id);

        return "member/join";
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        String username = rq.getMember().getUsername();
        // TODO: 리팩토링
        // 레디스에서 리프레시토큰삭제
        redisUt.deleteToken(username);
        // 레디스에 블랙리스트 추가
        redisUt.setBlackList(username);
        // 쿠키에서 jwt토큰, username 제거.
        Cookie accessTokenCookie = rq.getCookie("accessToken");
        Cookie usernameCookie = rq.getCookie("username");

        accessTokenCookie = cookieUt.expireCookie(accessTokenCookie);
        usernameCookie = cookieUt.expireCookie(usernameCookie);
        log.info("만료된쿠키= username= {}, accessToken= {}", usernameCookie.getMaxAge(), accessTokenCookie.getMaxAge());

        rq.setCookie(accessTokenCookie);
        rq.setCookie(usernameCookie);
        return rq.redirectWithMsg("/", "로그아웃 되었습니다.");
    }
}
