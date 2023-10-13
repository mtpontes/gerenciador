package main.java.br.com.alura.gerenciador.acao.empresa;

import java.io.IOException;

import javax.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.acao.AcaoComEntityManager;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;

@NoArgsConstructor
public class RemoveEmpresa extends AcaoComEntityManager{

	private EmpresaRepositoryMySQL repository = new EmpresaRepositoryMySQL(this.em);
	public RemoveEmpresa(EntityManager em) {
		super(em);
	}
	
	@Override
	public String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("acao removendo empresa");
		
		Long id = getParameterId(request);
		Empresa empresa = repository.findEmpresaById(id).removeOrRestoreEmpresa();
		
		repository.update(empresa);
		return "redirect:entrada?acao=ListaEmpresasUsuario";
	}
}
