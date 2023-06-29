package dev.blazo.crud.security.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import dev.blazo.crud.security.dto.JwtDTO;
import dev.blazo.crud.security.entity.PrimaryUser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        PrimaryUser primaryUser = (PrimaryUser) authentication.getPrincipal();
        List<String> roles = primaryUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(primaryUser.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    /**
     * The function `getUsernameFromToken` takes a token as input, parses it using a
     * secret key, and
     * returns the username extracted from the token.
     * 
     * @param token The token parameter is a string that represents a JSON Web Token
     *              (JWT).
     * @return The method is returning a String value, which is the username
     *         extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        // Parsing the given token using the secret key and extracting the subject
        // (username) from
        // the token's body.
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Error: Token is malformed");
        } catch (UnsupportedJwtException e) {
            logger.error("Error: Token is unsupported");
        } catch (ExpiredJwtException e) {
            logger.error("Error: Token is expired");
        } catch (IllegalArgumentException e) {
            logger.error("Error: Token is empty");
        } catch (SignatureException e) {
            logger.error("Error: Token has an invalid signature");
        }

        return false;
    }

    public String refreshToken(JwtDTO jwtDTO) throws ParseException {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(jwtDTO.getToken());
        } catch (ExpiredJwtException e) {
            JWT jwt = JWTParser.parse(jwtDTO.getToken());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            String username = claims.getSubject();
            List<String> roles = (List<String>) claims.getClaim("roles");

            return Jwts.builder()
                    .setSubject(username)
                    .claim("roles", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + expiration))
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                    .compact();
        }

        return null;
    }
}
