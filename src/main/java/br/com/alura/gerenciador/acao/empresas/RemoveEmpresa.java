package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.Acao;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;

public class RemoveEmpresa implements Acao {

	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response, EntityManager em) throws ServletException, IOException {
		EmpresaRepository repository = new EmpresaRepository(em);
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
