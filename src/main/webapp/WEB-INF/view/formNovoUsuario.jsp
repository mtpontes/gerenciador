<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:url value="/usuario" var="linkUsuarioServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Cadastro de Usuario</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/view/formLogin.css">
	
	<script src="js/validation/validaNovoUsuario.js" type="module"></script>
</head>
<body>
	<div class="titulo">
		<h2 class="titulo-cadastro">Cadastro de usuário</h2>
	</div>

	<form class="formulario" id="formulario" action="${linkUsuarioServlet }" method="post">
		<div class="form-group">
			<label class="form-label">Nome</label>
			<input class="form-input" id="nome" type="text" name="nome" placeholder="Seu nome completo"/>
			<span class="mensagem-erro" id="nomeErro" style="display: none;">Nome é obrigatório e só aceita letras, apóstrofo(') e hífen (-)</span>
		</div>
	
		<div class="form-group">
			<label class="form-label">Login</label>
			<input class="form-input" id="login" type="text" name="login" placeholder="Mínimo 3 caracteres"/>
			<span class="mensagem-erro" id="loginErro" style="display:none">Login precisa ter no mínimo 3 caracteres</span>
		</div>
		
		<div class="form-group">
			<label class="form-label">Senha</label>
			<input class="form-input" id="senha" type="password" name="senha" placeholder="Mínimo 8 caracteres" />
			<span class="mensagem-erro" id="senhaErro" style="display:none">Senha precisa ter no mínimo 8 caracteres</span>
		</div>
		
		<div class="form-group">
			<label class="form-label">Confirma senha</label>
			<input class="form-input" id="confirma" type="password" name="confirma" placeholder="Confirme sua senha" />
			<span class="mensagem-erro" id="confirmaErro" style="display:none">Confirmação deve ser idêntica a senha</span>
		</div>
		
		<input type="hidden" name="acao" value="novoUsuario">
		<input class="form-submit" type="submit" value="Enviar" />
	</form>

	<footer></footer>
</body>
</html>