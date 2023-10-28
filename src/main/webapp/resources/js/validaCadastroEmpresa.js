document.addEventListener('DOMContentLoaded', valida);

function valida() {
    const formulario = document.getElementById('formulario');

    const campos = [
        { input: document.getElementById('nome'), regex: /^[a-zA-Z0-9_.]{3,20}$/, mensagemErro: document.getElementById('mensagem-erro-login') },
        { input: document.getElementById('data'), regex: /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/, mensagemErro: document.getElementById('mensagem-erro-data') }
    ];
    
    campos.forEach((campo) => {
        campo.input.addEventListener('input', () => {
            campo.mensagemErro.style.display = 'none';
        })
    });

    formulario.addEventListener('submit', (evento) =>{
        campos.forEach((campo) => {
            const textoNoCampo = campo.input.value.trim();
            if(!campo.regex.test(textoNoCampo)){
                evento.preventDefault();
                campo.mensagemErro.style.display = 'inline-block';
            }
        })
    });
}