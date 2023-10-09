package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;

public class NovoUsuarioForm implements AcaoSemEntityManager {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "forward:formNovoUsuario.jsp";
	}
}
