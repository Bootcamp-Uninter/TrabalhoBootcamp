# **MedAlerta**

Sistema de gerenciamento de medicamentos | [Engenharia de Software 04/2026](https://www.youtube.com/watch?v=j6-r_D-2hcE).


---


<!-- Tecnologias -->
<p align="center">
  <a href="https://www.java.com/" title="Java">
    <img src="https://github.com/get-icon/geticon/raw/master/icons/java.svg" alt="Java" height="55px">
  </a>
  <a href="https://spring.io/projects/spring-boot" title="Spring Boot">
    <img src="https://img.icons8.com/color/48/spring-logo.png" alt="Spring Boot" height="55px">
  </a>
  <a href="https://www.mysql.com/" title="MySQL">
    <img src="https://github.com/get-icon/geticon/raw/master/icons/mysql.svg" alt="MySQL" height="50px">
  </a>
  <a href="https://www.docker.com/" title="Docker">
    <img src="https://github.com/get-icon/geticon/raw/master/icons/docker-icon.svg" alt="Docker" height="50px">
  </a>
  <a href="https://code.visualstudio.com/" title="VSCode">
    <img src="https://github.com/get-icon/geticon/raw/master/icons/visual-studio-code.svg" alt="VSCode" height="50px">
  </a>
</p>


---


## 📌 **Descrição**

Este repositório expõe a **infraestrutura arquitetural do projeto MedAlerta**, desenvolvido pela equipe participante do Bootcamp de Engenharia de Software elaborado pela [Uninter](https://www.uninter.com/).

O objetivo principal deste projeto foi desenvolver um sistema rápido, que atendesse ao requisitos especificados abaixo:

- Arquitetura em camadas
- CRUD
- Containerização da aplicação
- Banco de dados MySQL integrado
- Autenticação e segurança
- CI/CD com GitHub Actions


---


## 🏗️ **Arquitetura**

O projeto segue o padrão de **arquitetura em camadas**:

Service → Repository → Banco de Dados

- **Service**: regras de negócio  
- **Repository (CRUD)**: acesso aos dados  
- **Banco (MySQL)**: persistência  


---


## 📁 **Estrutura do projeto**


```
src/
└── main/
    ├── java/       # Código-fonte
    └── resources/  # Configurações
docker/
└── mysql/init/     # Scripts SQL
docs/               # Documentação e diagramas
```


---


## ⚙️ **Pré-requisitos**

- [Docker](https://www.docker.com/products/docker-desktop/)
- [VSCode](https://code.visualstudio.com/)
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)


---


## 🚀 **Como executar**

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

Para resetar o docker e banco de dados:
```bash
docker compose down -v
```


---


## 🧪 Testes
```bash
mvn test
```

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
| Thiago Bianna Pessanha da Cruz | 5666891 | [@thiagobianna](https://github.com/thiagobianna) |


---
