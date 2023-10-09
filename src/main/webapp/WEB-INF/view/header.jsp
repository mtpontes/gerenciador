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
</head>
<body>
	<header class="cabecalho">
		<div class="container">
			<div class="container-sair">
				<a class="logout" href="entrada?acao=Logout">Sair</a>
			</div>

			<div class="container-minhas-empresas">
				<a class="container-minhas-empresas-link" href="entrada?acao=ListaEmpresasUsuario">Minhas Empresas</a>
			</div>

			<div class="container-outras-empresas">
				<a class="container-outras-empresas-link" href="entrada?acao=ListaEmpresas">Outras Empresas</a>
			</div>
		</div>

		<div class="cabecalho-container-user">
			<h2 class="usuario-logado">${usuarioLogado.login }</h2>
		</div>
	</header>
</body>