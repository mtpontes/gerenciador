package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		System.out.println("Alterando empresa...");
		
		Long empresaID = getParameterId(request);
		String nomeEmpresa = request.getParameter("nome");
		LocalDate dataEmpresa= getParameterData(request);
		
		Empresa empresa = repository.findEmpresaById(empresaID).alteraDados(nomeEmpresa, dataEmpresa);
		repository.update(empresa);
		return "redirect:entrada?acao=ListaEmpresasUsuario";
	}
}
