package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;

@NoArgsConstructor
public class NovoUsuario extends AcaoComEntityManager{

	private UsuarioRepositoryMySQL repository = new UsuarioRepositoryMySQL(this.em);
	
	public NovoUsuario(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CADASTRANDO NOVO USUARIO");
		
		Usuario usuario = new Usuario(request.getParameter("login"), request.getParameter("senha"));
		repository.persist(usuario);
		System.out.println("USUARIO CADASTRADO");

		request.setAttribute("usuario", usuario.getLogin());
		return "redirect:entrada?acao=LoginForm";
		
	}
}
