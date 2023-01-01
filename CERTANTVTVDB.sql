create database certantvtv;
use certantvtv;

CREATE TABLE dueños(
	id int not null auto_increment,
    nombre varchar(255),
    dni int not null,
    exento bool not null,
    PRIMARY KEY (id)
);
SELECT * FROM dueños;

CREATE TABLE vehiculos (
	id int not null auto_increment,
    marca varchar(255) not null,
    modelo varchar(255) not null,
    dominio varchar(255) not null,
    dueño int not null,
    primary key(ID),
    foreign key(dueño) references dueños(id)
);
SELECT * FROM vehiculos;

CREATE TABLE inspectores (
	id int not null auto_increment,
    nombre varchar(255),
    PRIMARY KEY (id)
);
SELECT * FROM inspectores;

CREATE TABLE observaciones (
	id int not null auto_increment,
    luces varchar(255) not null,
    patente varchar(255) not null,
    espejos varchar(255) not null,
    chasis varchar(255) not null,
    vidrios varchar(255) not null,
    seguridad_y_emergencia varchar(255) not null,
    primary key (id)
);
SELECT * FROM observaciones;

CREATE TABLE mediciones (
	id int not null auto_increment,
    suspension varchar(255) not null,
    dirección_y_tren_delantero varchar(255) not null,
    frenos varchar(255) not null,
    contaminacion varchar(255) not null,
    primary key (id)
);
SELECT * FROM mediciones;

CREATE TABLE inspecciones (
	id int not null auto_increment,
    fecha DATE not null,
    estado varchar(255) not null,
    inspector int not null,
    vehiculo int not null,
    observacion int not null,
    medicion int not null,
    primary key(id),
    foreign key(inspector) references inspectores(id),
    foreign key(vehiculo) references vehiculos(id),
    foreign key(observacion) references observaciones(id),
    foreign key(medicion) references mediciones(id)
);



INSERT INTO dueños (nombre, dni, exento) 
VALUES 
	('Armando Oscar González', 00000001, true),
	('José Gustavo Sand', 00000002, false),
	('Diego Hernán Valeri', 00000003, true),
	('Lautaro Germán Acosta', 00000004, false),
	('Gilmar Gilberto Villagrán Seger', 00000005, false),
	('Juan Héctor Guidi', 00000006, true);
SELECT * FROM dueños;

INSERT INTO vehiculos (marca, modelo, dominio, dueño) 
VALUES 
	('Ford', 'Escort', 'AAA001', 1),
	('Ford', 'Sierra', 'AAA002', 1),
	('Toyota', 'Hilux', 'AAA003', 2),
	('Ford', 'Escort', 'AAA004', 1),
	('Ford', 'Sierra', 'AAA005', 1),
	('Volvo', '850', 'AAA006', 3),
	('Renault', '9', 'AAA007', 4),
	('Fiat', '600', 'AAA008', 5),
	('Fiat', 'Fiorino', 'AAA009', 5),
	('Chevrolet', 'Chevy', 'AAA010', 6),
	('Renault', 'Torino', 'AAA011', 6);
SELECT * FROM vehiculos;

INSERT INTO inspectores(nombre)
VALUES
	('Héctor Raúl Cúper'),
    ('Ramón Armando Cabrero Muñiz'),
    ('Guillermo Barros Schelotto'),
    ('Jorge Francisco Almirón Quintana');
SELECT * FROM inspectores;

INSERT INTO observaciones(luces , patente, espejos, chasis, vidrios, seguridad_y_emergencia)
VALUES 
	('apto', 'apto', 'apto', 'apto', 'apto', 'apto'),
    ('condicional', 'apto', 'apto', 'apto', 'apto', 'apto'),
    ('apto', 'condicional', 'apto', 'apto', 'apto', 'apto'),
    ('apto', 'apto', 'apto', 'condicional', 'apto', 'apto'),
    ('apto', 'apto', 'apto', 'apto', 'condicional', 'apto'),
    ('apto', 'apto', 'apto', 'apto', 'apto', 'condicional'),
    ('rechazado', 'apto', 'apto', 'apto', 'apto', 'apto'),
    ('apto', 'rechazado', 'apto', 'apto', 'apto', 'apto'),
    ('apto', 'apto', 'rechazado', 'apto', 'apto', 'apto'),
    ('apto', 'apto', 'apto', 'rechazado', 'apto', 'apto'),
    ('apto', 'apto', 'apto', 'apto', 'rechazado', 'apto'),
    ('apto', 'apto', 'apto', 'apto', 'apto', 'rechazado'),
    ('condicional', 'condicional', 'condicional', 'condicional', 'condicional', 'condicional'),
    ('rechazado', 'rechazado', 'rechazado', 'rechazado', 'rechazado', 'rechazado'),
    ('apto', 'rechazado', 'apto', 'rechazado', 'apto', 'apto');
SELECT * FROM observaciones;

INSERT INTO mediciones(suspension, dirección_y_tren_delantero, frenos, contaminacion)
VALUES
	('apto', 'apto', 'apto', 'apto'),
    ('condicional', 'apto', 'apto', 'apto'),
    ('apto', 'condicional', 'apto', 'apto'),
    ('apto', 'apto', 'condicional', 'apto'),
    ('apto', 'apto', 'apto', 'condicional'),
    ('rechazado', 'apto', 'apto', 'apto'),
    ('apto', 'rechazado', 'apto', 'apto'),
    ('apto', 'apto', 'rechazado', 'apto'),
	('apto', 'apto', 'apto', 'rechazado'),
    ('condicional', 'condicional', 'condicional', 'condicional'),
    ('rechazado', 'rechazado', 'rechazado', 'rechazado');
SELECT * FROM mediciones;


INSERT INTO inspecciones(fecha, estado, inspector, vehiculo, observacion, medicion)
VALUES
	('2022-12-10', 'rechazado', 1, 1, 1, 11), /*bochado*/
    ('2021-01-05', 'rechazado', 1, 2, 7, 1), /*bochado y vencido*/
    ('2022-12-10', 'condicional', 1, 3, 2, 1), /*condicional*/
    ('2021-01-05', 'condicional', 2, 4, 1, 2), /*codicional y vencido*/
    ('2022-10-13', 'apto', 2, 5, 1, 1), /*apto*/
    ('2021-01-03', 'apto', 2, 6, 1, 1), /*apto y vencido*/
    ('2020-10-17', 'apto', 2, 7, 1, 1), /*apto, vencido que va a volver*/
    ('2022-12-29', 'condicional', 3, 8, 3, 3), /*condicional que vuelve en el día (Hoy es 29 de Diciembre del 2022)*/
    ('2022-10-15', 'condicional', 3, 9, 4, 3), /*condicional que vuelve otro día*/
    ('2020-11-15', 'condicional', 3, 10, 1, 5), /*condicional, vencido que vuelve otro día*/
    ('2022-12-29', 'rechazado', 3, 11, 9, 1), /*rechazado que vuelve en el día*/
    ('2022-12-29', 'apto', 2, 7, 1, 1), /* vinieron todos hoy*/
    ('2022-12-29', 'apto', 4, 8, 1, 1),
    ('2022-12-29', 'apto', 4, 9, 1, 1),
    ('2022-12-29', 'apto', 4, 10, 1, 1),
    ('2022-12-29', 'rechazado', 2, 11, 8, 5),
	('2022-12-29', 'rechazado', 3, 11, 9, 10);
SELECT * FROM inspecciones;


SELECT v.*, d.nombre AS titular, d.dni, d.exento, i.*, ins.nombre AS nombre_inspector, o.*, m.*
FROM vehiculos v 
	INNER JOIN dueños d 
		ON v.dueño = d.id
    INNER JOIN inspecciones i 
		ON v.id = i.vehiculo
	INNER JOIN inspectores ins
		ON i.inspector = ins.id
	INNER JOIN observaciones o
		ON i.observacion = o.id 
	INNER JOIN mediciones m
		ON i.medicion = m.id
	ORDER BY i.fecha desc;

SELECT v.marca, v.modelo, v.dominio, d.nombre, i.estado, MAX(i.fecha) as fecha, v.id
FROM vehiculos v
	INNER JOIN dueños d
		ON v.dueño = d.id
	INNER JOIN inspecciones i 
		ON v.id = i.vehiculo
	GROUP BY v.dominio
    HAVING i.estado = 'condicional';

SELECT * FROM inspectores i WHERE i.id = 12031;

SELECT obs.id FROM observaciones obs WHERE obs.luces = 'apto' AND obs.patente = 'rechazado' AND obs.espejos = 'apto' 
AND obs.chasis = 'apto' AND obs.vidrios = 'apto' AND obs.seguridad_y_emergencia = 'apto';
