package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Infraestructure.services.admin.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailServiceImpl implements SendMailService {
    @Override
    public void sendMail(String email, String message) {
        log.info("Sending mail to: " + email + " with message: " + message);
    }
}
