/**
 * Realiza uma requisição HTTP do tipo POST para a URL especificada com o corpo fornecido.
 * 
 * @async
 * @function
 * @param {string} url - A URL de destino da requisição.
 * @param {Object} body - O corpo da requisição, que será convertido para JSON.
 * @returns {Promise<Object>} - Uma Promise que resolve para os dados convertidos da resposta JSON da requisição.
 * @throws {Error} - Lança um erro caso a resposta não esteja OK ou ocorra algum problema durante a requisição.
 */
export async function postRequest(url, body) {
	return fetch(url, {
		method: 'POST',
		body: JSON.stringify(body),
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

/**
 * Realiza uma requisição HTTP do tipo GET para a URL relativa especificada com os parâmetros fornecidos.
 * 
 * @async
 * @function
 * @param {string} urlRelativa - A parte da URL após o domínio.
 * @param {Object} params - Parâmetros a serem incluídos na URL da requisição.
 * @returns {Promise<Object>} - Uma Promise que resolve para os dados convertidos da resposta JSON da requisição.
 * @throws {Error} - Lança um erro caso a resposta não esteja OK ou ocorra algum problema durante a requisição.
 */
export async function getRequest(urlRelativa, params) {
    //constroi a URL com os parâmetros
    const baseURL = window.location.origin;
    const urlWithParams = new URL(`${baseURL}${urlRelativa}`);
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

/**
 * Realiza uma requisição HTTP do tipo PUT para a URL relativa especificada com o corpo de requisição fornecido.
 * 
 * @async
 * @function
 * @param {string} urlRelativa - A parte da URL após o domínio.
 * @param {Object} body - Dados a serem enviados no corpo da requisição.
 * @returns {Promise<Response>} - Uma Promise que resolve para a resposta da requisição.
 * @throws {Error} - Lança um erro caso a resposta não esteja OK ou ocorra algum problema durante a requisição.
 */
export async function putRequest(urlRelativa, body) {
	const completeUrl = window.location.origin + urlRelativa;
	return fetch(completeUrl, {
		method: 'PUT',
		body: JSON.stringify(body),
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
		return response;
	})
	.catch(error => {
		console.error('Erro:', error);
		throw error;
	});
}