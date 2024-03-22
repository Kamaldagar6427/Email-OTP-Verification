package com.generate.emailOTP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generate.emailOTP.dto.LoginDto;
import com.generate.emailOTP.dto.RegistterDto;
import com.generate.emailOTP.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegistterDto registterDto) {
		return new ResponseEntity<>(userService.register(registterDto), HttpStatus.OK);
	}

	@PutMapping("/verify-account")
	public ResponseEntity<String> veryfyAccount(@RequestParam String email, @RequestParam String otp) {
		return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
	}

	@PutMapping("/regenerate-otp")
	public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
		return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
	}

	@PutMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
		return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
	}
}
