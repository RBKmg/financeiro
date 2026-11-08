package com.ricardo.financeiro.service.exception;

public class NomeDaCategoriaJaCadastradaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NomeDaCategoriaJaCadastradaException(String mensagem ) {
		super(mensagem);
	}

}
