CREATE TABLE cliente (
	codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	tipo_pessoa VARCHAR(15) NOT NULL,
	cpf_cnpj VARCHAR(30),
	telefone VARCHAR(20),
	email VARCHAR(30) NOT NULL,
	logradouro VARCHAR(30),
	numero VARCHAR(10),
	complemento VARCHAR(20),
	cep VARCHAR(15),	
	codigo_cidade BIGINT,
	FOREIGN KEY (codigo_cidade) REFERENCES cidade(codigo)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;