var Salvavidas = Salvavidas || {};

Salvavidas.MascaraCpfCnpj = (function() {
	
	class MascaraCpfCnpj {
		constructor() {
			this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
			this.labelCpfCnpj = $('[for=cpfOuCnpj]')
			this.inputCpfCnpj = $('#cpfOuCnpj');
		}
		
		ativar() {
			this.radioTipoPessoa.on('change', onTipoPessoaAlterada.bind(this));
			var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
			if(tipoPessoaSelecionada) {
				aplicarMascara.call(this, $(tipoPessoaSelecionada));
			}
		}
	}	
	
	function onTipoPessoaAlterada(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		aplicarMascara.call(this, tipoPessoaSelecionada);
		this.inputCpfCnpj.val('');
	}
	
	function aplicarMascara(tipoPessoaSelecionada) {
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'));
		this.inputCpfCnpj.removeAttr('disabled');
	}
	
	return MascaraCpfCnpj;
}());

$(function() {
	var mascaraCpfCnpj = new Salvavidas.MascaraCpfCnpj();
	mascaraCpfCnpj.ativar();
})