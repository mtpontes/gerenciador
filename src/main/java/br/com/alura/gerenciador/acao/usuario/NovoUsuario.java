package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;

public class NovoUsuario implements AcaoComEntityManager {
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response, EntityManager em) throws ServletException, IOException {
		System.out.println("CADASTRANDO NOVO USUARIO");
		UsuarioRepository repository = new UsuarioRepository(em);
		
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario usuario = new Usuario();
		usuario.setLogin(login);
		usuario.setSenha(senha);
		
		repository.persist(usuario);
		System.out.println("USUARIOS CADASTRADO");
				
		request.setAttribute("usuario", usuario.getLogin());
		return "redirect:entrada?acao=LoginForm";
		
	}
}
