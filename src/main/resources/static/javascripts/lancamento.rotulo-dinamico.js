var Salvavidas = Salvavidas || {};

Salvavidas.RotuloDinamico = (function() {
	class RotuloDinamico {
		constructor() {
			this.radioTipoLancamento = $('.js-radio-tipo-lancamento');
			this.rotuloLancamento = $('[for=recebimento]');
			this.inputTipoLancamento = $('#recebimento');
		}
		
		ativar() {
			this.radioTipoLancamento.on('change', onTipoLancamentoAlterado.bind(this));
			var tipoLancamentoSelecionado = this.radioTipoLancamento.filter(':checked')[0];
			if(tipoLancamentoSelecionado) {
				aplicarRotulo.call(this, $(tipoLancamentoSelecionado));
			}
		}		
	}
	
	function onTipoLancamentoAlterado(evento) {
		var tipoLancamentoSelecionado = $(evento.currentTarget);
		aplicarRotulo.call(this, tipoLancamentoSelecionado);
		this.inputTipoLancamento.val('');		
	}
	
	function aplicarRotulo(tipoLancamentoSelecionado) {
		this.rotuloLancamento.text(tipoLancamentoSelecionado.data('documento'));
		this.inputTipoLancamento.removeAttr('disabled');
	}
	
	return RotuloDinamico;
}());

$(function() {
	var rotuloDinamico = new Salvavidas.RotuloDinamico();
	rotuloDinamico.ativar();
});
