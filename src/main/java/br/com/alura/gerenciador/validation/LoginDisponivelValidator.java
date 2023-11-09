package main.java.br.com.alura.gerenciador.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepository;
import main.java.br.com.alura.gerenciador.repository.UsuarioRepositoryMySQL;
import main.java.br.com.alura.gerenciador.util.JPAUtil;

@NoArgsConstructor
public class LoginDisponivelValidator implements ConstraintValidator<LoginDisponivelAnnotation, String> {

	private UsuarioRepository repository = new UsuarioRepositoryMySQL(JPAUtil.getEntityManager());
	
	public LoginDisponivelValidator(UsuarioRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void initialize(LoginDisponivelAnnotation constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String login, ConstraintValidatorContext context) {
		return !repository.existsByLogin(login);
	}

}
