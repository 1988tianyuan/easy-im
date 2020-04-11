package com.tianyuan.easyim.common.token;

import com.tianyuan.easyim.common.exception.TokenVerificationException;
import com.tianyuan.easyim.common.model.TokenData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenTest {

    private static File privateRsaKeyFile;

    private static File publicRsaKeyFile;

    @BeforeAll
    static void init() throws Exception {
        privateRsaKeyFile = new File(TokenTest.class.getClassLoader().getResource("private_key.der")
                .getFile());
        publicRsaKeyFile = new File(TokenTest.class.getClassLoader().getResource("public_key.der")
                .getFile());
    }

    @Test
    void generateToken_andThenVerify() throws Exception {
        TokenGenerator generator = new TokenGenerator(privateRsaKeyFile);
        TokenData data = new TokenData("shen.yuhang", System.currentTimeMillis());
        String token = generator.newJwt(data, 1);

        TokenVerifier verifier = new TokenVerifier(publicRsaKeyFile);
        Token<TokenData> tokenObj = verifier.verifyAndGetData(token, TokenData.class);

        Assertions.assertEquals("shen.yuhang", tokenObj.getData().getUsername());
    }

    @Test
    void generateToken_andThenVerify_expired() throws Exception {
        TokenGenerator generator = new TokenGenerator(privateRsaKeyFile);
        TokenData data = new TokenData("shen.yuhang", System.currentTimeMillis());
        String token = generator.newJwt(data, -1);

        TokenVerifier verifier = new TokenVerifier(publicRsaKeyFile);
        assertThrows(TokenVerificationException.class,
                () -> verifier.verifyAndGetData(token, TokenData.class));
    }
}
