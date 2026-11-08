package com.ricardo.financeiro.service.event.venda;

import com.ricardo.financeiro.model.Venda;

public class VendaEvent {
	
	private Venda venda;
	

	public VendaEvent(Venda venda) {
		this.venda = venda;
	}
	

	public Venda getVenda() {
		return venda;
	}	

}
