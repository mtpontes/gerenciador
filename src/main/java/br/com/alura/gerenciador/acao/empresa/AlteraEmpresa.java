package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;

@NoArgsConstructor
public class AlteraEmpresa extends AcaoComEntityManager {

	private EmpresaRepositoryMySQL repository = new EmpresaRepositoryMySQL(this.em);
	
	public AlteraEmpresa(EntityManager em) {
		super(em);
	}
	
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nomeEmpresa = request.getParameter("nome");
		String paramId = request.getParameter("id");
		Long id = Long.valueOf(paramId);
		String paramDataEmpresa = request.getParameter("data");
		LocalDate date = LocalDate.parse(paramDataEmpresa, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		System.out.println("acao altera empresa " + id);
		
		Empresa empresa = repository.findEmpresaById(id).alteraDados(nomeEmpresa, date);
		repository.update(empresa);
		return "redirect:entrada?acao=ListaEmpresas";
	
	}
}

