var Salvavidas = Salvavidas || {};

Salvavidas.GraficoLancamentoPorCategoria = (function() {
	
	class GraficoLancamentoPorCategoria {
		constructor() {
			this.ctx = $('#graficoLancamentosPorCategoria')[0].getContext('2d');
		}
		
		iniciar() {
			$.ajax({
				url: '/lancamentos/por-categoria',
				method: 'GET',
				success: onDadosRecebidos.bind(this)
			})
		}
	}
	
	function onDadosRecebidos(porCategoria) {
		var categoriaNome = [];
		var valorTotalPorCategoria = [];
		
		porCategoria.forEach(function(objeto) {
			categoriaNome.unshift(objeto.categoria.nome);
			valorTotalPorCategoria.unshift(objeto.total);
		})
		
		var graficoLancamentosPorCategoria = new Chart(this.ctx, {
			type: 'pie',
			data: {
				labels: categoriaNome,
				datasets: [{
					backgroundColor: [						
						"#1e7145", '#72D3F7', '#bc8f8f', '#808000', 
						'#808080', '#ffb6c1', '#483d8b', '#8fbc8f',
						'#cd853f', '#4682b4', '#4b0082', '#ffff00', '#eee8aa',
						"#b91d47", "#00aba9", "#2b5797", "#e8c3b9"
					],
					borderColor: '#fff',
					data: valorTotalPorCategoria
				}] 
			}
		});		
	}		
	return GraficoLancamentoPorCategoria;	
}());

Salvavidas.GraficoLancamentoPorDia = (function() {
	
	class GraficoLancamentoPorDia {
		constructor() {
			this.ctx = $('#graficoLancamentosPorDia')[0].getContext('2d');
		}
		
		iniciar() {
			$.ajax({
				url: '/lancamentos/por-dia',
				method: 'GET',
				success: onDadosRecebidos.bind(this)
			})			
		}				
	}
	
	function onDadosRecebidos(porDia) {
		var diasDoMes = configurarDiasMes();		
		
		var totaisReceitas = totaisPorCadaDiaMes(porDia.filter(
          	dado => dado.tipo == 'RECEITA' ), diasDoMes);

        var totaisDespesas = totaisPorCadaDiaMes(porDia.filter(
          	dado => dado.tipo == 'DESPESA' ), diasDoMes);
		
		var graficoLancamentosPorDia = new Chart(this.ctx, {
			type: 'line',
			data: {
				labels: diasDoMes,
				datasets: [
					{
						label: 'Receitas',
						borderColor: '#1E90FF',
						backgroundColor: "rgba(135,206,235,0.5)",
						pointBorderColor: "rgba(135,206,235,1)",
						pointBackgroundColor: '#191970',
						data: totaisReceitas
					},
				    {
						label: 'Despesas',
						borderColor: '#DC143C',
						backgroundColor: "rgba(255,99,71,0.5)",
						pointBorderColor: "rgba(255,99,71,1)",
						pointBackgroundColor: '#8B0000',
						data: totaisDespesas
					}
				] 
			}
		});
	}
	
	function configurarDiasMes() {
	    var mesReferencia = new Date();
	    mesReferencia.setMonth(mesReferencia.getMonth() + 1);
	    mesReferencia.setDate(0);
	
	    var quantidade = mesReferencia.getDate();	
	    var dias = [];
	
	    for(let i = 1; i <= quantidade; i++) {
	    	dias.push(i);
	    }
	    return dias;
	}
	
	function totaisPorCadaDiaMes(dados, diasDoMes) {
		var totais = [];

		for (var dia of diasDoMes) {
			let total = 0;

			for (var dado of dados) {
				if (dado.dia[2] == dia) {
					total = dado.total;
					break;
				}
			}
			totais.push(total);
		}
		return totais;
	}	
	return GraficoLancamentoPorDia;	
}());

$(function() {
	var graficoLancamentosPorCategoria = new Salvavidas.GraficoLancamentoPorCategoria();
	graficoLancamentosPorCategoria.iniciar();
	
	var graficoLancamentosPorDia = new Salvavidas.GraficoLancamentoPorDia();
	graficoLancamentosPorDia.iniciar();
})