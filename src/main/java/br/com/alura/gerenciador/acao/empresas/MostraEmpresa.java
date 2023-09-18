package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;

public class MostraEmpresa extends AcaoComEntityManager{

	private EmpresaRepository repository = new EmpresaRepository(getEntityManager());
	
	public MostraEmpresa(EntityManager em) {
		super(em);
	}

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("mostrando dados da empresa");
		
		String paramId = request.getParameter("id");
		Long id = Long.valueOf(paramId);
		
		Empresa empresa = repository.getEmpresaById(id);
		request.setAttribute("empresa", empresa);
		
		return "forward:formAlteraEmpresa.jsp";
	}
}
