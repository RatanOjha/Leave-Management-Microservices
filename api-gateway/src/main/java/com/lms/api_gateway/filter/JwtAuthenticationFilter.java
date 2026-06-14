package com.lms.api_gateway.filter;

// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
//
// import io.jsonwebtoken.Jwts;
// import reactor.core.publisher.Mono;
//
// @Component
// public class JwtAuthenticationFilter implements GatewayFilter {
//
// private final String SECRET = "mySecretKey123456";
//
// @Override
// public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
// String path = exchange.getRequest().getURI().getPath();
//
// // allow login without token
// if (path.contains("/auth/login")) {
// return chain.filter(exchange);
// }
//
// // check header
// if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
// throw new RuntimeException("Missing Authorization header");
// }
//
// String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
// if (authHeader == null || !authHeader.startsWith("Bearer ")) {
// throw new RuntimeException("Invalid Authorization header");
// }
//
// String token = authHeader.substring(7);
//
// try {
// Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token);
//
// } catch (Exception e) {
// throw new RuntimeException("Invalid or expired token");
// }
//
// return chain.filter(exchange);
// }
// }