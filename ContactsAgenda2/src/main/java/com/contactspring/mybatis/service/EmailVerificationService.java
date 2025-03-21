package com.contactspring.mybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.generate.model.UserHistory;
import com.contactspring.mybatis.original.QueryMapper;

@Service
public class EmailVerificationService {
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private QueryMapper queryMapper;
	
	public void sendVerificationEmail(String email, String verificationToken) {
		
		String subject = "Activate your contact agenda account";
		String activationLink = "http://localhost:8080/activate?token=" + verificationToken;
		String body = "Please follow the link below to activate your account:\n" + activationLink + " \n This link will expire in 15 minutes.";
		String senderEmail = "burnerneutralemail@gmail.com";
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail);
		System.out.println("Email to use: " + email);
		message.setTo(email);
		System.out.println("Subject of the email to be sent: " + subject);
		message.setSubject(subject);
		System.out.println("Body of the email to be sent: " + body);
		message.setText(body);
		
		mailSender.send(message);
	}
	
	public void passwordResetEmail(String email, String verificationToken) {
		
		String subject = "Please reset your contact agenda password";
		String activationLink = "http://localhost:8080/passwordReset?token=" + verificationToken;
		String body = "Please follow the link below to reset your password:\n" + activationLink + " \n This link will expire in 15 minutes.";
		String senderEmail = "burnerneutralemail@gmail.com";
		ValidatedUserProperties userPro = new ValidatedUserProperties();
		userPro = queryMapper.findByEmailPro(email);
		Boolean activeOrNot = userPro.getActive();
		
		if (activeOrNot) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderEmail);
			System.out.println("Email to use: " + email);
			message.setTo(email);
			System.out.println("Subject of the email to be sent: " + subject);
			message.setSubject(subject);
			System.out.println("Body of the email to be sent: " + body);
			message.setText(body);
			mailSender.send(message);
		} else {
			System.out.println("The user " + userPro.getUsername() + " is deactivated.");
			subject = "This user is deactivated";
			body = "This user is deactivated, please contact the administrators over " + senderEmail;
			
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderEmail);
			System.out.println("Email to use: " + email);
			message.setTo(email);
			System.out.println("Subject of the email to be sent: " + subject);
			message.setSubject(subject);
			System.out.println("Body of the email to be sent: " + body);
			message.setText(body);
			mailSender.send(message);
		}
	}
	
	public void sendUsernameOverEmail(String email, String username) {
		
		String url = "Login/index-login";
		UserHistory userHistory = new UserHistory();
		userHistory = queryMapper.findByUsernameHis(username);

		String subject = "This is your contact agenda username";
		String body = "";
		Boolean userHisExists = queryMapper.usernameExistsHis(username);
		String senderEmail = "burnerneutralemail@gmail.com";

		if (userHisExists) {
			body = "You have requested the remembering of your username, it is the following:\n" + username + "\nand your last login was on: \n" + userHistory.getLastLogin() + "\n\n You can follow this link to login: \n\n" + url;
		} else {
			body = "You have requested the remembering of your username, it is the following:\n" + username + "\n\nYou can follow this link to login for your first login: \n\n" + url;
	
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail);
		System.out.println("Email to use: " + email);
		message.setTo(email);
		System.out.println("Subject of the email to be sent: " + subject);
		message.setSubject(subject);
		System.out.println("Body of the email to be sent: " + body);
		message.setText(body);
		
		mailSender.send(message);
	}
	
	public void sendExpirationMail(String email) {
		
		String url = "Login/index-login";
		
		String subject = "Your link email expired";
		String body = "Your activation link expired. Your information has been deleted from our servers. In order to use our services, please register again by following this link: \n\n" + url;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("burnerneutralemail@gmail.com");
		System.out.println("Email to use: " + email);
		message.setTo(email);
		System.out.println("Subject of the email to be sent: " + subject);
		message.setSubject(subject);
		System.out.println("Body of the email to be sent: " + body);
		message.setText(body);
		
		mailSender.send(message);
	}
	
	public void sendDeactivationMail(String email) {
		
		String url = "Login/index-login";
		
		String subject = "Your link email expired";
		String body = "Your activation link expired. Your user has been deactivated, please contact the administrators of the page to regain access to your user.";
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("burnerneutralemail@gmail.com");
		System.out.println("Email to use: " + email);
		message.setTo(email);
		System.out.println("Subject of the email to be sent: " + subject);
		message.setSubject(subject);
		System.out.println("Body of the email to be sent: " + body);
		message.setText(body);
		
		mailSender.send(message);
	}
	
	public void notifyDeactivation(String email) {
		
		String url = "Login/index-login";
		String subject = "Your user is deactivated";
		String body = "Your user is deactivated. Plese contact the admin or reset your password. You can reset your password following this link: \n" + url;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("burnerneutralemail@gmail.com");
		System.out.println("Email to use: " + email);
		message.setTo(email);
		System.out.println("Subject of the email to be sent: " + subject);
		message.setSubject(subject);
		System.out.println("Body of the email to be sent: " + body);
		message.setText(body);
		
		mailSender.send(message);
	}

}
