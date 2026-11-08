package com.ricardo.financeiro.model;

public enum TipoLancamento {
	
	RECEITA("Entrada", "Recebimento"),
	DESPESA("Saída", "Pagamento");
	
	private String descricao;
	private String documento;
	
	private TipoLancamento(String descricao, String documento) {
		this.descricao = descricao;
		this.documento = documento;
	}

	public String getDescricao() {
		return descricao;
	}
	public String getDocumento() {
		return documento;
	}	

}
