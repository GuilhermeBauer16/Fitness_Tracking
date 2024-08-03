package com.github.GuilhermeBauer16.FitnessTracking.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private static final String EXPIRED_TOKEN_MESSAGE = "Expired or invalid JWT token";
    @Value("${SECRET_KEY}")
    private String secretKey;

    @Value("${EXPIRE_LENGTH}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;


    @PostConstruct
    protected void init(){
       secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
       algorithm = Algorithm.HMAC256(secretKey.getBytes());
   }

   public TokenVO createAccessToken(String username, UserProfile userProfile){
       Date now = new Date();
       Date validity = new Date(now.getTime() + validityInMilliseconds);
       String accessToken = getRefreshToken(username, userProfile, now, validity);
       String refreshToken = getRefreshToken(username, userProfile, now);
       return new TokenVO(username,true,now,validity,accessToken,refreshToken);

   }

   public TokenVO createRefreshToken(String refreshToken){
       if(refreshToken.contains("Bearer ")){
           refreshToken = refreshToken.substring("Bearer ".length());
       }

       JWTVerifier verifier = JWT.require(algorithm).build();
       DecodedJWT decodedJWT = verifier.verify(refreshToken);
       String username = decodedJWT.getSubject();
       Class<?> UserProfile = null;
       String decodedJWTClaim = decodedJWT.getClaim("roles").asString();
       UserProfile userProfile = com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile.fromString(decodedJWTClaim);

       return createAccessToken(username,userProfile);
   }



   private String getRefreshToken(String username, UserProfile userProfile, Date now, Date validity){
           String issuerUrl = ServletUriComponentsBuilder
                   .fromCurrentContextPath().build().toUriString();
           return JWT.create()
                   .withClaim("roles",userProfile.getUserProfile())
                   .withIssuedAt(now)
                   .withExpiresAt(validity)
                   .withSubject(username)
                   .withIssuer(issuerUrl)
                   .sign(algorithm).strip();
       }

    private String getRefreshToken(String username, UserProfile userprofile, Date now){
        Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * 3));
        return JWT.create()
                .withClaim("roles",userprofile.getUserProfile())
                .withIssuedAt(now)
                .withExpiresAt(validityRefreshToken)
                .withSubject(username)
                .sign(algorithm).strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return  new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token){
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest req){
       String bearerToken = req.getHeader("Authorization");

       if(bearerToken != null && bearerToken.startsWith("Bearer ")){
           return bearerToken.substring("Bearer ".length());
       }

       return null;
    }

    public Boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if(decodedJWT.getExpiresAt().before(new Date())){
                return false;
            }
            return true;
        }catch (Exception e) {
            throw new RuntimeException(EXPIRED_TOKEN_MESSAGE);
        }
    }

}
