<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:url value="/usuario" var="linkUsuarioServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/base.css">
	<link rel="stylesheet" href="styles/formLogin.css">
	<script src="resources/js/validaLogin.js" type="module"></script>
</head>
<body>

	<div class="titulo">
		<h2 class="titulo-cadastro">Fazer Login</h2>
	</div>

	<form class="formulario" id="formulario" action="${linkUsuarioServlet }" method="get">
		<div class="form-group">
			<label class="form-label">Login </label>
			<input class="form-input" type="text" id="login" name="login" placeholder="insira seu login" />
			<span class="mensagem-erro" id="loginErro" style="display:none">Login precisa ter no mínimo 3 caracteres</span>
		</div>
		
		<div class="form-group">
			<label class="form-label">Senha </label>
			<input class="form-input" type="password" id="senha" name="senha" placeholder="insira sua senha" />
			<span class="mensagem-erro" id="senhaErro" style="display:none">Senha precisa ter no mínimo 8 caracteres</span>
		</div>
		
		<input type="hidden" name="acao" value="login">
		<input class="form-submit" type="submit" value="Enviar" />
	</form>
	
	<a class="novo-usuario" href="usuario?acao=novoUsuarioForm" >Criar conta</a>
	
	<footer></footer>
</body>
</html>
