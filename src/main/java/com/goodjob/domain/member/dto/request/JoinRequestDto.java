package com.goodjob.domain.member.dto.request;

import lombok.Getter;

@Getter
public class JoinRequestDto {
    private String account;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}
