package br.com.alura.gerenciador.dto.empresa;

import java.time.format.DateTimeFormatter;

import br.com.alura.gerenciador.modelo.Empresa;
import br.com.alura.gerenciador.validation.annotation.DataNaoFutura;
import br.com.alura.gerenciador.validation.patterns.ValidationPatternsEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpresaBaseDTO(
		
		@NotBlank(message = "VALIDATION ERROR EMPRESA: Nome não pode ser nulo")
		@Pattern(regexp = ValidationPatternsEmpresa.NOME_EMPRESA_REGEX_PATTERN, message = ValidationPatternsEmpresa.NOME_EMPRESA_ERROR_MESSAGE)
		String nome,
		
		@NotBlank(message = "VALIDATION ERROR EMPRESA: Data não pode ser nulo")
		@Pattern(regexp = ValidationPatternsEmpresa.DATA_EMPRESA_REGEX_PATTERN, message = ValidationPatternsEmpresa.DATA_EMPRESA_ERROR_FORMAT_MESSAGE)
		@DataNaoFutura
		String data) {
	
	
	public EmpresaBaseDTO(Empresa empresa) {
		this(empresa.getNome(), empresa.getDataAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}
	
	public String getNome() {
		return this.nome;
	}
	public String getData() {
		return this.data;
	}
}
