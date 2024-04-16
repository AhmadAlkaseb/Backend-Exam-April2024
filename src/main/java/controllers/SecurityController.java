package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dtos.TokenDTO;
import dtos.UserDTO;
import exceptions.APIException;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import persistence.model.Role;
import persistence.model.SellerJPA;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
/*
public class SecurityController {

    private static EntityManagerFactory emf;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SecurityController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Handler login() {
        return (ctx) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode returnObject = objectMapper.createObjectNode();
            try {
                // Parse the email from the request body JSON object
                String email = ctx.bodyAsClass(ObjectNode.class).get("email").asText();
                System.out.println("Email in LOGIN: " + email);

                SellerJPA verifiedUserEntity;
                try (EntityManager em = emf.createEntityManager()) {
                    verifiedUserEntity = em.find(SellerJPA.class, email);
                }
                System.out.println("Verified User Entity: " + verifiedUserEntity); // Add this line for debugging
                String token = createToken(verifiedUserEntity);
                System.out.println("Generated Token: " + token); // Add this line for debugging
                ctx.status(200).json(new TokenDTO(token, verifiedUserEntity.getEmail()));

            } catch (EntityNotFoundException | ValidationException e) {
                ctx.status(401);
                System.out.println(e.getMessage());
                ctx.json(returnObject.put("msg", e.getMessage()));
            }
        };
    }

    public String createToken(SellerJPA user) {
        String ISSUER;
        String TOKEN_EXPIRE_TIME;
        String SECRET_KEY;

        if (System.getenv("DEPLOYED") != null) {
            ISSUER = System.getenv("ISSUER");
            TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
            SECRET_KEY = System.getenv("SECRET_KEY");
        } else {
            ISSUER = "Thomas Hartmann";
            TOKEN_EXPIRE_TIME = "1800000"; // 30 minutes in milliseconds
            SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
        }
        return createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
    }

    public boolean authorize(SellerJPA user, Set<String> allowedRoles) {
        AtomicBoolean hasAccess = new AtomicBoolean(false);
        if (user != null) {
            user.getRoles().stream().map(Role::getName) // Get the role name
                    .map(String::toUpperCase) // Convert to uppercase
                    .forEach(roleName -> {
                        if (allowedRoles.contains(roleName)) {
                            hasAccess.set(true);
                        }
                    });
        }
        return hasAccess.get();
    }

    private String createToken(SellerJPA user, String ISSUER, String TOKEN_EXPIRE_TIME, String SECRET_KEY) {
        // https://codecurated.com/blog/introduction-to-jwt-jws-jwe-jwa-jwk/
        try {
            // Convert roles to comma-separated string
            String rolesString = user.getRoles().stream()
                    .map(Role::getName) // Assuming Role has a method getName() to retrieve the role name
                    .collect(Collectors.joining(","));

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .claim("email", user.getEmail())
                    .claim("roles", rolesString) // Use the rolesString here
                    .expirationTime(new Date(new Date().getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();

        } catch (JOSEException e) {
            e.printStackTrace();
            throw new APIException(500, "Could not create token", "" + LocalTime.now());
        }
    }

    public Handler authenticate() {
        // To check the users roles against the allowed roles for the endpoint (managed by javalins accessManager)
        // Checked in 'before filter' -> Check for Authorization header to find token.
        // Find user inside the token, forward the ctx object with userDTO on attribute
        // When ctx hits the endpoint it will have the user on the attribute to check for roles (ApplicationConfig -> accessManager)
        ObjectNode returnObject = objectMapper.createObjectNode();
        return (ctx) -> {
            if (ctx.method().toString().equals("OPTIONS")) {
                ctx.status(200);
                return;
            }
            String header = ctx.header("Authorization");
            if (header == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Authorization header missing"));
                return;
            }
            String token = header.split(" ")[1];
            if (token == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Authorization header malformed"));
                return;
            }
            UserDTO verifiedTokenUser = verifyToken(token);
            if (verifiedTokenUser == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(returnObject.put("msg", "Invalid User or Token"));
            }
            System.out.println("USER IN AUTHENTICATE: " + verifiedTokenUser);
            ctx.attribute("user", verifiedTokenUser);
        };
    }

    public UserDTO verifyToken(String token) {
        boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
        String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY") : "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";

        try {
            if (tokenIsValid(token, SECRET) && tokenNotExpired(token)) {
                return getUserWithRolesFromToken(token);
            } else {
                throw new APIException(403, "Token is not valid", "" + LocalTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Could not verify token", "" + LocalTime.now());
        }
    }

    public boolean tokenNotExpired(String token) throws ParseException {
        if (timeToExpire(token) > 0)
            return true;
        else
            throw new APIException(403, "Token has expired", "" + LocalTime.now());
    }

    public boolean tokenIsValid(String token, String secret) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        if (jwt.verify(new MACVerifier(secret)))
            return true;
        else
            throw new APIException(403, "Token is not valid", "" + LocalTime.now());
    }

    public int timeToExpire(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime());
    }

    public UserDTO getUserWithRolesFromToken(String token) throws ParseException {
        // Return a user with Set of roles as strings
        SignedJWT jwt = SignedJWT.parse(token);
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("email").toString();

        Set<String> rolesSet = Arrays
                .stream(roles.split(","))
                .collect(Collectors.toSet());
        return new UserDTO(username, rolesSet);
    }
}*/
