//package com.jwt.domain.login.jwt.token;
//
//import com.jwt.domain.login.dto.TokenInfo;
//import com.jwt.domain.member.Member;
//import com.jwt.domain.member.Role;
//import lombok.extern.slf4j.Slf4j;
////import org.junit.jupiter.api.Test;
//
//@Slf4j
//public class TokenProviderTest {
//
//    private final String secret = "dGhpcyBpcyBteSBoaWRkZW4gand0IHNlY3JldCBrZXksIHdoYXQgaXMgeW91ciBqd3Qgc2VjcmV0IGtleT9YWA==";
//    private final Long accessTokenValidTimeInSeconds = 3L;
//    private final TokenProvider tokenProvider = new TokenProvider(secret, accessTokenValidTimeInSeconds);
//
////    @Test
////    void createToken() {
////        Member member = getMember();
////
////        TokenInfo tokenInfo = tokenProvider.createToken(member);
////        log.info(tokenInfo.toString());
////
////    }
//
//    private Member getMember() {
//        return Member.builder()
//                .email("jin@google.com")
//                .password("123")
//                .username("jin")
//                .role(Role.ROLE_USER)
//                .build();
//
//    }
//
//}
