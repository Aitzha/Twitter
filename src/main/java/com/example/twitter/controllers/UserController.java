package com.example.twitter.controllers;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * The minimum recommended cost, used by default
     */
    public static final int DEFAULT_COST = 16;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final int SIZE = 128;

    private final SecureRandom random;

    private final int cost;

    public UserController() {
        this.cost = DEFAULT_COST;
        this.random = new SecureRandom();
    }

    public String tempToken;


    /**
     * Hash a password for storage.
     *
     * @return a secure authentication token to be stored for later authentication
     */
    public String hash(char[] password)
    {
        byte[] salt = new byte[SIZE / 8];
        random.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];

        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        System.out.println(enc.encodeToString(salt));
        System.out.println(enc.encodeToString(dk));
        return Integer.toString(cost) + ':' + enc.encodeToString(hash);
    }


    /**
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    public boolean authenticate(char[] password, String token)
    {
        String[] elementsOfToken = token.split(":");

        int iterations = Integer.parseInt(elementsOfToken[0]);
        byte[] hash = Base64.getUrlDecoder().decode(elementsOfToken[1]);
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
        byte[] passwordHash = Arrays.copyOfRange(hash, SIZE / 8, hash.length);
        byte[] check = pbkdf2(password, salt, 1 << iterations);

        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx)
            zero |= passwordHash[idx] ^ check[idx];
        return zero == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
    {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        }
        catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }


    @PostMapping
    public String Registration(@RequestParam("password") String password) {
        tempToken = hash(password.toCharArray());
        System.out.println(tempToken);
        return "hashed";
    }

    @GetMapping
    public boolean Login(@RequestParam("password") String password) {
        return authenticate(password.toCharArray(), tempToken);
    }
}
