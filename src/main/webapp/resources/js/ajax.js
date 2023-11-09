export async function postRequest(url, corpo) {
	return fetch(url, {
		method: 'POST',
		body: JSON.stringify(corpo),
		headers: {
			'Content-Type': 'application/json'
		}
	})//executa o request, trata um possível erro e converte a resposta em Json
		.then(response => {
			if (!response.ok) {
				throw new Error(`Erro ao realizar a requisição: ${response.statusText}`);
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

export async function postRequestComSessao(url, corpo) {
	
	const sessionID = document.cookie.split(';')
		.map(cookie => cookie.split('='))
		.find(([nome, valor]) => nome.trim() === 'JSESSIONID');
	
	return fetch(url, {
		method: 'POST',
		body: JSON.stringify(corpo),
		headers: {
			'Content-Type': 'application/json',
			'JSESSIONID': sessionID
		}
	})//executa o request, trata um possível erro e converte a resposta em Json
		.then(response => {
			if (!response.ok) {
				throw new Error(`Erro ao realizar a requisição: ${response.statusText}`);
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


export async function getRequest(url) {
	return fetch(url, {
		method: 'GET'
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`Erro ao realizar a requisição: ${response.statusText}`);
			}
			return response.json();
		})
		.then(dadosConvertidos => {
			console.log('dadosConvertidos', dadosConvertidos);
			return dadosConvertidos;
		})
		.catch(error => {
			console.error('Erro:', error);
			throw error;
		});
}