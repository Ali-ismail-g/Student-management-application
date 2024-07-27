package com.app.studentManagementSystem.services;

import com.app.studentManagementSystem.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;

@Service
public class JwtService {
    private static final String secret_key="mySecretKey";
    private long accessTokenValidity = 30*60*1000; //valid till 30 mins

    private final JwtParser jwtParser;

    public JwtService(UserDetailsService userDetailsService) {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }
    private final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public String createToken(User user){
        Date issuedTime = new Date(System.currentTimeMillis());
        Date tokenValidity = new Date(issuedTime.getTime()+accessTokenValidity);

        return  Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .claim("role",user.getRole())
                .setSubject(user.getEmail())
                .setExpiration(tokenValidity)
                .setIssuedAt(issuedTime)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }
    public Claims parseJwtClaims(String token){return jwtParser.parseClaimsJws(token).getBody();}

    public Claims resolveClaims(HttpServletRequest req){
        try {
            String token = resolveToken(req);
            if(token != null){
                return parseJwtClaims(token);
            }
            return null;
        }catch (ExpiredJwtException ex){
            req.setAttribute("expired",ex.getMessage());
            throw ex;
        }catch (Exception ex){
            req.setAttribute("invalid",ex.getMessage());
            throw ex;
        }
    }
    public String resolveToken(HttpServletRequest request){ //extract token
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if(bearerToken!=null&&bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
    public boolean isTokenExpired(Date expirationDate) throws AuthenticationException {
        try {
            if(expirationDate.before(new Date()))
                return true;
            else
                return false;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean isTokenValid(String accessToken, UserDetails userDetails) throws AuthenticationException {
        String username = userDetails.getUsername();
        Claims claims = parseJwtClaims(accessToken);
        return username.equals(claims.getSubject())&&!isTokenExpired(claims.getExpiration());
    }

    public static String extractToken(String token){
        return token.substring(TOKEN_PREFIX.length());
    }
    public static String getRoleFromToken(String jwtToken){
        Claims claims = Jwts.parser()
                .setSigningKey(secret_key)
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.get("role").toString();
    }
}
