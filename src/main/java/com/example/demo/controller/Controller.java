package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AuthRequest;
import com.example.demo.model.AuthResponse;
import com.example.demo.service.MyUserDetailsService;
import com.example.demo.util.JwtUtil;

@RestController
public class Controller {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/hello")
	public String hello(HttpServletRequest request) {
		return "Hello World "+ request.getAttribute("username");
	}

	@PostMapping("/reterieveToken")
	public ResponseEntity<?> reterieveToken(@RequestBody AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (AuthenticationException e) {
			throw new UsernameNotFoundException("Invalid username");
		}
		
		UserDetails details= myUserDetailsService.loadUserByUsername(authRequest.getUsername());
		String jwt= jwtUtil.generateToken(details);
		
		return ResponseEntity.ok(new AuthResponse(jwt));
		
	}

}
