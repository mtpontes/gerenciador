package br.com.alura.gerenciador.integration.database;

import br.com.alura.gerenciador.modelo.Usuario;
import br.com.alura.gerenciador.dto.usuario.NovoUsuarioDTO;

public class UsuarioBuilder {

	private String login;
	private String senha;
	private String nome;
	
	public UsuarioBuilder setLogin(String login) {
		this.login = login;
		return this;
	}
	public UsuarioBuilder setSenha(String login) {
		this.login = login;
		return this;
	}
	public UsuarioBuilder setNome(String login) {
		this.login = login;
		return this;
	}
	
	public Usuario build() {
		return new Usuario(new NovoUsuarioDTO(this.nome, this.login, this.senha, this.senha));
	}
}
