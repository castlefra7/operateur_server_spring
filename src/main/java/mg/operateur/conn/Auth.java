/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.conn;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.SecretKey;

/**
 *
 * @author lacha
 */
public class Auth {
    private static String SECRET = "123123123112312312311231231231222";
    
    public static Key getKey() {
         SecretKey key;
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return key;
    }
}
