package com.jwt.domain.login.dto;

import lombok.Data;

@Data
public class TokenInfo {

    private String accessToken;

    private Data accessTokenExpireTime;

    private String ownerEmail;

    private String tokenId;
}
