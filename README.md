# API de Autenticação JWT

API REST para autenticação de usuários com JWT (JSON Web Token). Permite registro, login e proteção de rotas com Spring Security.

## O que o projeto faz

- Registra novos usuários com senha criptografada
- Autentica usuários e gera token JWT
- Protege rotas com validação de token
- Gerencia perfis de usuário (roles)
- Mantém sessões stateless (sem estado)

## Problemas que resolve

- Implementa autenticação segura em APIs REST
- Elimina necessidade de sessões no servidor
- Permite integração com frontends (Angular, React, etc)
- Fornece controle de acesso baseado em perfis
- Protege senhas com bcrypt

## Tecnologias

- Java 21
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- MySQL 8
- JWT (Auth0)
- Lombok
- Maven 3.6+

## Requisitos

**Software necessário:**
- Java 21 ou superior
- Maven 3.6 ou superior
- MySQL 8 ou superior

**Banco de dados:**
- Servidor MySQL acessível
- Database `login-autenticacao` criado

## Estrutura da tabela

Execute o script SQL:

```sql
CREATE TABLE `users` (
  `id` VARCHAR(36) COLLATE utf8mb4_general_ci NOT NULL,
  `name` VARCHAR(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` VARCHAR(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` VARCHAR(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `perfil` VARCHAR(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

## Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/login-auth-api.git
cd login-auth-api
```

### 2. Configure o banco de dados

Edite o arquivo `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://SEU_HOST:3306/SEU_DATABASE?useUnicode=yes&characterEncoding=UTF-8
    username: SEU_USUARIO
    password: SUA_SENHA
```

### 3. Configure a chave secreta JWT

Edite `application.yml`:

```yaml
api:
  security:
    token:
      secret: sua-chave-secreta-aqui-minimo-32-caracteres
```

**Importante:** Use uma chave forte em produção!

### 4. Compile o projeto

```bash
mvn clean install
```

### 5. Execute a aplicação

```bash
mvn spring-boot:run
```

ou

```bash
java -jar target/login-auth-api-0.0.1-SNAPSHOT.jar
```

### 6. Servidor estará rodando

```
http://localhost:8080
```

## Endpoints da API

### Registrar novo usuário

**POST** `/auth/register`

Cria novo usuário no sistema.

**Body (JSON):**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "perfil": "user"
}
```

**Resposta (200 OK):**
```json
{
  "name": "João Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "perfil": "user"
}
```

**Resposta (400 Bad Request):**
Email já cadastrado.

### Login

**POST** `/auth/login`

Autentica usuário e retorna token JWT.

**Body (JSON):**
```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "name": "João Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "perfil": "user"
}
```

**Resposta (400 Bad Request):**
Email ou senha incorretos.

### Rota protegida (exemplo)

**GET** `/user`

Requer autenticação via token JWT.

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Resposta (200 OK):**
```json
"sucesso!"
```

**Resposta (403 Forbidden):**
Token inválido ou ausente.

## Usando a API

### Com curl

**Registrar:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "password": "senha123",
    "perfil": "user"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

**Acessar rota protegida:**
```bash
curl http://localhost:8080/user \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### Com JavaScript (Fetch API)

**Registrar:**
```javascript
fetch('http://localhost:8080/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'João Silva',
    email: 'joao@email.com',
    password: 'senha123',
    perfil: 'user'
  })
})
  .then(response => response.json())
  .then(data => {
    console.log('Token:', data.token);
    localStorage.setItem('token', data.token);
  });
```

**Login:**
```javascript
fetch('http://localhost:8080/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'joao@email.com',
    password: 'senha123'
  })
})
  .then(response => response.json())
  .then(data => {
    localStorage.setItem('token', data.token);
  });
```

**Requisição autenticada:**
```javascript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/user', {
  headers: { 'Authorization': `Bearer ${token}` }
})
  .then(response => response.text())
  .then(data => console.log(data));
```

## Estrutura do projeto

```
src/
└── main/
    └── java/
        └── br/com/redelognet/loginauthapi/
            ├── controllers/
            │   ├── AuthController.java      # Endpoints de autenticação
            │   └── UserController.java      # Endpoints protegidos
            ├── dto/
            │   ├── LoginRequestDTO.java     # Request de login
            │   ├── RegisterRequestDTO.java  # Request de registro
            │   └── ResponseDTO.java         # Response padrão
            ├── entities/
            │   └── User.java                # Entidade JPA
            ├── infra/
            │   ├── cors/
            │   │   └── CorsConfig.java      # Configuração CORS
            │   └── security/
            │       ├── CustomUserDetailsService.java
            │       ├── SecurityConfig.java  # Configuração Spring Security
            │       ├── SecurityFilter.java  # Filtro de validação JWT
            │       └── TokenService.java    # Geração/validação JWT
            ├── repositories/
            │   └── UserRepository.java      # Interface JPA
            └── LoginAuthApiApplication.java
```

## Como funciona

### Fluxo de registro

1. Frontend envia dados do usuário para `/auth/register`
2. Backend verifica se email já existe
3. Senha é criptografada com BCrypt
4. Usuário é salvo no banco
5. Token JWT é gerado e retornado

### Fluxo de login

1. Frontend envia email e senha para `/auth/login`
2. Backend busca usuário por email
3. Senha é validada com BCrypt
4. Token JWT é gerado com expiração de 2 horas
5. Token é retornado ao frontend

### Fluxo de requisição autenticada

1. Frontend envia token no header `Authorization: Bearer TOKEN`
2. `SecurityFilter` intercepta a requisição
3. Token é validado e decodificado
4. Usuário é carregado do banco
5. Perfil (role) é adicionado ao contexto de segurança
6. Requisição prossegue se autorizada

## Perfis de usuário

O sistema suporta diferentes perfis (roles):

- `user`: Usuário comum
- `admin`: Administrador
- `manager`: Gerente
- Personalizáveis conforme necessidade

**Exemplo de uso:**

```java
@GetMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> adminOnly() {
    return ResponseEntity.ok("Acesso admin");
}
```

## Configurações importantes

### Token JWT

**Tempo de expiração:** 2 horas (configurável)

Edite `TokenService.java`:

```java
return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
```

### CORS

Por padrão, aceita requisições de `http://localhost:4200`.

Para alterar, edite `CorsConfig.java`:

```java
.allowedOrigins("http://seu-frontend.com")
```

### Logs

Para habilitar logs de segurança, edite `application.yml`:

```yaml
logging:
  level:
    org:
      springframework:
        security: DEBUG
```

## Segurança

### Boas práticas implementadas

- ✅ Senhas criptografadas com BCrypt
- ✅ Tokens JWT com expiração
- ✅ Sessões stateless
- ✅ CSRF desabilitado (API REST)
- ✅ Validação de token em cada requisição
- ✅ Perfis de usuário (roles)

### Para produção

**Obrigatório:**
- Use chave secreta forte (mínimo 32 caracteres)
- Configure HTTPS
- Restrinja CORS para domínios específicos
- Use variáveis de ambiente para credenciais
- Implemente refresh tokens
- Adicione rate limiting
- Configure logs de auditoria

## Variáveis de ambiente

Para produção, use variáveis de ambiente:

```bash
export DB_HOST=seu-host
export DB_PORT=3306
export DB_NAME=seu-database
export DB_USER=seu-usuario
export DB_PASSWORD=sua-senha
export JWT_SECRET=sua-chave-secreta-forte
```

No `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

api:
  security:
    token:
      secret: ${JWT_SECRET}
```

## Melhorias futuras

### Autenticação
- Implementar refresh tokens
- Adicionar autenticação OAuth2 (Google, GitHub)
- Criar endpoint de recuperação de senha
- Implementar verificação de email
- Adicionar autenticação de dois fatores (2FA)
- Criar sistema de bloqueio de conta após tentativas falhas

### Segurança
- Adicionar rate limiting por IP
- Implementar blacklist de tokens
- Criar logs de auditoria de acessos
- Adicionar validação de força de senha
- Implementar política de expiração de senha
- Criar sistema de permissões granulares

### Funcionalidades
- Criar endpoint para atualizar perfil
- Implementar upload de foto de perfil
- Adicionar endpoint para listar usuários (admin)
- Criar sistema de notificações
- Implementar soft delete de usuários
- Adicionar busca e filtros de usuários

### Validação
- Adicionar validação de email (formato)
- Implementar validação de CPF/CNPJ
- Criar validação de campos obrigatórios
- Adicionar mensagens de erro personalizadas
- Implementar validação de unicidade de email

### Documentação
- Adicionar Swagger/OpenAPI
- Criar collection do Postman
- Documentar códigos de erro
- Adicionar exemplos de integração
- Criar guia de deployment

## Notas importantes

- Token JWT expira em 2 horas (configurável)
- Senhas nunca são retornadas pela API
- Perfis são prefixados com `ROLE_` automaticamente
- CORS configurado apenas para localhost:4200
- Sessões são stateless (sem estado no servidor)
- Hibernate configurado em modo `update` (cria/atualiza tabelas)
