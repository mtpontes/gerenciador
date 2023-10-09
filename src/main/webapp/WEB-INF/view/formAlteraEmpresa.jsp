<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/entrada" var="linkEntradaServlet"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formAlteraEmpresa.css">
<title>Editar Empresa</title>
</head>
<body>
	<!-- inclui header.jsp -->
	<jsp:include page="header.jsp"></jsp:include>

	<h2 class="titulo">ATUALIZAÇÃO DE CADASTRO</h2>
	<form class="formulario" action="${linkEntradaServlet }" method="post">
		<div class="container">
			<div class="container-nome-e-data">
				<p class="container-nome-e-data-texto">Nome</p>
				<p class="container-nome-e-data-texto">Data Abertura</p> 
			</div>

			<div class="container-campo">
				<input class="container-campo-input" type="text" name="nome" value="${empresa.nome }" placeholder="Nome da empresa" style="color: black;" />
				<input class="container-campo-input" type="text" name="data" placeholder="Exemplo: 01/01/2001" style="color: black;" />
			</div>
		</div>


		<input type="hidden" name="id" value="${empresa.id }">
		<input type="hidden" name="acao" value="AlteraEmpresa">

		<div class="container-enviar">
			<input class="formulario-container-enviar" type="submit" value="Enviar" />
		</div>
	</form>

</body>
</html>