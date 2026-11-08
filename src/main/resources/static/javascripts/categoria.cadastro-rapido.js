$(function() {
	
	var modal = $('#modalCadastroRapidoCategoria');
	var botaoSalvar = $('.js-modal-botao-salvar-cadastro-categoria');
	
	/* Evita submeter o formulario ao apertar enter */
	var form = modal.find('form');
	form.on('submit', function(event) { event.preventDefault() });
	
	/* Pega URL para fazer o POST */
	var url = form.attr('action');
	
	
	/**
	 * onModalShow -> Ao abrir modal 
	 * onModalClose -> Ao fechar modal
	 */
	/* Focus no campo input ao acionar o modal */
	var inputNomeCategoria = $('#nomeCategoria');
	modal.on('shown.bs.modal', onModalShow);
	function onModalShow() {
		inputNomeCategoria.focus();
	}
	
	/**
	 * Limpa o input nome caso tenha cancelado e digitado algo
	 * Caso tenha caido em alguma exceção 
	 * Limpa a mensagem de erro 
	 * e remove a cor vermelha do formulario
	 */
	modal.on('hide.bs.modal', onModalClose);
	function onModalClose() {
		inputNomeCategoria.val('');
		containerMensagemDeErro.addClass('hidden');
		form.find('.form-group').removeClass('has-error');
	}
	
	
	/**
	 * Salvar uma categoria
	 * Utilizando a mesma variavel do input (onModalShow, onModalClose)
	 * Utiliza a função AJAX do JQuery para submeter ao servidor,
	 * URL salvavidas/categorias metodo em CategoriasController.class
	 */
	botaoSalvar.on('click', onBotaoSalvarClick);
	function onBotaoSalvarClick() {
		var nomeCategoria = inputNomeCategoria.val().trim();
		var csrfToken = form.find('input[name="_csrf"]').val();
		$.ajax({
			url: url,
			method: 'POST',
			contentType: 'application/json',
			headers: {'X-CSRF-TOKEN': csrfToken},
			data: JSON.stringify({ nome: nomeCategoria }),
			error: onErroAoSalvarCategoria,
			success: onSucessoAoSalvarCategoria
		})
	}
	
	var containerMensagemDeErro = $('.js-mensagem-cadastro-rapido-categoria');
	function onErroAoSalvarCategoria(objeto) {
		var mensagemDeErro = objeto.responseText;
		containerMensagemDeErro.removeClass('hidden');
		containerMensagemDeErro.html('<span>' + mensagemDeErro + '</span>');
		form.find('.form-group').addClass('has-error');
	}
	
	function onSucessoAoSalvarCategoria(categoria) {
		/* Coloca a categoria pronta para ser selecionada -> JPA metodo saveAndFlush */
		var comboCategoria = $('#categoria');
		comboCategoria.append('<option value=' + categoria.codigo + '>' + categoria.nome + '</option>');
		comboCategoria.val(categoria.codigo);
		modal.modal('hide');
	}
	
		
});