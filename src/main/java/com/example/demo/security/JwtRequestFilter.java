package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.MyUserDetailsServices;
import com.example.demo.util.Jwtutil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final Jwtutil jwtutil;
	private final MyUserDetailsServices userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String path = request.getServletPath();

	    // Skip public endpoints
	    if (path.equals("/register") || path.equals("/activate") || path.equals("/login")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String authHeader = request.getHeader("Authorization");
	    String email = null;
	    String token = null;

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);
	        email = jwtutil.extractUsername(token);
	    }

	    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
	        if (jwtutil.validateToken(token, userDetails)) {
	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	            SecurityContextHolder.getContext().setAuthentication(authToken); // ✅
	        }
	    }

	    filterChain.doFilter(request, response);
	}


}
