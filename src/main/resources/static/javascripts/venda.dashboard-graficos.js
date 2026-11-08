var Salvavidas = Salvavidas || {};

Salvavidas.GraficoVendaPorMes = (function() {
	
	class GraficoVendaPorMes {
		constructor() {
			this.ctx = $('#graficoVendaPorMes')[0].getContext('2d');
		}
		
		iniciar() {
			$.ajax({
				url: '/vendas/totalPorMes',
				method: 'GET',
				success: onDadosRecebidos.bind(this)
			})
		}
	}
	
	function onDadosRecebidos(vendaMes) {
		var meses = [];
		var valores = [];
		
		vendaMes.forEach(function(objeto) {
			meses.unshift(objeto.mes);
			valores.unshift(objeto.total);
		});
		
		var graficoDeVendasPorMes = new Chart(this.ctx, {
			type: 'line',
			data: {
				labels: meses,
				datasets: [{
					label: 'Vendas por mês',
					backgroundColor: 'rgba(26,179,148,0.5)',
					pointBorderColor: "rgba(26,179,148,1)",
					pointBackgroundColor: "#fff",
					data: valores
				}] 
			}
		});
	}	
	
	return GraficoVendaPorMes;
	
}());

Salvavidas.GraficoVendaPorOrigem = (function() {
	
	class GraficoVendaPorOrigem {
		constructor() {
			this.ctx = $('#graficoVendaPorOrigem')[0].getContext('2d');
		}
		
		iniciar() {
			$.ajax({
				url: '/vendas/porOrigem',
				method: 'GET', 
				success: onDadosRecebidos.bind(this)
			});
		}	
	}
	
	function onDadosRecebidos(vendaOrigem) {
		var meses = [];
		var livrosNacionais = [];
		var livrosInternacionais = [];
		
		vendaOrigem.forEach(function(obj) {
			meses.unshift(obj.mes);
			livrosNacionais.unshift(obj.totalNacional);
			livrosInternacionais.unshift(obj.totalInternacional)
		});
		
		var graficoVendasPorOrigem = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Nacional',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: livrosNacionais
		    	},
		    	{
		    		label: 'Internacional',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: livrosInternacionais
		    	}]
		    },
		});
	}
	
	return GraficoVendaPorOrigem;
	
}());

$(function() {
	var graficoVendaPorMes = new Salvavidas.GraficoVendaPorMes();
	graficoVendaPorMes.iniciar();
	
	var graficoVendaPorOrigem = new Salvavidas.GraficoVendaPorOrigem();
	graficoVendaPorOrigem.iniciar();
})