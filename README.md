# ☕ CafeFlow - Sistema de Gestão para Nomadismo Digital

O **CafeFlow** é uma API inteligente desenvolvida para resolver o conflito comum em cafeterias modernas: a ocupação prolongada de mesas por nômades digitais sem o consumo proporcional.

A solução proposta transforma o valor da reserva em **crédito de consumo**, garantindo a rotatividade e a receita do estabelecimento, enquanto assegura ao trabalhador remoto um espaço com infraestrutura (tomada e conforto).

---

## 🚀 Diferenciais do Projeto

- **Consumo Mínimo Revertido:** O valor pago na reserva não é uma taxa de aluguel, mas um crédito pré-pago que o cliente utiliza para consumir produtos do café.
- **Gestão de Infraestrutura:** Controle específico de estações de trabalho que possuem tomadas disponíveis.
- **Liberação Inteligente:** O sistema gerencia o estado da mesa (Disponível/Ocupada) automaticamente durante o ciclo de vida da reserva (Check-in ao Check-out).
- **Relatórios Financeiros:** Endpoint de faturamento em tempo real baseado apenas em reservas concluídas.
- **Tratamento Profissional de Erros:** Mensagens claras para regras de negócio (ex: mesa ocupada ou valor de reserva insuficiente).

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21 (LTS)
- **Framework:** Spring Boot 3.3.0
- **Banco de Dados:** SQLite (persistência local)
- **ORM:** Spring Data JPA
- **Documentação:** OpenAPI 3 (Swagger UI)
- **Gerenciamento de Dependências:** Maven
- **Produtividade:** Lombok

## 📋 Como Rodar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com
   ```
2. **Importe o projeto:** Abra no IntelliJ IDEA ou sua IDE de preferência.
3. **Execute a aplicação:** Rode a classe `CafeflowApplication.java`.
4. **Acesse a API:**
    - **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 📌 Endpoints Principais

### Stations (Mesas)
- `GET /stations/available`: Lista apenas as mesas livres com infraestrutura.
- `POST /stations`: Cadastro de novas mesas (com ou sem tomada).

### Bookings (Reservas & Consumo)
- `POST /bookings`: Realiza reserva e ativa o crédito de consumo.
- `PATCH /bookings/{id}/consume`: Lança consumo de produtos para abater do saldo.
- `POST /bookings/{id}/checkout`: Finaliza a conta, calcula o saldo devedor/restante e libera a mesa.
- `GET /bookings/revenue`: Relatório de faturamento total do café.

---
Desenvolvido por **Felipe Castro**
🚀 [LinkedIn](https://www.linkedin.com/in/felipecavcastro/) | [GitHub](https://github.com/felipecavcastro)
