-- =========================
-- 1. Criação das Tabelas
-- =========================

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
    quantidade INT NOT NULL,
    FOREIGN KEY (id_movimento) REFERENCES movimento(id_movimento),
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

-- =========================
-- 2. Dados de Teste
-- =========================

-- Inserir Utilizadores
INSERT IGNORE INTO utilizador (username, password, perfil) VALUES
    ('admin', 'admin123', 'ADMIN'),
    ('operador', '1234', 'OPERADOR'),
    ('compras', '1234', 'COMPRAS');

-- Inserir Fornecedores
INSERT IGNORE INTO fornecedor (nome, nif, contacto) VALUES
    ('Worten Empresas', '501234567', 'empresas@worten.pt'),
    ('PCDIGA', '509876543', 'comercial@pcdiga.com');

-- Inserir Clientes
INSERT IGNORE INTO cliente (nome, nif) VALUES
    ('Universidade de Aveiro', '501461108'),
    ('Câmara Municipal', '506789123');

-- Inserir Produtos
INSERT IGNORE INTO produto (nome, descricao, preco_unitario, stock_minimo, id_fornecedor) VALUES
    ('Portátil Lenovo ThinkPad', 'Intel i5, 16GB RAM, 512GB SSD', 850.00, 2, 1),
    ('Rato Wireless Logitech', 'MX Master 3', 99.90, 5, 1),
    ('Monitor Dell 24"', 'IPS Full HD', 180.00, 3, 2),
    ('Cabo HDMI 2m', 'Cabo banhado a ouro', 10.00, 20, 2),
    ('Macbook Air', 'M4', 1024.00, 15, 1);

-- Inserir Stock Inicial (Simulação)
INSERT IGNORE INTO stock (id_produto, quantidade) VALUES
    (1, 10), -- 10 Portáteis
    (2, 50), -- 50 Ratos
    (3, 1),  -- 1 Monitor (ALERTA: Abaixo do mínimo de 3!)
    (4, 100), -- 100 Cabos
    (5, 15); -- 15 Portáteis
