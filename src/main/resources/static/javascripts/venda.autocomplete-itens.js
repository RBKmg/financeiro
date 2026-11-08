Salvavidas = Salvavidas || {};

Salvavidas.Autocomplete = (function(){
	
	function Autocomplete() {
		this.isbnOuTituloInput = $('.js-isbn-ou-titulo-input');
		
		var htmlTemplateAutocomplete = $('#template-autocomplete-livro').html();
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
		
		this.emitterEvent = $({});
		this.on = this.emitterEvent.on.bind(this.emitterEvent);
	}
	
	Autocomplete.prototype.iniciar = function() {		
		var options = {
			url: function(isbnOuTitulo) {
				return this.isbnOuTituloInput.data('url') + '?isbnOuTitulo=' + isbnOuTitulo;
				/*return '/salvavidas/livros/filtro?isbnOuTitulo=' + isbnOuTitulo;*/
			}.bind(this),
			getValue: 'titulo',
			minCharNumber: 5,
			requestDelay: 500,
			ajaxSettings: {
				contentType: 'application/json'
			},
			
			template: {
				type: 'custom',
				method: template.bind(this)
			},
			list: {				
				onChooseEvent: onItemSelecionado.bind(this)				
			}			
		};
		
		this.isbnOuTituloInput.easyAutocomplete(options);
	}
	
	
	function onItemSelecionado() {
		this.emitterEvent.trigger('item-selecionado', this.isbnOuTituloInput.getSelectedItemData());
		this.isbnOuTituloInput.val('');
		this.isbnOuTituloInput.focus();
	}
	
	function template(titulo, livro) {
		livro.valorFormatado = Salvavidas.formatarMoedaOutput(livro.valor);
		return this.template(livro)
	}
	
	return Autocomplete;
	
}());

