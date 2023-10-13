package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;

@NoArgsConstructor
public class NovaEmpresa extends AcaoComEntityManager{

	private EmpresaRepositoryMySQL repository = new EmpresaRepositoryMySQL(this.em);
	
	public NovaEmpresa(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("mostrando dados da empresa");
		
		String nome = request.getParameter("nome");
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		LocalDate date = getParameterData(request);
		
		Empresa empresa = new Empresa(nome, date, usuario);
		repository.persist(empresa);
		System.out.println("Empresa cadastrada!");
		
		request.setAttribute("empresa", empresa.getNome());
		return "redirect:entrada?acao=ListaEmpresas";
	}
}
