<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:url value="/entrada" var="linkEntradaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formCadastroEmpresas.css">
	<script src="resources/js/validaCadastroEmpresa.js"></script>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include>
	<h2 class="titulo">CADASTRO</h2>
	
	<form class="formulario" id="formulario" action="${linkEntradaServlet }" method="post">
	
		<div class="container-nome-e-data">
			<p class="texto">Nome</p>
			<p class="texto">Data Abertura</p> 
		</div>
			
		<div class="container-input">
			<input class="input-campo" id="nome" name="nome" type="text"  placeholder="Nome da empresa" style="color: black"/>
			<input class="input-campo" id="data" name="data" type="text"  placeholder="Exemplo: 01/01/2001" style="color: black"/>
		</div>
		
		<div class="container-erro">
			<div class="container-erro-div">
				<span class="form-label" id="mensagem-erro-login" style="display:none">Nome precisa ser preenchido</span>
			</div>
			<div class="container-erro-div">
				<span class="form-label" id="mensagem-erro-data" style="display:none">Insira a data no formato dd/MM/aaaa</span>
			</div>
		</div>
	
		<input type="hidden" name="acao" value="NovaEmpresa">
		<input class="botao-enviar" type="submit" value="Enviar" />
	</form>
	
	</body>
	</html>