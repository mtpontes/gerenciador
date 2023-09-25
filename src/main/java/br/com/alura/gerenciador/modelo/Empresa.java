package main.java.br.com.alura.gerenciador.modelo;

import java.util.Date;

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
@Setter
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private Date dataAbertura = new Date();
	private Boolean ativo = true;
	@ManyToOne @JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Empresa(String nome, Date dataAbertura, Usuario usuario) {
		this.nome = nome;
		this.dataAbertura = dataAbertura;
		this.usuario = usuario;
	}
	
	
	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	
	public Date getDataAbertura() {
		return dataAbertura;
	}
}
