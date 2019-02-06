package com.felipeschottz.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.felipeschottz.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
}
