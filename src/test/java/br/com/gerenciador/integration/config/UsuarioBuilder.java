package br.com.gerenciador.integration.config;

import br.com.gerenciador.modelo.Usuario;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;

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