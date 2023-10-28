<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,main.java.br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="main.java.br.com.alura.gerenciador.util.DateUtil" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Minhas Empresas</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/listaEmpresas.css">
	<script src="resources/js/usuarioEmpresasBotoes.js"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="container-titulo">
		<h1 class="titulo">Minhas Empresas</h1>
		<a class="nova-empresa"href="entrada?acao=NovaEmpresaForm"><i class="fa-solid fa-circle-plus"></i></a>
	</div>
	
	<ul class="container-empresas">
		
		<li class="colunas">
			<div class="colunas-container">
				<p class="coluna-nome">Nome</p>
				<p class="coluna-data-lista-empresas coluna-data-minhas-empresas" >Data</p>
			</div>
		</li>
	
		<c:forEach items="${empresas}" var="empresa">
			<li class="lista">
				<p class="lista-nome">${empresa.nome }</p>
        		<p class="lista-data">${DateUtil.formatDate(empresa.dataAbertura, "dd/MM/yyyy")}</p>
		        	
	        	<div class="container-botoes">
	        		<a class="edita" id="edita" href="/gerenciador/entrada?acao=MostraEmpresa&nome=${empresa.nome }&id=${empresa.id}">edita</a>
					<a class="remove-ou-restaura" id="remove-ou-restaura" href="/gerenciador/entrada?acao=RemoveEmpresa&id=${empresa.id }" data-ativo=${empresa.ativo } method='post'></a>
				</div>
			</li>
		</c:forEach>
	</ul>

</body>
</html>