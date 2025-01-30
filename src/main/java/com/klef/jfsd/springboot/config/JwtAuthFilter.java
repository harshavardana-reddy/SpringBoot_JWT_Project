package com.klef.jfsd.springboot.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    /**
     * The `doFilterInternal` method in this Java code extracts a JWT token from the Authorization header, validates it, and sets the authentication in the SecurityContext if the token is valid.
     * 
     * @param request The `request` parameter in the `doFilterInternal` method represents the HTTP request that is being processed by the filter. It contains information about the client's request, such as headers, parameters, and body content. The `HttpServletRequest` class provides methods to access and manipulate this information.
     * @param response The `response` parameter in the `doFilterInternal` method represents the HTTP response that will be sent back to the client after processing the request. You can use this parameter to set headers, write content to the response body, or manipulate the response in any other way before it is sent back to
     * @param filterChain The `filterChain` parameter in the `doFilterInternal` method is an object that represents a chain of filters to be executed in a specific order. When the `doFilter` method is called on the `filterChain` object, it continues the execution of the filter chain by passing the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
//        System.out.println("JWT Authentication Filter Triggered");

        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Validate Authorization header and extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtService.extractUsername(token); // Extract username from token
            } catch (Exception e) {
                System.err.println("Error extracting username from token: " + e.getMessage());
            }
        }

        // Process only if username is extracted and the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
//            System.out.println(userDetails);
//            System.out.println(token);
            // Validate the token
            if (jwtService.validateToken(token, userDetails)) {
                // Create authentication token
//            	 System.out.println(userDetails.getPassword());
                UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
//                authenticationToken.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else {
				System.out.println("Invalid Token");
			}
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
