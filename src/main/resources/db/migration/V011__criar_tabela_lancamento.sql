CREATE TABLE lancamento (
	codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(255) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
	observacao TINYTEXT,
	tipo VARCHAR(20) NOT NULL,
	categoria_codigo BIGINT NOT NULL,
	usuario_codigo BIGINT NOT NULL,
	FOREIGN KEY (categoria_codigo) REFERENCES categoria(codigo),
	FOREIGN KEY (usuario_codigo) REFERENCES usuario(codigo)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, categoria_codigo, usuario_codigo)
VALUES ('Planilha nivelada', '2023-07-28', '2023-08-10', 120.00, null, 'DESPESA', 2, 1);