package com.proriberaapp.ribera.Crosscutting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        //mailSender.setUsername("riberaclubcieneguilla@gmail.com");
        //mailSender.setPassword("sistemas22");
        mailSender.setUsername("inclubnotification@inclub.world");
        mailSender.setPassword("Solopro100");
        //mailSender.setUsername("cieneguillaclubresortribera@gmail.com");
        //mailSender.setPassword("Sistemas#1");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
