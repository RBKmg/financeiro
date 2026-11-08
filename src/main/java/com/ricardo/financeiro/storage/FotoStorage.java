package com.ricardo.financeiro.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	public String salvarTemporariamente(MultipartFile[] files);

	public byte[] recuperarFotoTemporaria(String nome);
	
	public void salvar(String foto);

	public byte[] recuperarFotoThumbnail(String foto);

	public byte[] recuperarThumbnailEmail(String string);
	
	public void excluir(String foto);

}
