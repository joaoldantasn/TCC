INSERT INTO tb_usuario(nome, email, senha, telefone, altura, peso, active, role) VALUES ('João Lucas', 'joao@gmail.com','$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', '+1234567890', 1.91,90.0, true, 'ROLE_ALUNO');
INSERT INTO tb_usuario(nome, email, senha, telefone, altura, peso, active, role) VALUES ('Roberta Silva', 'betinha@gmail.com','$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', '+1234567890', 1.65,75.0, true, 'ROLE_ALUNO');
INSERT INTO tb_usuario(nome, email, senha, telefone, altura, peso, active, role) VALUES ('Luana Dantas', 'luana@gmail.com','$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', '+1234567890', 1.65,56.0, true, 'ROLE_ALUNO');
INSERT INTO tb_usuario(nome, email, senha, telefone, altura, peso, active, role) VALUES ('John Doe', 'john@gmail.com','$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', '+1234567890', 1.65,56.0, true, 'ROLE_PROFESSOR');

INSERT INTO tb_desafio(nome, descricao, created_at, start_at, end_at, tipo, premiacao, usuario_criador_id, finalizado) VALUES ('desafio 1', 'perder peso, so vale marcar cardio',  '2024-01-01',  '2024-02-01', '2024-03-01', 'CARDIO', 'kit da academia', 4, false);
INSERT INTO tb_desafio(nome, descricao, created_at, start_at, end_at, tipo, premiacao, usuario_criador_id, finalizado) VALUES ('desafio 2', 'constancia, marcar o dia que veio para academia',  '2024-01-01',  '2024-02-01', '2024-03-01', 'PRESENCA', 'um mês de academia grátis', 4, false);
INSERT INTO tb_desafio(nome, descricao, created_at, start_at, end_at, tipo, premiacao, usuario_criador_id, finalizado) VALUES ('desafio 3', 'so vale dia de perna',  '2024-01-01',  '2024-02-01', '2024-03-01', 'EXERCICIOS', 'garrafa da academia', 4, false);

INSERT INTO tb_exercicio(nome) VALUES ('01 - Cardio')
INSERT INTO tb_exercicio(nome) VALUES ('02 - Supino Inclinado')
INSERT INTO tb_exercicio(nome) VALUES ('02 - Supino Reto')
INSERT INTO tb_exercicio(nome) VALUES ('02 - Crossover')
INSERT INTO tb_exercicio(nome) VALUES ('02 - Pullover')
INSERT INTO tb_exercicio(nome) VALUES ('03 - Triceps Corda')
INSERT INTO tb_exercicio(nome) VALUES ('03 - Triceps Máquina')
INSERT INTO tb_exercicio(nome) VALUES ('03 - Triceps testa')

INSERT INTO tb_treino(usuario_id, desafio_id, descricao, nome, data_treino, pontuacao) VALUES (1, 1, 'corri 5km na esteira', 'Cardio', '2024-02-12', 5)
INSERT INTO tb_treino(usuario_id, desafio_id, descricao, nome, data_treino, pontuacao) VALUES (1, 2, 'treinin monstro', 'treino de peito', '2024-02-12', 1)
INSERT INTO tb_treino(usuario_id, desafio_id, descricao, nome, data_treino, pontuacao) VALUES (2, 1, 'corri 3km na esteira', 'Cardio', '2024-02-12', 3)

INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (1, 1);
INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (1, 2);
INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (2, 1);
INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (4, 1);
INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (4, 2);
INSERT INTO tb_desafio_usuarios (usuario_id, desafio_id) VALUES (4, 3);

INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 1);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 1);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 1);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 1);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 1);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (8, 2);

INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 3);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 3);
INSERT INTO tb_exercicio_treino (exercicio_id, treino_id) VALUES (1, 3);
