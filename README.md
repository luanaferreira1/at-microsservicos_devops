# Assessment: Microsserviços e DevOps com Spring Boot

Implementação prática do Assessment, demonstrando uma arquitetura de microsserviços orquestrada via Kubernetes (Minikube).

## Arquitetura do projeto

O projeto consiste em um sistema de E-commerce simplificado.

Os componentes principais são:

* **product-service:** Microsserviço responsável por gerenciar o catálogo de produtos. Expõe endpoints REST e conecta-se ao banco de dados.
* **order-service:** Microsserviço consumidor. Recebe pedidos e comunica-se de forma síncrona (via OpenFeign) com o *Product Service* para validar dados e recuperar preços.
* **PostgreSQL:** Banco de dados relacional orquestrado em container, servindo como persistência para os serviços.
* **Kubernetes (Minikube):** Plataforma de orquestração utilizada para gerenciar os containers e a rede entre os serviços.

## Tecnologias usadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.4.12
* **Comunicação:** Spring Cloud OpenFeign
* **Containerização:** Docker
* **Orquestração:** Kubernetes (Minikube)
* **Banco de Dados:** PostgreSQL

## Guia de reprodução
Instruções abaixo para rodar o projeto localmente.

### 1. Pré-requisitos

Certifique-se de ter instalado em sua máquina:
* Docker Desktop
* Minikube
* Java JDK 21
* Maven 

### 2. Inicialização do Ambiente

Inicie o Minikube utilizando o driver do Docker:

```bash
minikube start --driver=docker
```

**Importante:** Configure o seu terminal para utilizar o Docker Daemon de dentro do Minikube. Isso é necessário para que o cluster "enxergue" as imagens que vamos criar.

**No Prompt de Comando:**
```cmd
@FOR /f "tokens=*" %i IN ('minikube -p minikube docker-env') DO @%i
```

**No Linux/Mac/Bash:**
```bash
eval $(minikube -p minikube docker-env)
```

### 3. Build das Aplicações

Com o terminal configurado, gere os arquivos `.jar` e construa as imagens Docker.

**Para o Product Service:**
```bash
cd product-service
mvn clean package
docker build -t product-service:1.0 .
```

**Para o Order Service:**
```bash
cd ../order-service
mvn clean package
docker build -t order-service:1.1 .
```

### 4. Deploy no Kubernetes

A partir da raiz do projeto (onde está a pasta `k8s`), aplique os manifestos na ordem correta:

```bash
# 1. Subir o banco de dados
kubectl apply -f k8s/postgres-deployment.yaml

# 2. Subir os microsserviços
kubectl apply -f k8s/product-deployment.yaml
kubectl apply -f k8s/order-deployment.yaml
```

### 5. Verificação e Testes

**Verificar Status:** Certifique de que todos os Pods estão com status `Running` e `1/1`:

```bash
kubectl get pods
```

**Testar endpoints:** Para testar localmente, precisamos redirecionar as portas do cluster para sua máquina. Abra terminais separados para cada comando abaixo.

**A. Testar Product Service (Cadastro de Produto)**

Ative o redirecionamento:
```bash
kubectl port-forward service/product-service 8080:8080
```

Faça uma requisição POST (via Postman ou Curl):

* **URL:** `http://localhost:8080/products`
* **Método:** `POST`
* **Body (JSON):**
```json
{
  "name": "Notebook Gamer",
  "price": 5000.0
}
```

**B. Testar Order Service (Realizar Pedido)**

Ative o redirecionamento:
```bash
kubectl port-forward service/order-service 8081:8081
```

Faça uma requisição POST:

* **URL:** `http://localhost:8081/orders?productId=1&customer=Teste`
* **Método:** `POST`
* **Resposta Esperada:** Um JSON contendo os dados do pedido e o nome/preço do produto recuperado do outro serviço.