package com.securityexample.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY ="my-super-key";
    private static final long EXPIRATION_TIME =86400000;

    public String genrateToken(String username, String role){
        return JWT.create()
                .withSubject(username)
                .withClaim("role",role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
    public String vlaidateTokenAndRetrieveSubject(String token){
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)//verfies the token
                .getSubject();//this will get the user name //then supply where it was called
    }

}
