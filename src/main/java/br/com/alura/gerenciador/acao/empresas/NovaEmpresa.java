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

public class NovaEmpresa implements AcaoComEntityManager {
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response, EntityManager em) throws ServletException, IOException {
		System.out.println("Cadastrando nova empresa");
		EmpresaRepository repository = new EmpresaRepository(em);
		
		String nome = request.getParameter("nome");
		String dataAbertura = request.getParameter("data");
		
		Date dataFormatada = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataFormatada = sdf.parse(dataAbertura);
		} catch (ParseException e) {
			throw new ServletException(e);
		}
		
		Empresa empresa = new Empresa();
		empresa.setNome(nome);
		empresa.setDataAbertura(dataFormatada);
		repository.persist(empresa);
		
		request.setAttribute("empresa", empresa.getNome());
		return "redirect:entrada?acao=ListaEmpresas";
		
	}
}
