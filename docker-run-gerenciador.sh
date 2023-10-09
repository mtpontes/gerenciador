#!/bin/bash
docker run -d -p 8080:8080 --name gerenciador -e SECRET=N@mn --network minha-rede gerenciador-image
