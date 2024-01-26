<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/empresa" var="linkEmpresaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Header</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/base.css">
	<link rel="stylesheet" href="styles/modules/header.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
	
	<script src="js/modules/header/header.js" type="module"></script>
	
	
	
	<link rel="stylesheet" href="styles/modules/searchBar.css">
	<script src="js/modules/searchBar/searchBar.js" type="module"></script>
	<script src="js/modules/header/header.js" type="module"></script>
</head>
<header>
	<section class="cabecalho" id="cabecalho">
		<div class="container">
			<a class="container-minhas-empresas-link" href="empresa?acao=listaEmpresasUsuario&size=&page=">Minhas Empresas</a>
			<a class="container-outras-empresas-link" href="empresa?acao=listaEmpresas&size=&page=">Outras Empresas</a>
          
			<jsp:include page="searchBar.jsp"></jsp:include>
		</div>
		<i class="material-symbols-outlined" id="icone-usuario">account_circle</i>
	</section>

	<section class="modal" id="modal" style="display:none">
		<h2 class="usuario-logado">${usuarioLogado.nome } </h2>
		
		<a class="botao-logout" href="usuario?acao=logout">
			<i class="classe">Sair</i>
			<i class="material-symbols-outlined">logout</i>
		</a>
	</section>
</header>
</html>