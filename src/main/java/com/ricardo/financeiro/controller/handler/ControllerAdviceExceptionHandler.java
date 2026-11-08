package com.ricardo.financeiro.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ricardo.financeiro.service.exception.NomeDaCategoriaJaCadastradaException;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {
	
	@ExceptionHandler(NomeDaCategoriaJaCadastradaException.class)
	public ResponseEntity<String> handleNomeDaCategoriaJaCadastradaException(NomeDaCategoriaJaCadastradaException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

}
