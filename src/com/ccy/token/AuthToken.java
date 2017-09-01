package com.ccy.token;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;

import java.util.Date;    

public class AuthToken {

	//Sample method to construct a JWT
	 
	private String createJWT(String id, String issuer, String subject, long ttlMillis) {
	 
	//The JWT signature algorithm we will be using to sign the token
	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	long nowMillis = System.currentTimeMillis();
	Date now = new Date(nowMillis);
	 
	//We will sign our JWT with our ApiKey secret
	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("{userid:123,password:123456}");
	Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	  //Let's set the JWT Claims
	JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);
	 
	//if it has been specified, let's add the expiration
	if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	    Date exp = new Date(expMillis);
	    builder.setExpiration(exp);
	}
	 
	//Builds the JWT and serializes it to a compact, URL-safe string
	return builder.compact();
	}
	
	//Sample method to validate and read the JWT
	private void parseJWT(String jwt)  throws ExpiredJwtException{
	//This line will throw an exception if it is not a signed JWS (as expected)
		try {
			Claims claims = Jwts.parser()        
			   .setSigningKey(DatatypeConverter.parseBase64Binary("{userid:123,password:123456}"))
			   .parseClaimsJws(jwt).getBody();
				System.out.println("ID: " + claims.getId());
				System.out.println("Subject: " + claims.getSubject());
				System.out.println("Issuer: " + claims.getIssuer());
				System.out.println("Expiration: " + claims.getExpiration());
		} catch (ExpiredJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		AuthToken authToken = new AuthToken();
		System.out.println(authToken.createJWT("1", "1", "nihao", 123));
		
		authToken.parseJWT(authToken.createJWT("1", "1", "nihao", 123));
	}
}
