package com.paulo.pontointeligente.api.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;

public class PasswordUtilsTest {

    private static final String SENHA = "123456";
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void testGerarHashSenha() throws Exception {
        String hash = PasswordUtils.gerarBcrypt(SENHA);
        assertTrue(encoder.matches(SENHA, hash));
    }

    @Test
    public void testSenhaNula() throws Exception {
        assertNull(PasswordUtils.gerarBcrypt(null));
    }

}