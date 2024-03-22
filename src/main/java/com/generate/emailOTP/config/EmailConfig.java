package com.generate.emailOTP.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	@Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;
    
    public JavaMailSender getJavaMailSender() {
    	JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    	javaMailSender.setHost(mailHost);
    	javaMailSender.setPort(Integer.parseInt(mailPort));
    	javaMailSender.setUsername(mailUsername);
    	javaMailSender.setPassword(mailPassword);
    	
    	Properties props = javaMailSender.getJavaMailProperties();
    	props.put("mail.smpt.starttls.enable", "true");
    	return javaMailSender;
    }
}
