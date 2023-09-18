package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;

public class NovaEmpresaForm implements AcaoSemEntityManager {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "forward:formNovaEmpresa.jsp";
	}
}
