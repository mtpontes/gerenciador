package main.java.br.com.alura.gerenciador.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import at.favre.lib.crypto.bcrypt.BCrypt;
//import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Getter @Setter
	private String login;
	@Getter
	private String senha;
	private static final String secret = System.getenv("SECRET");
	
	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = hashSenha(senha);
	}

	public boolean ehIgual(String login, String senha) {
		if(!this.login.equals(login)) {
			return false;
		}
		
		if(!this.senha.equals(senha)) {
			return false;
		}
		return true;
	}
	
	public String hashSenha(String senha) {
        System.err.println("Definindo senha... A variável de ambiente é: " + secret);
		return BCrypt.withDefaults().hashToString(4, (secret + senha).toCharArray());
	}
	
    public boolean verificarSenha(String senha) {
//        BCrypt.Result result = BCrypt.verifyer().verify(senha.toCharArray(), this.senha);
//        return result.verified;
    	return BCrypt.verifyer().verify((secret + senha).getBytes(), this.senha.getBytes()).verified;

    }
}

