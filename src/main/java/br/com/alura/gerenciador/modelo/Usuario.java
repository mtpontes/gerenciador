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
	private static final String secret = System.getenv("SECRET");
	
	@OneToMany(mappedBy = "usuario")
	private List<Empresa> empresas = new ArrayList<>();
	

	public boolean ehIgual(String login, String senha) {
		if(!this.login.equals(login)) {
			return false;
		}
		
		if(!this.senha.equals(senha)) {
			return false;
		}
		return true;
	}
	
	public void setSenha(String senha) {
        System.err.println("Definindo senha... A variável de ambiente é: " + secret);
		this.senha = BCrypt.withDefaults().hashToString(4, (secret + senha).toCharArray());
	}
	
    public boolean verificarSenha(String senha) {
//        BCrypt.Result result = BCrypt.verifyer().verify(senha.toCharArray(), this.senha);
//        return result.verified;
    	System.out.println("Verificando senha...");
    	return BCrypt.verifyer().verify((secret + senha).getBytes(), this.senha.getBytes()).verified;

    }
}

