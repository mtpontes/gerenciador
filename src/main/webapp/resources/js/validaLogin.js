document.addEventListener('DOMContentLoaded', validaLogin);

function validaLogin () {
    const formulario = document.getElementById('formulario');

    const campos = [
        { input: document.getElementById('login'), regex: /^[a-zA-Z0-9_.]{3,20}$/, mensagemDeErro: document.getElementById('loginErro') },
        { input: document.getElementById('senha'), regex: /.{8,}/, mensagemDeErro: document.getElementById('senhaErro') }
    ];
    
    campos.forEach((campo) => {
        campo.input.addEventListener('input', () => {
            campo.mensagemDeErro.style.display = 'none';
        })
    });

    formulario.addEventListener('submit', function(event) {
      campos.forEach((campo) =>{
        const textoNoCampo = campo.input.value.trim();
        if(!campo.regex.test(textoNoCampo)){
                event.preventDefault();
                campo.mensagemDeErro.style.display = 'block';
              }
          })
    });
}