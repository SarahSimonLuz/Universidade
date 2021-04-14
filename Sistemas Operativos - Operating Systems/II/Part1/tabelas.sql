CREATE TABLE utilizador(
	id integer
);

CREATE TABLE necessidades(
	id integer,
	produto varchar(30),
	idnecessidades serial
);

CREATE TABLE localproduto(
	loja varchar(30),
	produto varchar(30)
);