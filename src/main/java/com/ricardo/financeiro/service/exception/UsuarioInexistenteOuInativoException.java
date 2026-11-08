package com.ricardo.financeiro.service.exception;

public class UsuarioInexistenteOuInativoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UsuarioInexistenteOuInativoException(String message) {
		super(message);
	}

}
