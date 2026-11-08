CREATE TABLE livro (
	codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
	titulo VARCHAR(50) NOT NULL,
	autor VARCHAR(50) NOT NULL,
	descricao TINYTEXT NOT NULL,
	tipo VARCHAR(30) NOT NULL,
	quantidade_paginas INT NOT NULL,
	isbn VARCHAR(20) NOT NULL,
	origem VARCHAR(20) NOT NULL,
	valor DECIMAL(10,2) NOT NULL,
	comissao DECIMAL(10,2) NOT NULL,
	quantidade_estoque INT NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO livro (titulo , autor, descricao, tipo, quantidade_paginas, isbn, origem, valor, comissao, quantidade_estoque) 
VALUES ('12 passos e 12 tradicoes', 'Bill W', 'Como trabalhar os 12 passos', 'LIVRO', 123, '1234567890123', 'INTERNACIONAL', 45.00, 20, 200);