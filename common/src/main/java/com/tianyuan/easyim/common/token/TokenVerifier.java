package com.tianyuan.easyim.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.io.Files;
import com.tianyuan.easyim.common.exception.TokenVerificationException;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

public class TokenVerifier {

    private RSAPublicKey publicKey;

    public TokenVerifier(File publicRsaKeyFile) {
        try {
            this.publicKey = genPublicKey(Files.toByteArray(publicRsaKeyFile));
        } catch (IOException e) {
            throw new RuntimeException("Build token verifier fail", e);
        }
    }

    private RSAPublicKey genPublicKey(byte[] publicRsaKey) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicRsaKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Gen RSA private key fail", e);
        }
    }

    public <T> Token<T> verifyAndGetData(String token, Class<T> dataType) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decoded = verifier.verify(token);
            T data = decoded.getClaim("data").as(dataType);
            return new Token<>(data);
        } catch (JWTVerificationException e) {
            throw new TokenVerificationException("Token verify fail", e);
        }
    }
}
