Salvavidas.TabelaDeItensVenda = (function() {
	
	function TabelaDeItensVenda(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaLivrosContainer = $('.js-container-de-itens-de-venda');
		
		this.uuid = $('#uuid').val();
		
		// BOX - atualiza valor Total -> emite evento para ser escutado em venda.js
		this.emitterEvent = $({});
		this.on = this.emitterEvent.on.bind(this.emitterEvent);
	}
	
	TabelaDeItensVenda.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionadoListener.bind(this));
		
		bindQuantidadeDeItens.call(this);
		bindTabelaItem.call(this);
	}
	
	TabelaDeItensVenda.prototype.valorTotal = function() {
		return this.tabelaLivrosContainer.data('valor');
	}
	
	function onItemSelecionadoListener(evento, item) {
		var resposta = $.ajax({
			url: '/vendas/item',
			method: 'GET',
			data: {
				codigoLivro: item.codigo,
				uuid: this.uuid
			}
		});		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onItemAtualizadoNoServidor(html) {
		this.tabelaLivrosContainer.html(html);
		
		bindQuantidadeDeItens.call(this);
		
		var tabelaItemValorTotal = bindTabelaItem.call(this);
		
		// BOX - atualiza valor Total
		this.emitterEvent.trigger('tabela-itens-atualizada-listener', tabelaItemValorTotal.data('valor-total'));
	}
	
	function onQuantidadeDeItemAlteradoAposTemplateRenderizar(evento) {		
		var input = $(evento.target);
		var novaQuantidade = input.val();
		
		if(novaQuantidade <= 0) {
			input.val(1);
			novaQuantidade = 1;
		}
		var codigoLivro = input.data('codigo-livro');
		var resposta = $.ajax({
			url: 'item/' + codigoLivro,
			method: 'GET',
			data: {
				quantidade: novaQuantidade,
				uuid: this.uuid
			}
		});
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onDoubleClick(evento) {
		var item = $(evento.currentTarget);
		item.toggleClass('solicitando-exclusao');
	}
	
	function onExclusaoItemClick(evento) {
		var codigoLivro = $(evento.target).data('codigo-livro');
		
		var resposta = $.ajax({
			url: 'item/' + this.uuid + '/' + codigoLivro,
			method: 'GET'
		});
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function bindQuantidadeDeItens() {
		var quantidadeItemInput = $('.js-tabela-livro-quantidade-item');
		quantidadeItemInput.on('change', onQuantidadeDeItemAlteradoAposTemplateRenderizar.bind(this));
		quantidadeItemInput.maskMoney({ precision: 0, thousands: '' });
	}
	
	function bindTabelaItem() {
		var tabelaItem = $('.js-tabela-item-excluir');
		tabelaItem.on('dblclick', onDoubleClick)
		
		$('.js-btn-exclusao-item').on('click', onExclusaoItemClick.bind(this));
		
		return tabelaItem;
	}
	
	return TabelaDeItensVenda;
}());


