package br.com.gerenciador.modelo;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gerenciador.modelo.dto.usuario.NovoUsuarioDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
	private Long id;
	@Getter
	@Column(unique = true)
	private String login;
	@Getter
	private String senha;
	@Getter
	private String nome;
	@OneToMany(mappedBy = "usuario")
	private List<Empresa> empresas = new ArrayList<>();
	
	public Usuario(NovoUsuarioDTO dto) {
		this.nome = dto.nome();
		this.login = dto.login();
		setSenha(dto.senha());
	}
	
	private void setSenha(String senha) {
    	String secret = System.getenv("SECRET");
		this.senha = BCrypt.withDefaults().hashToString(4, (secret + senha).toCharArray());
	}
	
    public boolean verificarSenha(String senha) {
    	String secret = System.getenv("SECRET");
    	return BCrypt.verifyer().verify((secret + senha).getBytes(), this.senha.getBytes()).verified;
    }
}