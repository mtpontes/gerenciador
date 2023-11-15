package main.java.br.com.alura.gerenciador.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.EntityManager;
import main.java.br.com.alura.gerenciador.dto.AlteraEmpresaDTO;
import main.java.br.com.alura.gerenciador.dto.NovaEmpresaDTO;
import main.java.br.com.alura.gerenciador.modelo.Empresa;
import main.java.br.com.alura.gerenciador.modelo.Usuario;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepository;
import main.java.br.com.alura.gerenciador.repository.EmpresaRepositoryMySQL;
import main.java.br.com.alura.gerenciador.validation.ValidatorUtil;

public class EmpresaService {

	private EmpresaRepository repository;
	
	public EmpresaService(EntityManager em) {
		this.repository = new EmpresaRepositoryMySQL(em);
	}
	
	public List<Empresa> consultaEmpresas() {
		return repository.findEmpresas();
	}
	public List<Empresa> consultaEmpresasUsuario(Long id) {
		return repository.findEmpresasByUsuarioId(id);
	}
	
	public void alteraDadosEmpresa(Long id, String nome, String data) {
		System.out.println("Service - alteraDadosEmpresa!");
		
		AlteraEmpresaDTO dto = new AlteraEmpresaDTO(nome, data);
		ValidatorUtil.valida(dto);
		
		Empresa empresa = repository.findEmpresaById(id);
		empresa.alteraDados(dto.nome(), formataData(dto.data()));
		repository.update(empresa);
	}
	
	public void cadastraEmpresa(String nome, String data, Usuario usuario) {
		System.out.println("Service - cadastraEmpresa!");
		
		NovaEmpresaDTO dto = new NovaEmpresaDTO(nome, data, usuario.getId());
		ValidatorUtil.valida(dto);
		
		repository.persist(new Empresa(dto.nome(), formataData(dto.data()), usuario));
	}
	
	//transforma String em LocalDate
	public LocalDate formataData(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
