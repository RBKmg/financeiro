CREATE TABLE usuario (
	codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(120) NOT NULL,
	ativo BOOLEAN DEFAULT TRUE NOT NULL,
	data_nascimento DATE
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE permissao (
	codigo BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE grupo (
	codigo BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE usuario_grupo (
	codigo_usuario BIGINT NOT NULL,
	codigo_grupo BIGINT NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_grupo),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE grupo_permissao (
	codigo_grupo BIGINT NOT NULL,
	codigo_permissao BIGINT NOT NULL,
	PRIMARY KEY (codigo_grupo, codigo_permissao),
	FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;