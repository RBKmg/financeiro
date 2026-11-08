var Salvavidas = Salvavidas || {};

Salvavidas.DialogoExcluir = (function() {
	class DialogoExcluir {
		constructor() {
			this.excluirBtn = $('.js-excluir-btn');
		}
		
		ativar() {
			this.excluirBtn.on('click', onExcluirClicado.bind(this));
			
			if(window.location.search.indexOf('excluido') > -1) {
				swal('Pronto!', 'Excluído com sucesso!', 'success');
			}
			
			function onExcluirClicado(evento) {
				evento.preventDefault();
				var botaoClicado = $(evento.currentTarget);
				var url = botaoClicado.data('url');
				var objeto = botaoClicado.data('objeto');
				
				swal({
					title: 'Tem certeza?',
					text: 'Excluir "' + objeto + '"? Você não poderá recuperar depois.',
					showCancelButton: true, 
					confirmButtonColor: '#DD6B55',
					confirmButtonText: 'Sim, exclua agora!',
					closeOnConfirm: false
				}, onExcluirConfirmado.bind(this, url));
			}
			
			function onExcluirConfirmado(url) {
				var token = $('meta[name="_csrf"]').attr('content');
				var header = $('meta[name="_csrf_header"]').attr('content');
				
				$.ajax({
					url: url,
					method: 'DELETE',
					beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
					success: onExcluidoSucesso.bind(this),
					error: onErrorExcluir.bind(this)
				})
			}
			
			function onExcluidoSucesso() {
				var urlAtual = window.location.href;
				var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
				var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
				
				window.location = novaUrl;
			}
			
			function onErrorExcluir(message) {
				swal('Oops!', message.responseText, 'error');
			}
		}		
	}	
	
	return DialogoExcluir;
}());


$(function() {
	var dialogoExcluir = new Salvavidas.DialogoExcluir();
	dialogoExcluir.ativar();
});