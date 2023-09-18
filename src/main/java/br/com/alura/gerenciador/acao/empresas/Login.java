package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.RepositoryUsuario;

@NoArgsConstructor
public class Login extends AcaoComEntityManager{

	private RepositoryUsuario service = new RepositoryUsuario(this.em);
	
	public Login(EntityManager em) {
		super(em);
	}

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Processando login...");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		System.out.println("Logando " + login);
		
		Usuario usuario = service.findByLogin(login);
		
		if(usuario != null && usuario.verificarSenha(senha)) {
			System.out.println("Usuario existe");
			HttpSession sessao = request.getSession();
			sessao.setAttribute("usuarioLogado", usuario);
			return "redirect:entrada?acao=ListaEmpresas";
		} else {
			return "redirect:entrada?acao=LoginForm";
		}
	}
}
