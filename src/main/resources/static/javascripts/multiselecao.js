Salvavidas = Salvavidas || {};

Salvavidas.MultiSelecao = (function() {
	function MultiSelecao() {		
		this.statusBtn = $('.js-status-btn');
		this.usuarioSelecionadoCheckBox = $('.js-usuario-selecionado-checkbox');
		this.todosUsuariosSelecionadosCheckbox = $('.js-todos-usuarios-selecionados-checkbox');		
	}
		
	MultiSelecao.prototype.ativar = function() {
		this.statusBtn.on('click', onAlterarStatusClicado.bind(this));
		this.todosUsuariosSelecionadosCheckbox.on('click', onTodosUsuariosSelecionadosCheckboxClicado.bind(this));
		this.usuarioSelecionadoCheckBox.on('click', onUsuarioSelecionadoClicado.bind(this));
	}		
		
	function onAlterarStatusClicado(event) {
		var botaoClicado = $(event.currentTarget);
		var status = botaoClicado.data('status');
		var url = botaoClicado.data('url');
		var apenasUsuariosSelecionados = this.usuarioSelecionadoCheckBox.filter(':checked');
		
		var codigosDosUsuarios = $.map(apenasUsuariosSelecionados, function(c) {
			return $(c).data('codigo');
		});
		
		if(codigosDosUsuarios.length > 0) {
			$.ajax({
				url: url,
				method: 'PUT',
				data: {
					codigos: codigosDosUsuarios,
					status: status
				},
				success: function() {
					window.location.reload();
				}
			})	
		}			
	}
	
	function onTodosUsuariosSelecionadosCheckboxClicado() {
		var status = this.todosUsuariosSelecionadosCheckbox.prop('checked');
		this.usuarioSelecionadoCheckBox.prop('checked', status);
		statusBotaoAtivarDesativar.call(this, status);
	}
	
	function onUsuarioSelecionadoClicado() {
		var quantidadeDeCheckboxChecados = this.usuarioSelecionadoCheckBox.filter(':checked');
		this.todosUsuariosSelecionadosCheckbox.prop('checked', quantidadeDeCheckboxChecados.length >= this.usuarioSelecionadoCheckBox.length);
		statusBotaoAtivarDesativar.call(this, this.usuarioSelecionadoCheckBox.length);
	}
	
	function statusBotaoAtivarDesativar(ativar) {
		ativar ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	return MultiSelecao;
}());

$(function() {
	var multiSelecao = new Salvavidas.MultiSelecao();
	multiSelecao.ativar();
})