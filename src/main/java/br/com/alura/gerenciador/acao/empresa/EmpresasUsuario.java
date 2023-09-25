package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;

@NoArgsConstructor
public class EmpresasUsuario extends AcaoComEntityManager {

	private EmpresaRepositoryMySQL repository = new EmpresaRepositoryMySQL(this.em);

	public EmpresasUsuario(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listando empresas do usuário");

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		//HttpSession sessao = request.getSession();
		
		System.out.println("O login e:" + usuario.getLogin() + " e o ID e: " + usuario.getId()); 
		List<Empresa> lista = repository.findEmpresasDoUsuario(usuario.getId());
		request.setAttribute("empresas", lista);
	
		return "forward:usuarioEmpresas.jsp";
	}
}