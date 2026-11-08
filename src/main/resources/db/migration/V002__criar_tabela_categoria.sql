CREATE TABLE categoria (
	codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO categoria (nome) VALUES ('7 tradicao');
INSERT INTO categoria (nome) VALUES ('Água');
INSERT INTO categoria (nome) VALUES ('Luz');