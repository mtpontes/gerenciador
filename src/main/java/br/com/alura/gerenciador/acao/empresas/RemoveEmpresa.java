package main.java.br.com.alura.gerenciador.acao.empresas;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaService;

@NoArgsConstructor
public class RemoveEmpresa extends AcaoComEntityManager{

	private EmpresaService service = new EmpresaService(this.em);
	
	public RemoveEmpresa(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("acao removendo empresa");
		
		String paramId = request.getParameter("id");
		Long id = Long.valueOf(paramId);
		
		System.out.println(id);
		
		Empresa empresa = service.findEmpresaById(id);
		empresa.setAtivo(false);
		service.persist(empresa);
		
		return "redirect:entrada?acao=ListaEmpresas";
	
	}
}
