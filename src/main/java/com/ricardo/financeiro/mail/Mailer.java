package com.ricardo.financeiro.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ricardo.financeiro.model.ItemVenda;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.model.Venda;
import com.ricardo.financeiro.storage.FotoStorage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class Mailer {
	
	private static Logger logger = LoggerFactory.getLogger(Mailer.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Async
	public void enviar(Venda venda) {
		
		Context context = new Context(Locale.of("pt", "BR"));
		context.setVariable("venda", venda);
		context.setVariable("logo", "logo");
	
		
		Map<String, String> fotos = new HashMap<>();
		boolean adicionarMockLivro = false;
		for(ItemVenda item : venda.getItens()) {
			Livro livro = item.getLivro();
			
			if(livro.temFoto()) {
				String cid = "foto-" + livro.getCodigo();
				context.setVariable(cid, cid);
				
				fotos.put(cid, livro.getFoto() + "|" + livro.getContentType());	
			} else {
				adicionarMockLivro = true;
				context.setVariable("mockLivro", "mockLivro");
			}
					
		}
		
		try {
			String emailBody = thymeleaf.process("mail/ResumoVenda", context);	
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			helper.setFrom("ricbapdevs@gmail.com");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject(String.format("Salva vidas - Venda nº %d", venda.getCodigo()));
			helper.setText(emailBody, true);
			
			helper.addInline("logo", new ClassPathResource("static/images/logo-blue.png"));
			
			if(adicionarMockLivro) {
				helper.addInline("mockLivro", new ClassPathResource("static/images/sem-foto-mock.png"));
			}
			
			for(String cid : fotos.keySet()) {
				String[] fotoContentType = fotos.get(cid).split("\\|");
				String foto = fotoContentType[0];
				String contentType = fotoContentType[1];
				
				byte[] arrayFoto = fotoStorage.recuperarThumbnailEmail(foto);
				helper.addInline(cid, new ByteArrayResource(arrayFoto), contentType);
			}
			
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("Erro ao enviar email", e);
		}
	}

}


/*
SimpleMailMessage mensagem = new SimpleMailMessage();
mensagem.setFrom("ric_bap@hotmail.com");
mensagem.setTo(venda.getCliente().getEmail());
mensagem.setSubject("Venda efetuada");
mensagem.setText("Obrigado sua venda foi processada");		
mailSender.send(mensagem);
*/