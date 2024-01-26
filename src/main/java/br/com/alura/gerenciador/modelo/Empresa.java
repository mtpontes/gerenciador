package br.com.alura.gerenciador.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.alura.gerenciador.dto.empresa.request.NovaEmpresaDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private LocalDate dataAbertura;
	private Boolean ativo = true;
	@ManyToOne @JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Empresa(NovaEmpresaDTO dto) {
		this.nome = dto.base().nome();
		this.dataAbertura = LocalDate.parse(dto.base().data(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.usuario = dto.usuario(); 
	}
	
	public Empresa removeOrRestoreEmpresa() {
		if (this.ativo == true) {
			this.ativo = false;
			} else {
				this.ativo = true;				
			}
		return this;
	}
	
	public Empresa alteraDados(String nome, LocalDate dataAbertura) {
		this.nome = nome;
		this.dataAbertura = dataAbertura;
		return this;
	}
}
