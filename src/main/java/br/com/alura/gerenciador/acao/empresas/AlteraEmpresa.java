package main.java.br.com.alura.gerenciador.acao.empresas;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaService;


@NoArgsConstructor
public class AlteraEmpresa extends AcaoComEntityManager {

	private EmpresaService service = new EmpresaService(this.em);
	
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
		
		
		Empresa empresa = service.findEmpresaById(id);
		empresa.setId(id);
		empresa.setNome(nomeEmpresa);
		empresa.setDataAbertura(dataAbertura);
		service.persist(empresa);
		
		return "redirect:entrada?acao=ListaEmpresas";
	
	}
}

