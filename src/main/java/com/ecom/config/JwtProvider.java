package com.ecom.config;

import com.ecom.exception.CustomAuthenticationException;
import com.ecom.modal.CustomUser;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private long jwtExpiration;

	public String generateJwtToken(Authentication authentication) {
        CustomUser userPrincipal = (CustomUser) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(String.valueOf(userPrincipal.getEmail()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
	}
	
	public String getUserIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken, HttpServletRequest request) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(jwtSecret)
					.build()
					.parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			throw new CustomAuthenticationException("Invalid JWT signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			throw new CustomAuthenticationException("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			throw new CustomAuthenticationException("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			throw new CustomAuthenticationException("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			throw new CustomAuthenticationException("JWT claims string is empty: " + e.getMessage());
		}
	}

}
