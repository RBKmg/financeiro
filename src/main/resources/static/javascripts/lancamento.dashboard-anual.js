var Salvavidas = Salvavidas || {};

Salvavidas.GraficoLancamentoAnualPorTipo = (function() {
	
	class GraficoLancamentoAnualPorTipo {
		constructor() {
			this.ctx = $('#graficolancamentoAnualPorTipo')[0].getContext('2d');
		}
		
		iniciar() {
			$.ajax({
				url: '/anual/por-tipo',
				method: 'GET', 
				success: onDadosRecebidos.bind(this)
			});
		}	
	}
	
	function onDadosRecebidos(lancamentosPorTipo) {
		console.log("--", lancamentosPorTipo)
		var meses = [];
		var receitas = [];
		var despesas = [];
		
		lancamentosPorTipo.forEach(function(obj) {
			meses.unshift(obj.mes);
			receitas.unshift(obj.totalReceita);
			despesas.unshift(obj.totalDespesa)
		});
		
		var graficoLancamentoAnualPorTipo = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Receita',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: receitas
		    	},
		    	{
		    		label: 'Despesa',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: despesas
		    	}]
		    },
			options: {
				responsive: true,
				maintainAspectRatio: false
			}
		});
	}
	
	return GraficoLancamentoAnualPorTipo;
	
}());

$(function() {
/*	var graficoVendaPorMes = new Salvavidas.GraficoVendaPorMes();
	graficoVendaPorMes.iniciar();*/
	
	var graficoLancamentoAnualPorTipo = new Salvavidas.GraficoLancamentoAnualPorTipo();
	graficoLancamentoAnualPorTipo.iniciar();
})