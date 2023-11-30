export async function postRequest(url, corpo) {
	return fetch(url, {
		method: 'POST',
		body: JSON.stringify(corpo),
		headers: {
			'Content-Type': 'application/json'
		}
	})//executa o request, lança um possível erro ou converte a resposta em Json
    .then(response => {
        if (!response.ok) {
            // Se a resposta não estiver OK, trata como um erro
            return response.json().then(errorData => {
				throw new Error(`${errorData.error}: ${response.statusText}`);
            })
        }
        return response.json();
	})//pega a resposta e retorna
	.then(function(dadosConvertidos) {
		console.log('dadosConvertidos', dadosConvertidos);
		return dadosConvertidos;
	})//captura algum outro possível erro
	.catch(error => {
		console.error('Erro:', error);
		throw error;
	});
};

export async function getRequest(url, params) {
    //constroi a URL com os parâmetros
    const urlWithParams = new URL(url);
    Object.keys(params).forEach(key => urlWithParams.searchParams.append(key, params[key]));

    //faz a requisição GET
    return fetch(urlWithParams, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
				throw new Error(`${errorData.error}: ${response.statusText}`);
            });
        }
        return response.json();
    })
    .then(function(dadosConvertidos) {
        console.log('dadosConvertidos', dadosConvertidos);
        return dadosConvertidos;
    })
    .catch(error => {
        console.error('Erro:', error);
        throw error;
    });
};


export async function putRequest(url, corpo) {
	return fetch(url, {
		method: 'PUT',
		body: JSON.stringify(corpo),
		headers: {
			'Content-Type': 'application/json'
		}
	})
	.then(response => {
		if (!response.ok) {
			response.json().then(errorData => {
				throw new Error(`${errorData.error}: ${response.statusText}`);
			})
		}
		return response.json();
	})
	.then(function(dadosConvertidos) {
		console.log('dadosConvertidos', dadosConvertidos);
		return dadosConvertidos;
	})
	.catch(error => {
		console.error('Erro:', error);
		throw error;
	});
}