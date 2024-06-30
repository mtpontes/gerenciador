package br.com.gerenciador.modelo.dto.empresa;

import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.validation.annotation.DataNaoFutura;
import br.com.gerenciador.validation.patterns.ValidationPatternsEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpresaBaseDTO(
		
	@NotBlank(message = "Nome não pode ser nulo")
	@Pattern(regexp = ValidationPatternsEmpresa.NOME_EMPRESA_REGEX_PATTERN, message = ValidationPatternsEmpresa.NOME_EMPRESA_ERROR_MESSAGE)
	String nome,
	
	@NotBlank(message = "Data não pode ser nulo")
	@Pattern(regexp = ValidationPatternsEmpresa.DATA_EMPRESA_REGEX_PATTERN, message = ValidationPatternsEmpresa.DATA_EMPRESA_ERROR_FORMAT_MESSAGE)
	@DataNaoFutura
	String data
	) {
	
	
	public EmpresaBaseDTO(Empresa empresa) {
		this(empresa.getNome(), empresa.getDataAbertura().toString());
	}
	
	public String getNome() {
		return this.nome;
	}
	public String getData() {
		return this.data;
	}
}