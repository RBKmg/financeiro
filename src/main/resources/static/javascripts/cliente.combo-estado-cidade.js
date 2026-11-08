Salvavidas = Salvavidas || {};

Salvavidas.ComboEstado = (function() {
	
	function ComboEstado() {
		this.selectEstado = $('#estado');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	 
	ComboEstado.prototype.ativar = function() {
		this.selectEstado.on('change', onEstadoAlterado.bind(this));
	}
		
	function onEstadoAlterado() {
		/*console.log('estado selecionado ', this.selectEstado.val())*/;
		this.emitter.trigger('alterado', this.selectEstado.val());
	}			
	
	return ComboEstado;
}());


Salvavidas.ComboCidade = (function() {
	
	function ComboCidade(comboEstado) {
		this.comboEstado = comboEstado;
		this.selectCidade = $('#cidade');
		this.imgLoading = $('.js-img-loading');	
		this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
	}
	
	ComboCidade.prototype.iniciar = function() {
		reset.call(this);
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this));
		
		var codigoEstado = this.comboEstado.selectEstado.val();
		inicializarCidades.call(this, codigoEstado);
	}	
	
	function onEstadoAlterado(evento, codigoEstado) {
		/*console.log('codigo do estado no combo cidade ', codigoEstado);*/
		this.inputHiddenCidadeSelecionada.val('');
		inicializarCidades.call(this, codigoEstado);		 	
	}
	
	function inicializarCidades(codigoEstado) {
		if(codigoEstado) {
			var resposta = $.ajax({
				url: this.selectCidade.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: { 'estado': codigoEstado },
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onCarregarCidadesNoSelect.bind(this));			
		} else {
			reset.call(this);
		}	
	}
	
	function onCarregarCidadesNoSelect(cidades) {
		var options = [];
		cidades.forEach(function(cidade) {
			options.push('<option value="' + cidade.codigo + '">' + cidade.nome + '</option>');
		});
		
		this.selectCidade.html(options.join(''));
		this.selectCidade.removeAttr('disabled');
		
		/* Mantém a cidade selecionada após submissao do formulario */
		var codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
		if(codigoCidadeSelecionada) {
			this.selectCidade.val(codigoCidadeSelecionada);
		}
	}
	
	function reset() {
		this.selectCidade.html('<option value="">Selecione a cidade</option>');
		this.selectCidade.val('');
		this.selectCidade.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao() {
		reset.call(this)
		this.imgLoading.show();
	}
	
	function finalizarRequisicao() {
		this.imgLoading.hide();
	}	
	
	return ComboCidade;
}());



$(function() {
	var comboEstado = new Salvavidas.ComboEstado;
	comboEstado.ativar();
	
	var comboCidade = new Salvavidas.ComboCidade(comboEstado);
	comboCidade.iniciar();
});