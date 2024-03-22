package com.generate.emailOTP.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generate.emailOTP.dto.LoginDto;
import com.generate.emailOTP.dto.RegistterDto;
import com.generate.emailOTP.entity.User;
import com.generate.emailOTP.repository.UserRepository;
import com.generate.emailOTP.util.EmailUtil;
import com.generate.emailOTP.util.OtpUtil;
import jakarta.mail.MessagingException;

@Service
public class UserService {
	@Autowired
	private OtpUtil otpUtil;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private UserRepository userRepository;

	public String register(RegistterDto registterDto) {
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(registterDto.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}
		User user = new User();
		user.setName(registterDto.getName());
		user.setEmail(registterDto.getEmail());
		user.setPassword(registterDto.getPassword());
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);

		return "User registration successful";
	}

	public String verifyAccount(String email, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (2 * 60)) {
			user.setActive(true);
			userRepository.save(user);
			return "OTP verified you can login";
		}
		return "Please regenerate otp and try again";
	}

	public String regenerateOtp(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email : " + email));
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(email, otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		return "Email sent... please verify account within 1 minute";
	}

	public String login(LoginDto loginDto) {
		User user = userRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found with this email : " + loginDto.getEmail()));
		if (!loginDto.getPassword().equals(user.getPassword())) {
			return "Password is incorrect";
		} else if (!user.isActive()) {
			return "your account is not veryfied";
		}
		return "Login successful";
	}
}
