# Driver Allocation System

## Descrição
Este projeto é um sistema de alocação de motoristas e veículos, desenvolvido em Java com Spring Boot. Ele permite a criação, leitura, atualização e deleção de motoristas, veículos e suas associações. O sistema utiliza o banco de dados H2 para armazenamento de dados em memória, além da documentação com o Swagger.

## Funcionalidades
- **Motoristas**: Cadastrar, listar, buscar por ID, editar e deletar motoristas.
- **Veículos**: Cadastrar, listar, buscar por ID, editar e deletar veículos.
- **Associações**: Criar associações entre motoristas e veículos, listar associações, buscar por ID, editar e deletar associações.

## Tecnologias Utilizadas
- **Backend**: Java, Spring Boot
- **Banco de Dados**: H2 Database
- **Testes Unitários**: JUnit, Mockito
- **Documentação da API**: Swagger

## Configuração do Projeto

### Pré-requisitos
- JDK 17 ou superior
- Maven

### Clonando o repositório
```bash
git clone https://github.com/erikmarconato/driver-allocation.git
cd driver_allocation
```

### Executando o projeto
1. Navegue até o diretório do projeto.
2. Execute o comando Maven para iniciar o aplicativo:
```bash
mvn spring-boot:run
```

### Acessando a API
A API estará disponível em `http://localhost:8080`. Você pode acessar a documentação da API através do Swagger em `http://localhost:8080/swagger-ui/index.html`.

## Endpoints
### Motoristas
- `POST /motoristas` - Cria um novo motorista.
- `GET /motoristas` - Lista todos os motoristas.
- `GET /motoristas/{id}` - Busca um motorista por ID.
- `PUT /motoristas/{id}` - Edita os dados de um motorista por ID.
- `DELETE /motoristas/{id}` - Deleta um motorista por ID.

### Veículos
- `POST /veiculos` - Cria um novo veículo.
- `GET /veiculos` - Lista todos os veículos.
- `GET /veiculos/{id}` - Busca um veículo por ID.
- `PUT /veiculos/{id}` - Edita os dados de um veículo por ID.
- `DELETE /veiculos/{id}` - Deleta um veículo por ID.

### Associações
- `POST /associacao` - Cria uma nova associação de motorista e veículo.
- `GET /associacao` - Lista todas as associações cadastradas.
- `GET /associacao/{id}` - Busca a associação por ID.
- `PUT /associacao/{id}` - Edita a associação por ID.
- `DELETE /associacao/{id}` - Deleta a associação por ID.
