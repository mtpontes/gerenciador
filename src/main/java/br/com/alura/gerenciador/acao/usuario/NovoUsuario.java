package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;
import main.java.br.com.alura.gerenciador.repository.UsuarioService;

@NoArgsConstructor
public class NovoUsuario extends AcaoComEntityManager{

	private UsuarioService service = new UsuarioService(this.em);
	
	public NovoUsuario(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CADASTRANDO NOVO USUARIO");
		
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		System.out.println("O valor de senha no request é: " + senha);
		
		Usuario usuario = new Usuario(login, senha);
		
		service.persist(usuario);
		System.out.println("USUARIOS CADASTRADO");
				
		request.setAttribute("usuario", usuario.getLogin());
		return "redirect:entrada?acao=LoginForm";
	}
}
