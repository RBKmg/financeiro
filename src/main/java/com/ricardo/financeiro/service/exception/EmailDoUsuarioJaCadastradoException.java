package com.ricardo.financeiro.service.exception;

public class EmailDoUsuarioJaCadastradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EmailDoUsuarioJaCadastradoException(String message) {
		super(message);
	}

}
