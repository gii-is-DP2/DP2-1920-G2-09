-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,email,enabled) VALUES ('admin1','4dm1n','admin@gmail.com',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,email,enabled) VALUES ('owner1','0wn3r','alejandrobp99@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');

INSERT INTO users(username,password,email,enabled) VALUES ('owner2','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner2','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner3','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner3','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner4','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner4','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner5','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner5','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner6','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner6','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner7','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner7','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner8','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner8','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner9','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner9','owner');
INSERT INTO users(username,password,email,enabled) VALUES ('owner10','0wn3r','owner@gmail.com',TRUE);
INSERT INTO authorities VALUES ('owner10','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,email,enabled) VALUES ('vet1','v3t','vet1@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');
-- Uno de prueba --
INSERT INTO users(username,password,email,enabled) VALUES ('prueba','prueba','fraromgon2@gmail.com',TRUE);
INSERT INTO authorities VALUES ('prueba','owner');

INSERT INTO users(username,password,email,enabled) VALUES ('vet2','v3t','vet2@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet2','veterinarian');

INSERT INTO users(username,password,email,enabled) VALUES ('vet3','v3t','vet3@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet3','veterinarian');

INSERT INTO users(username,password,email,enabled) VALUES ('vet4','v3t','vet4@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet4','veterinarian');

INSERT INTO users(username,password,email,enabled) VALUES ('vet5','v3t','vet5@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet5','veterinarian');

INSERT INTO users(username,password,email,enabled) VALUES ('vet6','v3t','vet6@gmail.com',TRUE);
INSERT INTO authorities VALUES ('vet6','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter','vet1');
INSERT INTO vets VALUES (2, 'Helen', 'Leary','vet2');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas','vet3');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega','vet4');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens','vet5');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins','vet6');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'Alejandro', 'Blanco', '110 W. Liberty St.', 'Madison',null,null,null,null, '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie',null,null,null,null, '6085551749', 'owner2');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland',null,null,null,null, '6085558763', 'owner3');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor',null,null,null,null, '6085553198', 'owner4');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison',null,null,null,null, '6085552765', 'owner5');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona',null,null,null,null, '6085552654', 'owner6');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona',null,null,null,null, '6085555387', 'owner7');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison',null,null,null,null, '6085557683', 'owner8');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison',null,null,null,null, '6085559435', 'owner9');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee',null,null,null,null, '6085555487', 'owner10');
INSERT INTO owners VALUES (11, 'Francisco', 'Arroyo', 'Reina Mercedes', 'Sevilla','1111222233334444','123',12,2090, '692345123', 'prueba');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
-- INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 11);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

--- LOS NUEVOS A PARTIR DE AQUI ---
INSERT INTO product VALUES (1,'Gel de perro',true,1,'Es un gel de perro',10,10.20,'https://tinyurl.com/vp4wlrz');
INSERT INTO product VALUES (2,'Gel de gato',true,2,'Es un gel de gato',150,102.00,'https://tinyurl.com/vp4wlrz');
INSERT INTO product VALUES (3,'Gel de caballo',false,3,'Es un gel de caballo',0,152.54,'https://tinyurl.com/vp4wlrz');

INSERT INTO prescriptions(id,name,description,pet_id,vet_id,date_inicio,date_final) VALUES (1, 'Tratamiento', 'Tomar paracetamol 2 semanas', 1, 1, '2020-10-15', '2020-10-20');
INSERT INTO prescriptions(id,name,description,pet_id,vet_id,date_inicio,date_final) VALUES (2,'Titulo de la nueva prescripción','Descripción de la nueva descripción',13,1,'2013-01-01','2013-01-01');
INSERT INTO prescriptions(id,name,description,pet_id,vet_id,date_inicio,date_final) VALUES (3,'jajajaja','descripcionjajajaja',1,1,'2010-03-09','2010-03-19');

INSERT INTO product_coment VALUES (1,'Descripción de prueba 1','2013-01-04','Titulo de prueba',false,3,1,'prueba');
INSERT INTO product_coment VALUES (2,'Descripción de prueba 2','2013-01-02','Titulo de prueba2',true,3,1,'vet1');
--- WALKS ---

INSERT INTO walks VALUES (1,'Primer Paseo', 'Esto es un paseo1','https://tinyurl.com/wygb5vu');
INSERT INTO walks VALUES (2,'Segundo Paseo', 'Esto es un paseo2','https://tinyurl.com/wygb5vu');
INSERT INTO walks VALUES (3,'Tercer Paseo', 'Esto es un paseo3','https://tinyurl.com/wygb5vu');

INSERT INTO walk_coment(id,title,description,post_date,rating,username,walk_id) VALUES (1,'Leo','descripción','2010-09-07',2,'prueba', 1);
INSERT INTO walk_coment(id,title,description,post_date,rating,username,walk_id) VALUES (2,'BUEN PASEO','descripción del paseo','2019-09-07',5,'owner1', 2);
INSERT INTO walk_coment(id,title,description,post_date,rating,username,walk_id) VALUES (3,'BUEN PASEO PERO PODRÍA SER MEJOR','descripción del paseo nueva','2019-09-07',5,'owner2', 2);

INSERT INTO shopping_cart(id,owner_id) VALUES (1,11);
INSERT INTO shopping_cart(id,owner_id) VALUES (2,10);
INSERT INTO shopping_cart(id,owner_id) VALUES (3,9);
INSERT INTO shopping_cart(id,owner_id) VALUES (4,8);
INSERT INTO shopping_cart(id,owner_id) VALUES (5,7);
INSERT INTO shopping_cart(id,owner_id) VALUES (6,6);
INSERT INTO shopping_cart(id,owner_id) VALUES (7,5);
INSERT INTO shopping_cart(id,owner_id) VALUES (8,4);
INSERT INTO shopping_cart(id,owner_id) VALUES (9,3);
INSERT INTO shopping_cart(id,owner_id) VALUES (10,2);
INSERT INTO shopping_cart(id,owner_id) VALUES (11,1);


INSERT INTO item(id, shopping_cart_id,product_id,quantity,unit_price) VALUES (1, 1, 1, 5, 10.20);
INSERT INTO item(id, shopping_cart_id,product_id,quantity,unit_price) VALUES (2, 1, 2, 50, 102.00);
INSERT INTO item(id, shopping_cart_id,product_id,quantity,unit_price) VALUES (3, 11, 3, 10, 152.54);


