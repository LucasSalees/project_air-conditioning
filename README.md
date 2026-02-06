# 🌬️ Project Air Conditioning

Sistema de gerenciamento de climatização desenvolvido com **Spring Boot 3.4.2**, utilizando **Java 21**, banco de dados **PostgreSQL (Supabase)** e deploy via **Docker no Render**.

Este documento explica **passo a passo** como configurar o projeto desde o banco de dados até o deploy em produção.

---

## 📌 Tecnologias Utilizadas

- Java 21  
- Spring Boot 3.4.2  
- PostgreSQL (Supabase)  
- Docker  
- Render (Deploy)  
- Maven  

---

## 🗄️ 1. Banco de Dados (Supabase)

O projeto utiliza **PostgreSQL hospedado no Supabase**.

### 1.1 Criando o Banco de Dados
1. Acesse: https://supabase.com  
2. Crie uma conta (caso não tenha).
3. Clique em **New Project**.
4. Defina:
   - Nome do projeto
   - Senha do banco (guarde essa senha)
   - Região

Após a criação, aguarde a inicialização do projeto.

### 1.2 Obtendo as Credenciais de Conexão

1. No painel do Supabase, vá em:
   **Project Settings → Database**
2. Localize a seção **Connection string**.
3. Selecione:
   - Aba: **URI**
   - Modo: **Transaction Pooler**
   - Porta: **6543**

### Padrão da URL JDBC:

jdbc:postgresql://[HOST]:6543/postgres?prepareThreshold=0

### Guarde as seguintes informações:

DB_URL
DB_USER (geralmente postgres.xxxx)
DB_PASSWORD

Esses dados serão usados no backend e no deploy.

---

## ☕ 2. Backend (Spring Boot)

O backend foi desenvolvido com Spring Boot 3.4.2 e Java 21.

### 2.1 Estrutura de Pacotes

⚠️ Importante:
Para evitar problemas com ComponentScan, todas as classes devem estar abaixo do pacote raiz:
### com.system_air

Estrutura recomendada:

| Pacote                                              | Responsabilidade                 |
| --------------------------------------------------- | -------------------------------- |
| `com.system_air.project_air.conditioning`           | Classe principal (`Application`) |
| `com.system_air.project_airconditioning.model`      | Entidades JPA                    |
| `com.system_air.project_airconditioning.controller` | Controllers / Endpoints REST     |

### 2.2 Configuração do application.properties

Arquivo localizado em:
src/main/resources/application.properties

Conteúdo:

spring.application.name=project_air-conditioning
server.port=${PORT:8081}

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

🔎 Explicação:

As credenciais do banco são lidas por variáveis de ambiente, garantindo segurança.
O ddl-auto=update cria/atualiza as tabelas automaticamente.
A porta é configurável via variável PORT, necessária para o Render.

---

## 🐳 3. Docker (Containerização)

O Docker garante que o projeto rode da mesma forma em qualquer ambiente.

### 3.1 Dockerfile

Crie um arquivo chamado Dockerfile na raiz do projeto:

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/project_air-conditioning-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

🔎 O que acontece aqui:

O Maven compila o projeto.
O JAR gerado é copiado para uma imagem mais leve.
A aplicação expõe a porta 8081.

---

## 🚀 4. Deploy no Render

### 4.1 Criando o Serviço

Acesse: https://render.com
Clique em New → Web Service
Conecte seu repositório do GitHub.
Selecione:
Runtime: Docker
Branch: main

### 4.2 Variáveis de Ambiente

No painel do serviço, vá em Environment → Environment Variables e adicione:

| Variável      | Valor            |
| ------------- | ---------------- |
| `DB_URL`      | URL do Supabase  |
| `DB_USER`     | Usuário do banco |
| `DB_PASSWORD` | Senha do banco   |
| `PORT`        | `8081`           |

⚠️ O valor da PORT deve ser o mesmo definido no Dockerfile (EXPOSE 8081).

---

### 🛠️ 5. Comandos Git Úteis

# Inicializar repositório
git init

# Adicionar repositório remoto
git remote add origin https://github.com/LucasSalees/project_air-conditioning.git

# Adicionar arquivos
git add .

# Commit
git commit -m "Descrição da alteração"

# Enviar para o GitHub
git push origin main

## ✅ 6. Teste de Funcionamento

Após o deploy ficar com status Live no Render, acesse:

https://project-air-conditioning.onrender.com/api/agendamentos

Resposta esperada:

[]

✔️ Um array vazio indica que:

A aplicação está rodando
O backend conectou corretamente ao banco de dados.

---

## 👨‍💻 Autor

**Lucas Sales**  
🔗 LinkedIn: https://www.linkedin.com/in/lucas-salees/


