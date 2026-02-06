# 🌬️ Project Air Conditioning

Sistema de gerenciamento de climatização desenvolvido com **Spring Boot 3.4.2** e implantado via **Docker** no **Render**, utilizando **Supabase (PostgreSQL)** como banco de dados.

---

## 🗄️ 1. Banco de Dados (Supabase)
O banco de dados utiliza **PostgreSQL** hospedado na nuvem.

* **Criação:** Acesse [supabase.com](https://supabase.com) e crie um novo projeto.
* **Configuração de Conexão:**
    * Vá em `Project Settings` > `Database`.
    * Em **Connection string**, selecione a aba **URI** e o modo **Transaction Pooler** (Porta `6543`).
* **Padrão da URL:** `jdbc:postgresql://[HOST]:6543/postgres?prepareThreshold=0`
* **Credenciais:** Guarde o Usuário (`postgres.xxxx`) e a Senha definida.

---

## ☕ 2. Backend (Spring Boot 3.4.2)
Desenvolvido com **Java 21**.

### 📂 Estrutura de Pacotes
Para evitar erros de `ComponentScan`, mantenha as classes sob a raiz `com.system_air`:

| Pacote | Conteúdo |
| :--- | :--- |
| `com.system_air.project_air.conditioning` | Classe Principal (Application) |
| `com.system_air.project_airconditioning.model` | Entidades JPA |
| `com.system_air.project_airconditioning.controller` | Endpoints REST |

---

### ⚙️ Configuração (application.properties)
Localizado em `src/main/resources/`:

properties
spring.application.name=project_air-conditioning
server.port=${PORT:8081}

# Conexão via Variáveis de Ambiente
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

---

###  🐳 3. Containerização (Dockerfile)
Arquivo na raiz do projeto para garantir a portabilidade do deploy.

Dockerfile
# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jdk
COPY --from=build /target/project_air-conditioning-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

---

### 🚀 4. Deploy (Render)
Novo Serviço: Crie um Web Service conectado ao seu GitHub.

Runtime: Selecione Docker.

Variáveis de Ambiente (Environment Variables):

DB_URL: Sua URL do Supabase.
DB_USER: Seu usuário do banco.
DB_PASSWORD: Sua senha do banco.

PORT: 8081 (alinhado ao EXPOSE do Docker).

Nota: Se precisar reiniciar do zero, use a opção "Clear Build Cache & Deploy".

---

### 🛠️ 5. Comandos Úteis
Bash
# Inicializar o repositório
git init

# Vincular ao GitHub
git remote add origin [https://github.com/LucasSalees/project_air-conditioning.git](https://github.com/LucasSalees/project_air-conditioning.git)

# Enviar alterações
git add .
git commit -m "Descrição da alteração"
git push origin main

### ✅ 6. Teste de Funcionamento
Após o status ficar Live no Render, valide através do endpoint: https://project-air-conditioning.onrender.com/api/agendamentos

Resposta esperada: [] (Um JSON vazio indica que a conexão com o banco foi realizada com sucesso).
