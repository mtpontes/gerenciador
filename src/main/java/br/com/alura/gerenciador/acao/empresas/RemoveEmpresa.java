package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;

public class RemoveEmpresa extends AcaoComEntityManager{

	private EmpresaRepository repository = new EmpresaRepository(getEntityManager());
	
	public RemoveEmpresa(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("acao removendo empresa");
		
		String paramId = request.getParameter("id");
		Long id = Long.valueOf(paramId);
		
		System.out.println(id);
		
		Empresa empresa = repository.getEmpresaById(id);
		empresa.setAtivo(false);
		repository.persist(empresa);
		
		return "redirect:entrada?acao=ListaEmpresas";
	
	}
}
