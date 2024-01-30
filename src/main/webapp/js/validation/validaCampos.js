/**
 * Validação de campos do formulário com base em expressões regulares.
 * Adiciona um evento 'input' a cada campo, ocultando a mensagem de erro associada quando o usuário interage.
 * Adiciona um evento 'submit' ao formulário, verificando cada campo com sua expressão regular correspondente.
 * Se um campo não atender à validação, impede o envio do formulário e exibe a mensagem de erro.
 * 
 * @param {Array} campos - Array de objetos contendo informações sobre os campos a serem validados.
 */
export function validaCampos(campos) {
    const formulario = document.getElementById('formulario');
    console.log("executando validaCampos");

    // Adiciona um evento 'input' a cada elemento do array para ocultar mensagens de erro
    campos.forEach(function(campo) {
        campo.input.addEventListener('input', function() {
            campo.erro.style.display = 'none';
        });
    });

    formulario.addEventListener('submit', function(event) {
        campos.forEach(function(campo) {
            // Remove espaços do texto do campo atual
            const valor = campo.input.value.trim();
            
            // Verifica se o texto não está de acordo com a expressão regular
            if (!campo.regex.test(valor)) {
                event.preventDefault();
                
                // Exibe o elemento <span> com a mensagem de erro
                campo.erro.style.display = 'block';
            }
        });
    });
}
