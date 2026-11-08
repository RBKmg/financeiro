Salvavidas = Salvavidas || {};

Salvavidas.PesquisaRapidaClientes = (function() {
	
	function PesquisaRapidaClientes() {
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientesModal');
		this.nomeClienteInput = $('#nomeClienteModal');
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-cliente-btn');
		
		this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaClientes');
		this.htmlTabelaPesquisaRapidaClientes = $('#tabela-pesquisa-rapida-clientes').html();
		this.template = Handlebars.compile(this.htmlTabelaPesquisaRapidaClientes);
		
		this.mensagemErro = $('.js-mensagem-erro');
	}
	
	PesquisaRapidaClientes.prototype.iniciar = function() {
		this.pesquisaRapidaBtn.on('click', onPesquisaRapidaClicado.bind(this));
		this.pesquisaRapidaClientesModal.on('shown.bs.modal', onModalShow.bind(this));
	}
	
	function onModalShow() {
		this.nomeClienteInput.focus();
	}
	
	function onPesquisaRapidaClicado(event) {
		event.preventDefault();
		
		$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				nome: this.nomeClienteInput.val()
			},
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisaQuantidadeCaracteresInvalidos.bind(this)
		});
	}
	
	function onPesquisaConcluida(resultado) {
		this.mensagemErro.addClass('hidden');
		
		var html = this.template(resultado);
		this.containerTabelaPesquisa.html(html);
		
		var tabelaPesquisaRapidaClientes = new Salvavidas.TabelaPesquisaRapidaClientes(this.pesquisaRapidaClientesModal);
		tabelaPesquisaRapidaClientes.ativar();
	}
	
	function onErroPesquisaQuantidadeCaracteresInvalidos() {
		this.mensagemErro.removeClass('hidden');
	}
	
	return PesquisaRapidaClientes;
}());

Salvavidas.TabelaPesquisaRapidaClientes = (function() {
	
	function TabelaPesquisaRapidaClientes(modal) {
		this.modalCliente = modal;
		this.clientePesquisado = $('.js-cliente-filtrado-na-pesquisa-rapida');
	}
	
	TabelaPesquisaRapidaClientes.prototype.ativar = function() {
		this.clientePesquisado.on('click', onClienteSelecionado.bind(this));
	}
	
	function onClienteSelecionado(evento) {
		this.modalCliente.modal('hide');
		
		var clienteSelecionado = $(evento.currentTarget);		
		$('#nomeCliente').val(clienteSelecionado.data('nome'));
		$('#codigoCliente').val(clienteSelecionado.data('codigo'));
	}
	
	return TabelaPesquisaRapidaClientes;
}());


$(function() {
	var pesquisaRapidaClientes = new Salvavidas.PesquisaRapidaClientes();
	pesquisaRapidaClientes.iniciar();
});