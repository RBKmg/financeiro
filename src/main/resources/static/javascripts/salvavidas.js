var Salvavidas = Salvavidas || {};

Salvavidas.MaskMoney = (function() {
	class MaskMoney {
		constructor() {
			this.decimal = $('.js-decimal');
			this.plain = $('.js-plain');
		}
		
		ativar() {
			this.decimal.maskMoney({ decimal: ',', thousands: '.' });
			this.plain.maskMoney({ precision: 0, thousands: '.' });
		}	
	}	
	
	return MaskMoney;
}());

Salvavidas.MaskPhoneNumber = (function() {	
	class MaskPhoneNumber {
		constructor() {
			this.inputPhoneNumber = $('.js-phone-number');
		}
		
		enable() {
			var maskBehavior = function(val) {
				  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
			};
			var options = {
			  onKeyPress: function(field, options) {
			      field.mask(maskBehavior.apply({}, arguments), options);
			    }
			};
			this.inputPhoneNumber.mask(maskBehavior, options);
		}		
	}	
	
	return MaskPhoneNumber;	
})();

Salvavidas.MaskCep = (function() {	
	class MaskCep {
		constructor() {
			this.inputCep = $('.js-cep');	
		}
		
		ativar() {
			var options =  {
					onKeyPress: function(cep, options) {
						var masks = ['00000-000', '0-00-00-00'];
					    var mask = (cep.length>7) ? masks[1] : masks[0];
					    $('.crazy_cep').mask(mask, options);
			}};
			this.inputCep.mask('00000-000', options);
		}	
	}	
	
	return MaskCep;
}());

Salvavidas.MaskDate = (function() {	
	class MaskDate {
		constructor() {
			this.inputDate = $('.js-date');
		}
		
		ativar() {
			this.inputDate.mask('00/00/0000');
			this.inputDate.datepicker({
				orientation: 'bottom',
				language: 'pt-BR',
				autoclose: true  
			})
		}
	}
	
	return MaskDate;
}());


Salvavidas.Security = (function() {
	class Security {
		constructor() {
			this.token = $('input[name=_csrf]').val();
			this.header = $('input[name=_csrf_header]').val();
		}
		
		ativar() {
			$(document).ajaxSend(function(event, jqxhr, settings) {
				jqxhr.setRequestHeader(this.header, this.token);
			}.bind(this));
		}
	}
	
	return Security;
}());  

/*Salvavidas.Security = (function() {
	class Security {
		constructor() {
			this.token = $('input[name="_csrf"]').val();
			this.header = $('input[name="_csrf_header"]').val();
		}
		
		ativar() {
			$(document).ajaxSend(function(event, jqxhr, settings) {
				
				if (typeof this.header === 'string' &&
					this.header.length > 0 &&
					typeof this.token === 'string' &&
					this.token.length > 0) {
					
					jqxhr.setRequestHeader(this.header, this.token);
				}
			}.bind(this));
		}
	}
	
	return Security;
})();*/

Salvavidas.MaskHorario = (function() {	
	class MaskHorario {
		constructor() {
			this.inputHorario = $('.js-horario');
		}
		
		ativar() {
			this.inputHorario.mask('00:00');
		}
	}
	
	return MaskHorario;
}());


numeral.language('pt-br');

Salvavidas.formatarMoedaOutput = function(valor) {
	return numeral(valor).format('0,0.00');
}

Salvavidas.recuperarValor = function(valorFormatado) {
	return numeral().unformat(valorFormatado);
}

$(function() {
	var maskMoney = new Salvavidas.MaskMoney();
	maskMoney.ativar();
	
	var maskPhoneNumber = new Salvavidas.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	var maskCep = new Salvavidas.MaskCep();
	maskCep.ativar();
	
	var maskDate = new Salvavidas.MaskDate();
	maskDate.ativar();
	/*
	var security = new Salvavidas.Security();
	security.ativar();  */
	
	var horario = new Salvavidas.MaskHorario();
	horario.ativar();
});
