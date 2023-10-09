package main.java.br.com.alura.gerenciador.modelo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Setter
	private String nome;
	@Setter
	private LocalDate dataAbertura;
	private Boolean ativo = true;
	@ManyToOne @JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Empresa(String nome, LocalDate dataAbertura, Usuario usuario) {
		this.nome = nome;
		this.dataAbertura = dataAbertura;
		this.usuario = usuario;
	}
	
	public void removeOrRestoreEmpresa() {
		if (this.ativo == true) {
			this.ativo = false;
			} else {
				this.ativo = true;				
			}
	}
	
	public Empresa alteraDados(String nome, LocalDate dataAbertura) {
		this.nome = nome;
		this.dataAbertura = dataAbertura;
		return this;
	}
}
