<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,main.java.br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="main.java.br.com.alura.gerenciador.util.DateUtil" %>


<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Catalogo de Empresas</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/base.css">
	<link rel="stylesheet" href="styles/header.css">
	<!-- Ícones fornecidos por Font Awesome (https://fontawesome.com/) -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
	<script src="resources/js/headerModal.js"></script>
</head>
<body>
	<header class="cabecalho">
		<div class="container">
			<div class="container-minhas-empresas">
				<a class="container-minhas-empresas-link" href="entrada?acao=ListaEmpresasUsuario">Minhas Empresas</a>
			</div>

			<div class="container-outras-empresas">
				<a class="container-outras-empresas-link" href="entrada?acao=ListaEmpresas">Outras Empresas</a>
			</div>
		</div>

		<div class="cabecalho-container-user">
			<i class="fas fa-user" id="icone-usuario"></i>
		</div>
	</header>
	
	<div id="modal" class="modal" style="display:none">
		<div class="user-menu">
			<h2 class="usuario-logado">${usuarioLogado.nome } </h2>
			<div class="usuario-sair">
				<a class="logout" href="usuario?acao=Logout"><i class="fa-solid fa-right-from-bracket"></i> Sair</a>
			</div>
		</div>
	</div>
</body>
</html>