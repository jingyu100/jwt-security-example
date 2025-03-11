package com.jwt.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@ToString(exclude = {"accessToken"})
public class TokenInfo {

    private String accessToken;

    private Date accessTokenExpireTime;

    private String ownerEmail;

    private String tokenId;
}
