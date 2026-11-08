Salvavidas.Venda = (function() {
	
	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
		
		this.valorTotalBox = $('.js-valor-total-box');
		this.valorTotalNegativo = $('.js-valor-total-negativo');
		
		this.valorFreteInput = $('#valorFrete');
		this.valorDescontoInput = $('#valorDesconto');
		
		this.valorTotalItens = this.tabelaItens.valorTotal() || 0;
		this.valorFrete = this.valorFreteInput.data('valor') || 0;
		this.valorDesconto = this.valorDescontoInput.data('valor') || 0;
	}
	
	Venda.prototype.iniciar = function() {
		this.tabelaItens.on('tabela-itens-atualizada-listener', onTabelaItensAtualizada.bind(this));		
		this.valorFreteInput.on('keyup', onIncluindoValorFrete.bind(this));		
		this.valorDescontoInput.on('keyup', onIncluindoValorDesconto.bind(this));
		
		this.tabelaItens.on('tabela-itens-atualizada-listener', onValoresAlterados.bind(this));		
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
		
		onValoresAlterados.call(this);
	}
	
	function onTabelaItensAtualizada(evento, valorTotalItens) {
		this.valorTotalItens = valorTotalItens == null ? 0 : valorTotalItens;		
	}
	
	function onIncluindoValorFrete(evento) {
		this.valorFrete = Salvavidas.recuperarValor($(evento.target).val()) || 0;
	}
	
	function onIncluindoValorDesconto(evento) {
		this.valorDesconto = Salvavidas.recuperarValor($(evento.target).val()) || 0;
	}
	
	function onValoresAlterados() {
		/*console.log('TT', typeof this.valorTotalItens);
		console.log('TT Parse float', typeof parseFloat(this.valorTotalItens));
		console.log('VF', typeof this.valorFrete);
		console.log('VD', typeof this.valorDesconto);
		console.log('SOMA ', typeof this.valorTotalItens + this.valorFrete);*/
		
		var valorTotalIncluidoFreteEDesconto = parseFloat(this.valorTotalItens || 0) + numeral(this.valorFrete || 0).value() - numeral(this.valorDesconto || 0).value();
		this.valorTotalBox.html(Salvavidas.formatarMoedaOutput(valorTotalIncluidoFreteEDesconto));
		
		this.valorTotalNegativo.toggleClass('negativo', valorTotalIncluidoFreteEDesconto < 0);
	}
	
	return Venda;
}());


$(function(){
	var autocomplete = new Salvavidas.Autocomplete();
	autocomplete.iniciar();
	
	var tabelaDeItensVenda = new Salvavidas.TabelaDeItensVenda(autocomplete);
	tabelaDeItensVenda.iniciar();
	
	var venda = new Salvavidas.Venda(tabelaDeItensVenda);
	venda.iniciar();
});