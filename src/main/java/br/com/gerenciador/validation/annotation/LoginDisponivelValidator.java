package br.com.gerenciador.validation.annotation;

import br.com.gerenciador.repository.UsuarioRepository;
import br.com.gerenciador.repository.UsuarioRepositoryJPA;
import br.com.gerenciador.util.JPAUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginDisponivelValidator implements ConstraintValidator<LoginDisponivelAnnotation, String> {

	private UsuarioRepository repository = new UsuarioRepositoryJPA(JPAUtil.getEntityManager());
	
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
