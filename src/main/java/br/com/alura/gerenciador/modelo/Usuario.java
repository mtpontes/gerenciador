package main.java.br.com.alura.gerenciador.modelo;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.dto.NovoUsuarioDTO;

@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
	private Long id;
	@Getter	
	private String login;
	@Getter
	private String senha;
	@Getter
	private String nome;
	@OneToMany(mappedBy = "usuario")
	private List<Empresa> empresas = new ArrayList<>();
	
	public Usuario(String login, String senha) {
		this.login = login;
		setSenha(senha);
	}
	public Usuario(NovoUsuarioDTO dto) {
		this.nome = dto.nome();
		this.login = dto.login();
		setSenha(dto.senha());
	}
	
	public void setSenha(String senha) {
    	String secret = System.getenv("SECRET");
        System.out.println("Definindo senha... A variável de ambiente é: " + secret);
		this.senha = BCrypt.withDefaults().hashToString(4, (secret + senha).toCharArray());
	}
	
    public boolean verificarSenha(String senha) {
    	String secret = System.getenv("SECRET");
    	System.out.println("Verificando senha...");
    	return BCrypt.verifyer().verify((secret + senha).getBytes(), this.senha.getBytes()).verified;
    }
}