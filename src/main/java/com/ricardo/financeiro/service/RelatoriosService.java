package com.ricardo.financeiro.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatoriosService {

    private final DataSource dataSource;

    public RelatoriosService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] gerarLancamentos(Date dataInicio, Date dataFim) {
        try (Connection connection = dataSource.getConnection()) {
            InputStream relatorio = getClass().getResourceAsStream("/relatorios/relatorio_lancamentos.jasper");

            if (relatorio == null) {
                throw new IllegalStateException("Arquivo relatorio_lancamentos.jasper não encontrado");
            }

            Map<String, Object> parametros = new HashMap<>();

            parametros.put("REPORT_LOCALE", Locale.of("pt", "BR"));
            parametros.put("data_inicio", dataInicio);
            parametros.put("data_fim", dataFim);

            JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de lançamentos", e);
        }
    }
    
    public byte[] gerarVendasEmitidas(Date dataInicio, Date dataFim) {
        try (Connection connection = dataSource.getConnection()) {

            InputStream relatorio = getClass().getResourceAsStream("/relatorios/relatorio_vendas_emitidas.jasper");

            if (relatorio == null) {
                throw new IllegalStateException("Arquivo relatorio_vendas_emitidas.jasper não encontrado");
            }

            Map<String, Object> parametros = new HashMap<>();

            parametros.put("REPORT_LOCALE", Locale.of("pt", "BR"));
            parametros.put("data_inicio", dataInicio);
            parametros.put("data_fim", dataFim);

            JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, connection);

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de vendas emitidas", e);
        }
    }
}