INSERT INTO permissao (codigo, nome) VALUES (1, 'ROLE_CADASTRAR_CIDADE');
INSERT INTO permissao (codigo, nome) VALUES (2, 'ROLE_CADASTRAR_USUARIO');
INSERT INTO permissao (codigo, nome) VALUES (3, 'ROLE_CANCELAR_VENDA');
INSERT INTO permissao (codigo, nome) VALUES (4, 'ROLE_DELETAR_CIDADE');
INSERT INTO permissao (codigo, nome) VALUES (5, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao (codigo, nome) VALUES (6, 'ROLE_DELETAR_CATEGORIA');
INSERT INTO permissao (codigo, nome) VALUES (7, 'ROLE_CADASTRAR_CLIENTE');
INSERT INTO permissao (codigo, nome) VALUES (8, 'ROLE_DELETAR_CLIENTE');
INSERT INTO permissao (codigo, nome) VALUES (9, 'ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permissao (codigo, nome) VALUES (10, 'ROLE_DELETAR_LANCAMENTO');
INSERT INTO permissao (codigo, nome) VALUES (11, 'ROLE_CADASTRAR_LIVRO');
INSERT INTO permissao (codigo, nome) VALUES (12, 'ROLE_DELETAR_LIVRO');
INSERT INTO permissao (codigo, nome) VALUES (13, 'ROLE_ASSOCIAR_GRUPO_PERMISSAO');

INSERT INTO usuario_grupo (codigo_usuario, codigo_grupo) VALUES (
	(SELECT codigo FROM usuario WHERE email = 'ricbapdevs@gmail.com'), 1);

INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 1);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 2);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 3);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 4);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 5);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 6);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 7);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 8);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 9);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 10);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 11);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 12);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 13);

INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 1);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 4);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 5);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 6);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 7);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 8);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 11);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (2, 12);

INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 1);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 4);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 7);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 8);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 11);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (3, 12);

INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (4, 2);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (4, 5);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (4, 6);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (4, 9);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (4, 10);
