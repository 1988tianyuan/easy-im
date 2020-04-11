package com.tianyuan.easyim.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import com.tianyuan.easyim.common.util.JsonUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Map;

import static com.tianyuan.easyim.common.token.TokenConstant.CLAIM_DATA;

public class TokenGenerator {

    private RSAPrivateKey privateKey;

    public TokenGenerator(File privateRsaKeyFile) {
        try {
            this.privateKey = genPrivateKey(Files.toByteArray(privateRsaKeyFile));
        } catch (IOException e) {
            throw new RuntimeException("Build token generator fail", e);
        }
    }

    private RSAPrivateKey genPrivateKey(byte[] privateRsaKey) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateRsaKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Gen RSA private key fail", e);
        }
    }

    public String newJwt(Object data, int expireSeconds) {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        Map<String, Object> dataMap = JsonUtil.convert(data, new TypeReference<Map<String, Object>>() {});
        return JWT.create()
                .withExpiresAt(DateUtils.addSeconds(new Date(), expireSeconds))
                .withClaim(CLAIM_DATA, dataMap)
                .sign(algorithm);
    }
}
