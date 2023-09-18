package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.Acao;

public class NovoUsuarioForm implements Acao {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response, EntityManager em) throws ServletException, IOException {
		return "forward:formNovoUsuario.jsp";
	}
}
