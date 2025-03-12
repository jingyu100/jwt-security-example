package com.jwt.web.controller;

import com.jwt.domain.login.LoginService;
import com.jwt.domain.login.dto.TokenInfo;
import com.jwt.domain.member.Member;
import com.jwt.domain.member.UserPrincipal;
import com.jwt.web.controller.dto.MemberCreateDto;
import com.jwt.web.controller.dto.MemberLoginDto;
import com.jwt.web.controller.json.ApiResponseJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/api/account/create")
    public ApiResponseJson createNewAccount(@Valid @RequestBody MemberCreateDto memberCreateDto,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Member member = loginService.createMember(memberCreateDto);
        log.info("Account successfully created with details: {}", member);

        return new ApiResponseJson(HttpStatus.OK, Map.of(
                "email", member.getEmail(),
                "username", member.getUsername()
        ));
    }

    @PostMapping("/api/account/auth")
    public ApiResponseJson authenticateAccountAndIssueToken(@Valid @RequestBody MemberLoginDto memberLoginDto,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        TokenInfo tokenInfo = loginService.loginMember(memberLoginDto.getEmail(), memberLoginDto.getPassword());
        log.info("Token issued: {}", tokenInfo);

        return new ApiResponseJson(HttpStatus.OK, tokenInfo);
    }

    @GetMapping("/api/account/userinfo")
    public ApiResponseJson getUserInfo(@AuthenticationPrincipal UserPrincipal userPrinciple) {
        log.info("요청 이메일 : {}", userPrinciple.getEmail());

        Member foundMember = loginService.getUserInfo(userPrinciple.getEmail());

        return new ApiResponseJson(HttpStatus.OK, foundMember);
    }

    @PostMapping("/api/account/logout")
    public ApiResponseJson logoutUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @RequestHeader("Authorization") String authHeader) {
        log.info("로그아웃 요청 이메일 : {}", userPrincipal.getEmail());

        loginService.logout(authHeader.substring(7), userPrincipal.getEmail());

        return new ApiResponseJson(HttpStatus.OK, "OK. BYE~~");
    }


}
