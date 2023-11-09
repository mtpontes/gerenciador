import { validaCampos } from './validaCampos.js';
document.addEventListener('DOMContentLoaded', valida);

function valida() {

    const campos = [
        { input: document.getElementById('nome'), regex: /^[A-Za-z0-9\s.'-]{1,100}$/, erro: document.getElementById('nomeErro') },
        { input: document.getElementById('data'), regex: /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/, erro: document.getElementById('dataErro') }
    ];
    
	validaCampos(campos);
}