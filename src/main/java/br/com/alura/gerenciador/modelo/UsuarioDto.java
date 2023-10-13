package main.java.br.com.alura.gerenciador.modelo;

public record UsuarioDto(Long id, String nome) {

	public UsuarioDto(Usuario user) {
		this(user.getId(), user.getNome());
	}

}
