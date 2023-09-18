package main.java.br.com.alura.gerenciador.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	public Empresa(String nome, Date datAbertura) {
		this.nome = nome;
		this.dataAbertura = dataAbertura;
	}
	
	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	
	public Date getDataAbertura() {
		return dataAbertura;
	}
}
