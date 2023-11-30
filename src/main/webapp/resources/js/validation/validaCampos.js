export function validaCampos(campos){
	const formulario = document.getElementById('formulario');
	console.log("executando validaCampos");

	//adiciona um evento 'input' em cada elemento do array
    campos.forEach(function(campo) {
        campo.input.addEventListener('input', function() {
            campo.erro.style.display = 'none';
        });
    });

    //aplica evento de submit que executa a validação dos campos
    formulario.addEventListener('submit', function(event) {
        //itera sobre os elementos
        campos.forEach(function(campo) {
            //tira os espaços do texto do campo atual
            const valor = campo.input.value.trim();
            //"este texto não está de acordo com a expressão regular?"
            if (!campo.regex.test(valor)) {
                event.preventDefault();
                console.log("Não passou no teste do regex");
                //exibe o elemento <span> que por padrão é display:none
                campo.erro.style.display = 'block';
            }
        });
    });
}