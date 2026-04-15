# MedAlerta

Sistema de gerenciamento de medicamentos — Bootcamp de Engenharia de Software 04/2026.

---

## 👥 Time

| Nome | RU | GitHub |
|------|----|--------|
| Anne Victória Dantas Lima | 5195149 | [@victoriadantas](https://github.com/victoriadantas) |
| Alessandra de Souza Oliveira | 4324388 | [@hereisaless](https://github.com/hereisaless) |
| Desyree Garcia | 986134 | [@desyreegarcia](https://github.com/desyreegarcia) |
| Gabriela de Castro Laurindo | 5215280 | [@gabicastrum](https://github.com/gabicastrum) |
| Jeremias Marques | 5793710 | [@jeremarques](https://github.com/jeremarques) |
| Laion Silvério de Oliveira | 5654622 | [@laionsilverio](https://github.com/laionsilverio) |
| Lucas Santos | 5285287 | [@lucas-glsantos](https://github.com/lucas-glsantos) |
| Renata Couto | 3833684 | [@recouto3](https://github.com/recouto3) |
| Weden Gabriel da Silva Gomes | 4170826 | [@wedengabriel](https://github.com/wedengabriel) |
---

## ⚙️ Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [VSCode](https://code.visualstudio.com/)
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)

---

## 🚀 Como executar

### 1. Clonar o repositório
```bash
git clone https://github.com/Bootcamp-Uninter/TrabalhoBootcamp.git
cd TrabalhoBootcamp
```

### 2. Configurar variáveis de ambiente
```bash
cp .env.exemplo .env
# Edite o arquivo .env com suas configurações
```

### 3. Subir os containers
```bash
docker compose up --build
```

### 4. Rodar a aplicação Java
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 🔗 Acessos

| Serviço | Endereço |
|---------|----------|
| Aplicação | http://localhost:8080 |
| Health check | http://localhost:8080/actuator/health |
| MySQL | localhost:3306 |

---

## 🗄️ Banco de dados

Scripts de inicialização ficam em `docker/mysql/init/` e rodam automaticamente na primeira vez.

Para resetar o banco:
```bash
docker compose down -v
docker compose up --build
```

---

## 🧪 Testes
```bash
mvn test
```

---

## 📁 Estrutura do projeto

```
src/
└── main/
    ├── java/       # Código-fonte
    └── resources/  # Configurações
docker/
└── mysql/init/     # Scripts SQL
docs/               # Documentação e diagramas
```