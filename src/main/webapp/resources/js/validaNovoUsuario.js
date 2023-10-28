document.addEventListener('DOMContentLoaded', valida);

function valida() {
    const formulario = document.getElementById('formulario');

    //mapeia elementos e agrupa suas propriedades
    const campos = [
        { input: document.getElementById('nome'), regex: /^[A-Za-zÀ-ÖØ-öø-ÿ']+([\s-][A-Za-zÀ-ÖØ-öø-ÿ']+)*$/, erro: document.getElementById('nomeErro') },
        { input: document.getElementById('login'), regex: /^[a-zA-Z0-9_.]{3,20}$/, erro: document.getElementById('loginErro') },
        { input: document.getElementById('senha'), regex: /.{8,}/, erro: document.getElementById('senhaErro') },
        { input: document.getElementById('confirma'), regex: /.+/, erro: document.getElementById('confirmaErro') }
    ];

    //aplica evento que apaga mensagens de erro assim que o usuário altera entrada do campo
    campos.forEach(function(campo) {
        campo.input.addEventListener('input', function() {
            campo.erro.style.display = 'none';
        });
    });

    //aplica evento de submit que executa a validação dos campos
    formulario.addEventListener('submit', function(event) {
        //itera sobre os elementos
        campos.forEach(function(campo) {
            //tira os espaços do elemento atual
            const valor = campo.input.value.trim();
            //"este texto não está de acordo com a RegExp?   OU   (o elemento atual é o elemento 'confirma'?   E   o elemento atual é diferente de senha?")
            if (!campo.regex.test(valor) || (campo.input === confirma && valor !== senha.value)) {
                event.preventDefault();
                //exibe o elemento <span> que por padrão é display:none
                campo.erro.style.display = 'block';
            }
        });
    });
}