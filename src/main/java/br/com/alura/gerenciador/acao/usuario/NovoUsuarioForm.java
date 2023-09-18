package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoSemEntityManager;

@NoArgsConstructor
public class NovoUsuarioForm implements AcaoSemEntityManager {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response	) throws ServletException, IOException {
		return "forward:formNovoUsuario.jsp";
	}
}
