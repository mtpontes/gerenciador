<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/entrada" var="linkEntradaServlet"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formCadastroEmpresas.css">
	<script src="resources/js/validaCadastroEmpresa.js"></script>
<title>Editar Empresa</title>
</head>
<body>
	<!-- inclui header.jsp -->
	<jsp:include page="header.jsp"></jsp:include>

	<h2 class="titulo">ATUALIZAÇÃO DE CADASTRO</h2>
	
	<form class="formulario" id="formulario" action="${linkEntradaServlet }" method="post">
			
		<div class="container-nome-e-data">
			<p class="texto">Nome</p>
			<p class="texto">Data Abertura</p> 
		</div>

		<div class="container-input">
			<input class="input-campo" id="nome" name="nome" type="text" value="${nome }" placeholder="Nome da empresa" />
			<input class="input-campo" id="data" name="data" type="text" placeholder="Exemplo: 01/01/2001"" />
		</div>
			
		<div class="container-erro">
			<span class="form-label" id="mensagem-erro-login" style="display:none">Nome precisa ser preenchido</span>
			<span class="form-label" id="mensagem-erro-data" style="display:none">Insira a data no formato dd/MM/aaaa</span>
		</div>

		<input type="hidden" name="id" value="${id }">
		<input type="hidden" name="acao" value="AlteraEmpresa">
		<input class="botao-enviar" type="submit" value="Enviar" />
	</form>

</body>
</html>