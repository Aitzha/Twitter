package com.example.twitter.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class JWTDecoder {
    public static class JWTToken {
        String jti;
        String iss;
        String aud;
        public String sub;
        String typ;
        String azp;
        String sessionState;
        String acr;
        String name;
        String preferredUsername;
        String givenName;
    }

    private static final String TOKEN_PREFIX = "Bearer ";

    private final String token;
    private final String header;
    private final String payload;
    private final String signature;
    private final JWTToken jwtToken;

    public JWTDecoder(String token) {
        this.token = token;

        String[] chunks = token.substring(TOKEN_PREFIX.length()).split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        header = new String(decoder.decode(chunks[0]));
        payload = new String(decoder.decode(chunks[1]));
        if (chunks.length > 2) {
            signature = new String(decoder.decode(chunks[2]));
        } else {
            signature = "";
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setVersion(2.0);

        Gson gson = builder.create();
        jwtToken = gson.fromJson(payload, JWTToken.class);
    }

    public void print() {
        System.out.println(payload);
        System.out.println(jwtToken.sub);
    }

    public String getToken() {
        return token;
    }

    public String getHeader() {
        return header;
    }

    public String getPayload() {
        return payload;
    }

    public String getSignature() {
        return signature;
    }

    public JWTToken getJWTToken() {return jwtToken;}
}
