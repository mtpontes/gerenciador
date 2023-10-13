package main.java.br.com.alura.gerenciador.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Transient;
//import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
	private Long id;
	@Getter @Setter
	private String login;
	@Getter
	private String senha;
	@Getter
	private String nome;
	
	@OneToMany(mappedBy = "usuario")
	private List<Empresa> empresas = new ArrayList<>();

	@Transient
	private static final String secret = System.getenv("SECRET");
	
	public Usuario(String login, String senha) {
		this.login= login;
		this.senha = setSenha(senha);
	}

	
	public String setSenha(String senha) {
        System.out.println("Definindo senha... A variável de ambiente é: " + secret);
		return this.senha = BCrypt.withDefaults().hashToString(4, (secret + senha).toCharArray());
	}
	
    public boolean verificarSenha(String senha) {
    	System.out.println("Verificando senha...");
    	return BCrypt.verifyer().verify((secret + senha).getBytes(), this.senha.getBytes()).verified;

    }
}

