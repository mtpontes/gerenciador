package br.com.gerenciador.integration.database;

import br.com.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.dto.empresa.request.NovaEmpresaDTO;
import br.com.gerenciador.modelo.Empresa;
import br.com.gerenciador.modelo.Usuario;

public class EmpresaBuilder {
	private String nome;
	private String data;
	private Usuario usuario;
	
	public EmpresaBuilder setNome(String login) {
		this.nome= login;
		return this;
	}
	public EmpresaBuilder setData(String login) {
		this.data = login;
		return this;
	}
	public EmpresaBuilder setUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}
	
	public Empresa build() {
		return new Empresa(new NovaEmpresaDTO(new EmpresaBaseDTO(this.nome, this.data), usuario));
	}
}
