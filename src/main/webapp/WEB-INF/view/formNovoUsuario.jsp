<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:url value="/usuario" var="linkUsuarioServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Cadastro de Usuario</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formLogin.css">
</head>
<body>

	<div class="titulo">
		<h2 class="titulo-cadastro">Cadastro de usuário</h2>
	</div>

	<form class="formulario" action="${linkEntradaServlet }" method="post">
	
		<div class="form-group">
			<label class="form-label">Login</label>
			<input class="form-input" type="text" name="login"/>
		</div>
		
		<div class="form-group">
			<label class="form-label">Senha</label>
			<input class="form-input" type="password" name="senha" pattern=".{8,}" title="A senha deve ter pelo menos 8 caracteres." placegolder="mínimo 8 caracteres" />
		</div>
		
		<input type="hidden" name="acao" value="NovoUsuario">
		<input class="form-submit" type="submit" value="Avançar" />
	</form>

</body>
</html>