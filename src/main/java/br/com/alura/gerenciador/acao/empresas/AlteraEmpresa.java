package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;

public class AlteraEmpresa extends AcaoComEntityManager {

	private EmpresaRepository repository = new EmpresaRepository(getEntityManager());
	
	public AlteraEmpresa(EntityManager em) {
		super(em);
	}
	
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nomeEmpresa = request.getParameter("nome");
		String paramDataEmpresa = request.getParameter("data");
		String paramId = request.getParameter("id");
		Long id = Long.valueOf(paramId);
		
		System.out.println("acao altera empresa " + id);
		
		Date dataAbertura = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataAbertura = sdf.parse(paramDataEmpresa);
		} catch (ParseException e) {
			throw new ServletException(e);
		}
		
		
		Empresa empresa = repository.getEmpresaById(id);
		empresa.setId(id);
		empresa.setNome(nomeEmpresa);
		empresa.setDataAbertura(dataAbertura);
		repository.persist(empresa);
		
		return "redirect:entrada?acao=ListaEmpresas";
	
	}
}

