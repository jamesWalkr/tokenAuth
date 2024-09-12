package com.example.security_demo_v2.Config;

import javax.crypto.KeyGenerator;
import java.security.KeyPair;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;


public class Jwks {
    public Jwks() {
    }

    public static RSAKey generateRsa(){

        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

    }
}
