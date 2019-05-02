package com.jean.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.jean.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);

}
