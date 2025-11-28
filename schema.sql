-- Esquema da Base de Dados ---

-- 1. Tabela de Utilizadores --
CREATE TABLE IF NOT EXISTS utilizador(
    id_utilizador INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'OPERADOR', 'COMPRAS') NOT NULL
);

-- 2. Tabela de Fornecedores --
CREATE TABLE IF NOT EXISTS fornecedor(
    id_fornecedor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nif CHAR(9) UNIQUE,
    contacto VARCHAR(50)

);

-- 3. Tabela de Clientes --
CREATE TABLE IF NOT EXISTS cliente(
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nif CHAR(9) UNIQUE
);

-- 4. Tabela de Produtos --
CREATE TABLE IF NOT EXISTS produto(
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco_unitario DECIMAL (10, 2) NOT NULL,
    stock_minimo INT DEFAULT 5,
    id_fornecedor INT,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor)

);

-- 5. Tabela de Stock --
CREATE TABLE IF NOT EXISTS stock(
    id_stock INT AUTO_INCREMENT PRIMARY KEY,
    id_produto INT NOT NULL,
    quantidade INT DEFAULT 0,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

-- 6. Tabela de Movimentos --
CREATE TABLE IF NOT EXISTS movimento(
    id_movimento INT AUTO_INCREMENT PRIMARY KEY,
    data_movimento DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo_movimento ENUM('ENTRADA', 'SAÍDA') NOT NULL,
    id_utilizador INT NOT NULL,
    FOREIGN KEY (id_utilizador) REFERENCES utilizador(id_utilizador)
);

-- 7. Tabela de Linha de Movimento --
CREATE TABLE IF NOT EXISTS linha_movimento(
    id_linha INT AUTO_INCREMENT PRIMARY KEY,
    id_movimento INT NOT NULL,
    id_produto INT NOT NULL,
    FOREIGN KEY (id_movimento) REFERENCES movimento(id_movimento),
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);