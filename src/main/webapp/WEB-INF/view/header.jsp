<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,main.java.br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="main.java.br.com.alura.gerenciador.util.DateUtil" %>


<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Header</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/base.css">
	<link rel="stylesheet" href="styles/header.css">
	<script src="resources/js/headerModal.js"></script>
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
</head>
<body>
	<header class="cabecalho">
		<div class="container">
			<a class="container-minhas-empresas-link" href="empresa?acao=listaEmpresasUsuario">Minhas Empresas</a>
			<a class="container-outras-empresas-link" href="empresa?acao=listaEmpresas">Outras Empresas</a>
		</div>

		<i class="material-symbols-outlined" id="icone-usuario">account_circle</i>
	</header>

	<div id="modal" class="modal" style="display:none">
		<div class="usuario">
			<h2 class="usuario-logado">${usuarioLogado.nome } </h2>
			<a class="botao-logout" href="usuario?acao=logout" method='post'>Sair<i class="material-symbols-outlined">logout</i></a>
		</div>
	</div>
</body>
</html>