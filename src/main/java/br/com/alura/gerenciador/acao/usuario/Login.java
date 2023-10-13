package main.java.br.com.alura.gerenciador.acao.usuario;

import java.io.IOException;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.modelo.UsuarioDto;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;

@NoArgsConstructor
public class Login extends AcaoComEntityManager{

	private UsuarioRepositoryMySQL repository = new UsuarioRepositoryMySQL(this.em);
	
	public Login(EntityManager em) {
		super(em);
	}

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Processando login...");
		
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		System.out.println("Logando " + login);
		
		Usuario usuario =  repository.findByLogin(login);
		if(usuario != null && usuario.verificarSenha(senha)) {
			System.out.println("Usuario existe");
			
			HttpSession sessao = request.getSession();
			sessao.setAttribute("usuarioLogado", usuario);
			sessao.setMaxInactiveInterval(3600);
			
			System.out.println("Usuario autenticado!");
			return "foward:entrada?acao=ListaEmpresasUsuario";
		} else {
			return "redirect:usuario?acao=LoginForm";
		}
	}

}
