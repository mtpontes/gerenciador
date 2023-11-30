export function cssColors(){
	const estiloBody = getComputedStyle(document.body);
	
	const cores = {
		corPrincipal: estiloBody.getPropertyValue('--cor-principal'),
		corSecundaria: estiloBody.getPropertyValue('--cor-secundaria'),
		corTerciaria: estiloBody.getPropertyValue('--cor-terciaria'),
		corQuaternaria: estiloBody.getPropertyValue('--cor-quarternaria'),
		corQuinternaria:estiloBody.getPropertyValue('--cor-quinternaria'),
		corBranca: estiloBody.getPropertyValue('--cor-branca')
	}
	return cores;
}
