<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/empresa" var="linkEmpresaServlet"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formCadastroEmpresas.css">
	<script src="resources/js/validaCadastroEmpresa.js" type="module"></script>
<title>Editar Empresa</title>
</head>
<body>
	<!-- inclui header.jsp -->
	<jsp:include page="header.jsp"></jsp:include>

	<h2 class="titulo">ATUALIZAÇÃO DE CADASTRO</h2>
	
	<form class="formulario" id="formulario" action="${linkEmpresaServlet }" method="post">
			
		<div class="container-nome-e-data">
			<p class="texto">Nome</p>
			<p class="texto">Data Abertura</p> 
		</div>

		<div class="container-input">
			<input class="form-input" id="nome" name="nome" type="text" value="${nome }" placeholder="Nome da empresa" />
			<input class="form-input" id="data" name="data" type="text" placeholder="Exemplo: 01/01/2001"" />
		</div>
			
		<div class="container-erro">
			<div class="container-erro-div">
				<span class="form-label" id="nomeErro" style="display:none">Nome precisa ser preenchido</span>
			</div>
			<div class="container-erro-div">
				<span class="form-label" id="dataErro" style="display:none">Insira a data no formato dd/MM/aaaa</span>
			</div>
		</div>

		<input type="hidden" name="id" value="${id }">
		<input type="hidden" name="acao" value="alteraEmpresa">
		<input class="botao-enviar" type="submit" value="Enviar" />
	</form>

	<footer></footer>
</body>
</html>