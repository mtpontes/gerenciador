package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaService;

@NoArgsConstructor
public class ListaEmpresas extends AcaoComEntityManager{

	private EmpresaService service = new EmpresaService(this.em);
	
	public ListaEmpresas(EntityManager em) {
			super(em);
	}
		
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("listando empresas");
		
		List<Empresa> lista = service.findEmpresas();
		request.setAttribute("empresas", lista);
		
		return "forward:listaEmpresas.jsp";
	}
}
