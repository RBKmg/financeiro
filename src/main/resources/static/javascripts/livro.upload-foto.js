var Salvavidas = Salvavidas || {};

Salvavidas.UploadFoto = (function() {
	class UploadFoto {
		constructor() {
			this.inputNomeFoto = $('input[name=foto]');
			this.inputContentType = $('input[name=contentType]');
			
			this.htmlFotoLivroTemplate = $('#foto-livro').html();
			this.template = Handlebars.compile(this.htmlFotoLivroTemplate);			
			
			this.upLoadDrop = $('#upload-drop');
			this.containerFotoLivro = $('.js-container-foto-livro');
			
			this.fotoNova = $('input[name=novaFoto]');
		}
		
		ativar() {
			var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.jpg|jpeg|png',
				action: this.containerFotoLivro.data('url-fotos'),
				complete: onUploadCompleto.bind(this),
				beforeSend: adicionarCsrfToken
		}
		
			UIkit.uploadSelect('#upload-select', settings);
			UIkit.uploadDrop(this.upLoadDrop, settings);
			
			if(this.inputNomeFoto.val()) {
				renderizarFotoNova.call(this, { nome: this.inputNomeFoto.val(), contentType: this.inputContentType.val() });
			}
		}
	}
	
	function onUploadCompleto(resposta) {
		this.fotoNova.val('true');
		renderizarFotoNova.call(this, resposta);
	}
	
	function renderizarFotoNova(resposta) {
		this.inputNomeFoto.val(resposta.nome);
		this.inputContentType.val(resposta.contentType);
		
		this.upLoadDrop.addClass('hidden');
		
		var foto = '';
		if(this.fotoNova.val() == 'true') {
			foto = 'temp/';
		}
		foto += resposta.nome;
		
		var htmlFotoLivro = this.template({ foto: foto })
		this.containerFotoLivro.append(htmlFotoLivro);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
	
	function onRemoverFoto() {
		$('.js-foto-livro-handlebars').remove();
		this.upLoadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
		this.novaFoto.val('false');
	}
	
	function adicionarCsrfToken(xhr) {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		
		xhr.setRequestHeader(header, token);
	}
	
	return UploadFoto;
}());

$(function() {
	var uploadFoto = new Salvavidas.UploadFoto();
	uploadFoto.ativar();
});