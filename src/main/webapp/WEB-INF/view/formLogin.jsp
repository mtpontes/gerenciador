<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:url value="/usuario" var="linkEntradaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formLogin.css">
</head>
<body>

	<div class="titulo">
		<h2 class="titulo-cadastro">Fazer Login</h2>
	</div>

	<form class="formulario" action="${linkEntradaServlet }" method="post">
		<div class="form-group">
			<label class="form-label">Login </label>
			<input class="form-input" type="text" name="login" placeholder="insira seu login" />
		</div>
		
		<div class="form-group">
			<label class="form-label">Senha </label>
			<input class="form-input" type="password" name="senha" placeholder="insira sua senha" />
		</div>
		
		<input type="hidden" name="acao" value="Login">
		<input class="form-submit" type="submit" value="Enviar" />
	</form>
	
	<div class="novo-usuario">
		<a class="novo-usuario-link" href="usuario?acao=NovoUsuarioForm" >Criar conta</a>
	</div>

</body>
</html>
