package com.noyon.main.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.noyon.main.entity.OurUsers;
import com.noyon.main.repository.TokenRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenUnit {

	@Autowired
	private TokenRepo tokenRepo;
	
	private final String SecretKey="dskasidsdskjdsakdfak34287sdfkjsdkjskdfjsdflkjfdjk";
	
	
	//generated key OurUsers 
	
	public String generatedToken(OurUsers user)
	{
		
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("role",user.getRole())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+86400000))
				.signWith(getSigninKey())
				.compact();
	}
	
	public SecretKey getSigninKey()
	{
		
		byte [] keyBytes=Decoders.BASE64URL.decode(SecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//extract All Claims
	
	private Claims extractAllClaims(String token)
	{
		
		return Jwts.parser()
				.verifyWith(getSigninKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
				
	}
	
	//extract username
	
	public String extractUsername(String token)
	{
		return extractClaim(token,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token,Function<Claims, T> resolver)
	{
		Claims claims=extractAllClaims(token);
		return resolver.apply(claims);
	}
	
	//check the token is exired or not
	
	public boolean isTokenExpired(String token)
	{
		
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	 //check the token is valid or not
	public boolean isValid(String token,UserDetails user)
	{
		
		String username=extractUsername(token);
		
		boolean validToken=tokenRepo.findByToken(token).map(t -> !t.isLogout()).orElse(false);
		
		return (username.contains(user.getUsername()) && !isTokenExpired(token) && validToken);
	}
	
	//extract user role 
	
	public String extractUserRole(String token)
	{
		return extractClaim(token, claims->claims.get("role",String.class));
	}
	
	
	
}
