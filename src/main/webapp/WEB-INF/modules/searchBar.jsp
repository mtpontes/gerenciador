<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, br.com.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/empresa" var="linkEmpresaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles/modules/searchBar.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
	
	<script src="js/modules/searchBar/searchBar.js" type="module"></script>
</head>
<form class="form-search" id="form-search" action="${linkEmpresaServlet}" method="get">
	<div class="search" id="search">
		<div class="form-search-container">
			<input type="search" class="search-input" id="search-input" name="nomeEmpresa" placeholder="Ex: Abc Inc">
			<p class="botao-limpar material-symbols-outlined" id="botao-limpar" style="display:none">close</p>
		</div>

		<input type="hidden" name="acao" value="search">
		<input type="submit" class="pesquisar search-icon-header material-symbols-outlined" id="pesquisar" value="search">
	</div>
</form>
</html>