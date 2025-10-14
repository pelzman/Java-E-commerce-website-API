package com.ecommerce.project.security.jwt;


import com.ecommerce.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
// IMPLEMENTING JW-COOKIE
@Component
public class JwtUtils {

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ecom.app.jwtCookie}")
    private String jwtCookie;
    private  static  final Logger logger = (Logger) LoggerFactory.getLogger(JwtUtils.class);
    //Getting jwt from the header
//    public String getJwtFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization") ;
//        logger.debug("Authorization header : {}" , bearerToken);
//        if(bearerToken != null && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public String getJwtFromCookie(HttpServletRequest request) {
      Cookie cookie = WebUtils.getCookie( request,jwtCookie)  ;
     if(cookie != null)    {
         return  cookie.getValue();
     }
     else {
         return  null;
     }

    }

     public ResponseCookie generateJwtFromCookie(UserDetailsImpl userPrincipal){
       String jwt = generateJwtTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie ,jwt)
                .path("/api")
                .httpOnly(false)
                .maxAge(24 *60* 60)
                .build();
          return cookie ;
     }

    public ResponseCookie generateCleanCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie ,"null")
                .path("/api")
                .build();
        return cookie ;
    }
    //Generating Token from username
    public String generateJwtTokenFromUsername(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date( new Date().getTime() +  jwtExpirationMs))
                .signWith(key())
                .compact();

    }

    // Getting username from jwt Token
    public String getUsernameFromJwtToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }
    // Generate Signing key

    public Key key(){

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    // Validate jwt Token

    public boolean validateJwtToken(String authToken){
        try {

            Jwts.parserBuilder()
                    .setSigningKey((SecretKey) key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }
        catch(MalformedJwtException  e){
            logger.error( "Invalid JWT token : {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error( "JWT token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error( "JWT token not supported : {}", e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.error( "JWT claim string is empty: {}", e.getMessage());
        }

        return false;
    }


}
// IMPLEMENTING JW-TOKEN FROM HEADERS

//@Component
//public class JwtUtils {
//
//    @Value("${spring.app.jwtExpirationMs}")
//    private long jwtExpirationMs;
//
//    @Value("${spring.app.jwtSecret}")
//    private String jwtSecret;
//    private  static  final Logger logger = (Logger) LoggerFactory.getLogger(JwtUtils.class);
//    //Getting jwt from the header
//    public String getJwtFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization") ;
//        logger.debug("Authorization header : {}" , bearerToken);
//        if(bearerToken != null && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//    //Generating Token from username
//    public String generateJwtTokenFromUsername(UserDetailsImpl userDetails){
//        String userName = userDetails.getUsername();
//        return Jwts.builder()
//                .setSubject(userName)
//                .claim("roles", userDetails.getAuthorities()
//                        .stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toSet()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date( new Date().getTime() +  jwtExpirationMs))
//                .signWith(key())
//                .compact();
//
//    }
//
//    // Getting username from jwt Token
//    public String getUsernameFromJwtToken(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//
//    }
//    // Generate Signing key
//
//    public Key key(){
//
//        return Keys.hmacShaKeyFor(
//                Decoders.BASE64.decode(jwtSecret)
//        );
//    }
//    // Validate jwt Token
//
//    public boolean validateJwtToken(String authToken){
//        try {
//
//            Jwts.parserBuilder()
//                    .setSigningKey((SecretKey) key())
//                    .build()
//                    .parseClaimsJws(authToken);
//            return true;
//        }
//        catch(MalformedJwtException  e){
//            logger.error( "Invalid JWT token : {}", e.getMessage());
//        } catch (ExpiredJwtException e){
//            logger.error( "JWT token has expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            logger.error( "JWT token not supported : {}", e.getMessage());
//        }
//        catch (IllegalArgumentException e){
//            logger.error( "JWT claim string is empty: {}", e.getMessage());
//        }
//
//        return false;
//    }
//
//
//}
















