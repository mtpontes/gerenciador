package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;

@NoArgsConstructor
public class ListaEmpresas extends AcaoComEntityManager{

	private EmpresaRepositoryMySQL repository = new EmpresaRepositoryMySQL(this.em);
	
	public ListaEmpresas(EntityManager em) {
		super(em);
	}
		
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listando empresas...");
		
		List<Empresa> listaEmpresas = repository.findEmpresas();
		request.setAttribute("empresas", listaEmpresas);
		
		return "forward:listaEmpresas.jsp";
	}
}
