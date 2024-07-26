----USER_LEVEL----
INSERT INTO public.userlevel(levelname, leveldescription) VALUES
    ('SILVER', 'Nivel inicial para nuevos usuarios'),
	('GOLD', 'Usuarios que han realizado compras regulares'),
	('PREMIUM', 'Usuarios que han realizado compras frecuentes y significativas');


----ROOM_TYPE----
INSERT INTO public.roomtype (roomtypename,roomtypedescription) VALUES
	 ('Departamento vista al río con habitacion matrimonial','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón'),

	 ('Departamento vista al río con habitacion doble','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón'),

	 ('Departamento con vista al jardín con habitacion matrimonial','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón'),

	 ('Departamento con vista al jardín con habitacion doble','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón'),

	 ('Departamento con vista doble con habitacion matrimonial','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón'),

	 ('Departamento con vista doble con habitacion doble','Amplio departamento que cuenta con :
- Una kitchenette totalmente equipada, incluyendo cubiertos, utensilios de cocina y frigobar.
- La sala está equipada con un televisor de 55 pulgadas y tiene acceso a un balcón.
- El departamento cuenta con dos dormitorios, cada uno con su propio baño completo. Uno de los dormitorios tiene dos camas matrimoniales y un armario, mientras que el otro cuenta con una cama queen, armario y también acceso al balcón');


----STATE_ROOM----
INSERT INTO public.stateroom (stateroomname) VALUES
	 ('LIMPIANDO'),
	 ('LISTO PARA LLEGADA'),
	 ('OCUPADO HUESPED'),
	 ('EN MANTENIMIENTO'),
	 ('REQUIERE LIMPIEZA PROFUNDA'),
	 ('EN RENOVACION'),
	 ('LISTO PARA OCUPAR'),
	 ('SIN DISPONIBILIDAD'),
	 ('SIN MANTENIMIENTO'),
	 ('RESERVADO');


----ROOM_DETAIL----
INSERT INTO public.roomdetail (information,terms,bedrooms,squaremeters,oceanviewbalcony,balconyoverlookingpool) VALUES
	 ('Departamento con vista doble con habitacion matrimonial ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'72.13',true,true),
	 ('Departamento con vista al jardín con habitacion doble ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'72.12',false,true),
	 ('Departamento vista al río con habitacion doble ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'80.04',true,false),
	 ('Departamento con vista al jardín con habitacion doble ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'81.5',false,true),
	 ('Departamento con vista doble con habitacion doble ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'64',true,true),
	 ('Departamento con vista al jardín con habitacion matrimonial ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'63.53',false,true),
	 ('Departamento vista al río con habitacion matrimonial ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'63.23',true,false),
	 ('Departamento con vista al jardín con habitacion matrimonial ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'71.79',false,true),
	 ('Departamento vista al río con habitacion matrimonial ','La cancelacion de reserva aplica 48 horas (2 dias) antes de la llegada.
La tarifa  NO es REEMBOLSABLE',2,'71.22',true,false);


----ROOM----
INSERT INTO public.room (roomtypeid,stateroomid,roomdetailid,roomname,roomnumber,image,capacity) VALUES
	 (1,2,2,'Departamento con vista al río','202','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/feb6ae4a-75dd-4db9-8d31-a0ba9a462c22-resized_IMG_7568.jpg',2),
	 (1,2,2,'Departamento con vista al río','302','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/30ff668b-ee83-4d4a-814d-2904352e3573-resized_IMG_7593.jpg',2),
	 (1,2,4,'Departamento con vista al río','204','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/909b1d71-a92b-4a6e-bce9-6cbc1cac6574-resized_IMG_7577.jpg',2),
	 (2,2,4,'Departamento con vista al río','304','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/78f3ee93-8845-41d3-a0c8-1063d014507b-resized_IMG_7567.jpg',4),
	 (2,2,6,'Departamento con vista al río','206','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/a8b6adaa-4191-4a17-ba04-e46a134aef96-resized_IMG_7582.jpg',4),
	 (2,2,6,'Departamento con vista al río','306','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/909b1d71-a92b-4a6e-bce9-6cbc1cac6574-resized_IMG_7577.jpg',4),

	 (3,2,1,'Departamento con vista al jardín','201','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/080b7545-d1a1-415b-9f94-79d1699d017c-compressed_IMG_1384.JPEG',2),
	 (3,2,1,'Departamento con vista al jardín','301','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/a8b6adaa-4191-4a17-ba04-e46a134aef96-resized_IMG_7582.jpg',2),
	 (3,2,3,'Departamento con vista al jardín','203','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/56dc5341-1f57-4729-b4d2-03101b0ccb6e-resized_IMG_7534.JPG',2),
	 (3,2,3,'Departamento con vista al jardín','303','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/77bb88db-250d-47ce-bd4a-fea99328c43e-resized_IMG_7535.jpg',2),
	 (4,2,5,'Departamento con vista al jardín','205','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/d06fee02-dc0b-44c8-81e7-1e0f2499b78b-resized_IMG_7498.jpg',4),
	 (4,2,5,'Departamento con vista al jardín','305','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/e5ce87b8-5903-46c9-805b-583f9247173e-IMG_1425.JPEG',4),
	 (4,2,7,'Departamento con vista al jardín','307','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/d0427e0c-c636-4e77-89bb-258eb9d3a7e6-IMG_1426.JPEG',4),
	 (4,2,7,'Departamento con vista al jardín','207','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/ba64568f-a82e-4a8b-a71e-c3886e7c67d4-resized_IMG_7531.JPG',4),

	 (5,2,8,'Departamento con vista doble','208','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/78f3ee93-8845-41d3-a0c8-1063d014507b-resized_IMG_7567.jpg',2),
	 (5,2,8,'Departamento con vista doble','308','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/67abc9bf-d956-4646-8904-9d03f364247c-resized_IMG_7492.jpg',2),
	 (6,2,9,'Departamento con vista doble','309','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/7a3e8902-7b84-4353-8c80-4070af79d4b2-resized_IMG_7522.JPG',4),
	 (6,2,9,'Departamento con vista doble','209','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/ef6104fe-b20b-4765-806e-dea8ec6a7a76-compressed_IMG_1436.JPEG',4);


----OFFER_TYPE----
INSERT INTO public.offertype (offertypename,offertypedescription) VALUES
	 ('Departamento vista piscina',''),
	 ('Departamento vista jardín',''),
	 ('Departamento vista rio','');


----ROOM_OFFER----
INSERT INTO public.roomoffer (roomid,offertypeid,"cost",offertimeinit,offertimeend,offername,riberapoints,inresortpoints,points) VALUES
	 (1 ,1,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,3,3,3),
	 (2 ,2,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,5,4),
	 (3 ,3,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,6,7,6),

	 (4 ,1,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,8,10,8),
	 (5 ,2,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,4,3),
	 (6 ,3,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,3,3,3),

	 (7 ,1,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,6,7,5),
	 (8 ,2,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),
	 (9,3,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),
	 (10,1,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),

	 (11,2,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,5,4),
	 (12,1,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (13,2,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (14,3,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),

	 (15,1,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (16,2,225.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),

	 (17,3,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (18,1,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5);

----PAYMENT_METHOD----
INSERT INTO public.paymentmethod (description,state) VALUES
	 ('Tarjeta de Credito y Debito','ACTIVE'),
	 ('Pago con Transferencia Bancaria','ACTIVE'),
	 ('Codigo QR usando tu billetera electronica','ACTIVE'),
	 ('Pago con Yape','ACTIVE');


----PAYMENT_TYPE----
INSERT INTO public.paymenttype (paymenttypedesc,countryid,paymentmethodid) VALUES
	 ('BCP',1,1),
	 ('INTERBANK',1,1),
	 ('Otros medios',1,1);

----PAYMENT_SUBTYPE----
INSERT INTO public.paymentsubtype (paymentsubtypedesc,accountsoles,accountdollars,paymenttypeid,soles,dollars,percentage,statussoles,statusdollars) VALUES
	 ('Ventanilla Lima','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Agente Provincia','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Agente Lima','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Banca Movil','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Banca por Internet','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Cuenta BCP Yape','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Ventanilla Provincia','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Cajero Lima','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Cajero Provincia','19390445695-0-85','191-2616687-1-90',1,100.0,0.0,10.0,1,0),
	 ('Cajero Provincia','191-2606708-0-82','191-2616687-1-90',1,100.0,0.0,10.0,1,0);

----gender----
INSERT INTO public.gender (genderdesc) VALUES
	 ('MASCUINO'),
     ('FEMENINO');

----transactioncategory----
INSERT INTO public.transactioncategory (transactioncategoryid, name) VALUES
	 (1, 'Pago');

----termsversion----
INSERT INTO public.termsversion (s3url, createdat) VALUES
	 ('https://www.google.com', now());

----refusereason----
INSERT INTO public.refusereason (refusereasonname) VALUES
	 ('Sin rechazo'),
	 ('La imagen o captura esta borrosa. No se visualiza'),
	 ('El codigo de operacion es invalido o no existe');

----REGISTER_TYPE----
INSERT INTO public.registertype (registertypename) VALUES
	 ('MANUAL'),
	 ('GOOGLE'),
	 ('USUARIO');

----pay me state----
INSERT INTO public.paymentstate (paymentstatename) VALUES
	 ('PENDIENTE'),
	 ('ACEPTADO'),
     ('RECHAZADO'),
     ('ANULADO');

----BOOKING_STATUS----
INSERT INTO public.bookingstate (bookingstatename) VALUES
	 ('RECHAZADO'),
	 ('ACEPTADO'),
	 ('PENDIENTE'),
	 ('ANULADO'),
	 ('FINALIZADO'),
	 ('EN USO');


----COMFORT----
INSERT INTO public.comforttype (comforttypename,comforttypedescription,active) VALUES

---Habitacion matrimonial
	 ('Cama Queen',NULL,true),
	 ('Juego de sabanas para cama queen',NULL,true),
	 ('Cobertor para cama queen',NULL,true),
	 ('Almohadas',NULL,true),
	 ('Fundas de almohadas',NULL,true),
	 ('Duvet o plumoon queen',NULL,true),
	 ('Pie de cama',NULL,true),
	 ('Telefono',NULL,true),
	 ('Televisor  (43" pulgadas)',NULL,true),
---Baño de habitacion doble/duplex
---Baño de habitacion matrimonial
	 ('Secadora',NULL,true),
	 ('Toalla blanca de cuerpo',NULL,true),
	 ('Toalla blanca de cara',NULL,true),
	 ('Toalla azul de pisicna',NULL,true),
	 ('Dispensadores de jabon',NULL,true),
	 ('Dispensador de shampoo',NULL,true),
---Habitacion Doble / Duplex
	 ('Cama de 2 plazas',NULL,true),
	 ('Juego de sabanas para cama de 2 plazas',NULL,true),
	 ('Cobertor para cama de 2 plazas',NULL,true),
	 ('Lampara',NULL,true),

---Kitchenette
	 ('Frigobar (con bebidas)',NULL,true),
	 ('Microondas',NULL,true),
	 ('Waflera',NULL,true),
	 ('Vinera',NULL,true),
	 ('Sillas de madera alta',NULL,true),
	 ('Dispensador de lava vajilla',NULL,true),
	 ('Esponja lava vajilla',NULL,true),
	 ('Mueble sofa cama',NULL,true),
---Menaje
	 ('Cubiertos ( cuchara, cucharita, tenedor, cuchillo)',NULL,true),
	 ('Copas de vino',NULL,true),
	 ('Saca corcho',NULL,true),
	 ('Tazas',NULL,true),
	 ('Platos tendidos grande',NULL,true),
	 ('Platos de tendido mediano',NULL,true),
	 ('Platos hondos',NULL,true),
	 ('Platos de taza',NULL,true),
---Sala
	 ('Mesa de centro de ceramico grande',NULL,true),
	 ('Mesa de centro de ceramico pequeña',NULL,true),
	 ('Televisor (55" pulgadas )',NULL,true),
	 ('Telefono (intercominicador)',NULL,true),
	 ('Luminarias colgantes',NULL,true),
	 ('Macetas con plantas',NULL,true),
---Amenities de frigobar
	 ('Jugo en caja',NULL,true),
	 ('Energizante',NULL,true),
	 ('Cervezas',NULL,true),
	 ('Botella de agua',NULL,true),
	 ('Botella de vino',NULL,true),
	 ('Botella de champagne',NULL,true);

----IMPLEMENTS_APARTMENT----
INSERT INTO public.implements_apartment(implements_apartment_name) VALUES
	('Habitacion matrimonial'),
	('Baño de habitacion matrimonial'),
	('Habitacion Doble / Duplex'),
	('Baño de habitacion doble/duplex'),
	('Kitchenette'),
	('Menaje'),
	('Sala'),
	('Amenities de frigobar');

----BEDS_TYPE----
INSERT INTO public.bedstype (bedtypename,bedtypedescription) VALUES
	 ('King','Cama King-size para una experiencia de lujo y espacio amplio para dormir cómodamente.'),
	 ('Queen','Cama Queen-size, un estándar de confort en la mayoría de los hoteles, ideal para parejas.'),
	 ('Double','Cama Doble, ofreciendo espacio adicional para una persona que prefiere más espacio.'),
	 ('Twin','Dos camas individuales, perfectas para amigos o familiares que prefieren dormir separados.'),
	 ('Waterbed','Cama de agua que proporciona una sensación de flotación y alivio del estrés, perfecta para una experiencia de sueño relajante.');

----BEDROOM----
INSERT INTO public.bedroom (roomid,bedtypeid,quantity) VALUES
	 (1,2,1), (2,2,1), (3,2,1),
     (4,3,2), (5,3,2), (6,3,2),
     (7,2,1), (8,2,1), (9,2,1), (10,2,1),
	 (11,3,2), (12,3,2), (13,3,2), (14,3,2),
	 (15,2,1), (16,2,1),
	 (17,3,2), (18,3,2);

----COMFORT_SERVICE_DETAIL----
INSERT INTO public.implements_comfort(implements_apartment_id, comforttypeid, cuantity) VALUES
	(1, 1, 1),(1, 2, 1),(1, 3, 1),(1, 4, 4),(1, 5, 4),(1, 6, 1),(1, 7, 1),(1, 8, 1),(1, 9, 1),
	(2, 10, 1),(2, 11, 1),(2, 12, 1),(2, 13, 1),(2, 14, 2),(2, 15, 2),
	(3, 16, 2),(3, 17, 2),(3, 18, 2),(3, 19, 1),
	(4, 10, 1),(4, 11, 1),(4, 12, 1),(4, 13, 1),(4, 14, 2),(4, 15, 2),
	(5, 20, 1),(5, 21, 1),(5, 22, 1),(5, 23, 1),(5, 24, 6),(5, 25, 1),(5, 26, 1),(5, 27, 1),
	(6, 28, 6),(6, 29, 2),(6, 30, 1),(6, 31, 6),(6, 32, 6),(6, 33, 6),(6, 34, 6),(6, 35, 6),(6, 36, 6),
	(7, 37, 1),(7, 38, 1),(7, 39, 1),(7, 40, 1),(7, 41, 3),(7, 42, 2),
	(8, 43, 1),(8, 44, 1),(8, 45, 1),(8, 46, 1),(8, 47, 1),(8, 48, 1);

----COMFORT_ROOM_OFFER_DETAIL----
INSERT INTO public.comfortroomofferdetail (roomofferid,comforttypeid) VALUES
---Habitacion matrimonial
     (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),
     (2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),(2,11),(2,12),(2,13),(2,14),(2,15),
     (3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(3,9),(3,10),(3,11),(3,12),(3,13),(3,14),(3,15),

     (7,1), (7,2),(7,3),(7,4),(7,5),(7,6),(7,7),(7,8),(7,9),(7,10),(7,11),(7,12),(7,13),(7,14),(7,15),
     (8,1), (8,2),(8,3),(8,4),(8,5),(8,6),(8,7),(8,8),(8,9),(8,10),(8,11),(8,12),(8,13),(8,14),(8,15),
     (9,1), (9,2),(9,3),(9,4),(9,5),(9,6),(9,7),(9,8),(9,9),(9,10),(9,11),(9,12),(9,13),(9,14),(9,15),
     (10,1),(10,2),(10,3),(10,4),(10,5),(10,6),(10,7),(10,8),(10,9),(10,10),(10,11),(10,12),(10,13),(10,14),(10,15),

	 (15,1),(15,2),(15,3),(15,4),(15,5),(15,6),(15,7),(15,8),(15,9),(15,10),(15,11),(15,12),(15,13),(15,14),(15,15),
	 (16,1),(16,2),(16,3),(16,4),(16,5),(16,6),(16,7),(16,8),(16,9),(16,10),(16,11),(16,12),(16,13),(16,14),(16,15),
---Habitacion Doble / Duplex
     (4,4),(4,3),(4,8),(4,9),(4,16),(4,17),(4,18),(4,19),(4,10),(4,11),(4,12),(4,13),(4,14),(4,15),
     (5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,9),(5,10),(5,11),(5,12),(5,13),(5,14),(5,15),
     (6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(6,10),(6,11),(6,12),(6,13),(6,14),(6,15),

	 (11,3),(11,3),(11,8),(11,9),(11,16),(11,17),(11,18),(11,19),(11,10),(11,11),(11,12),(11,13),(11,14),(11,15),
	 (12,3),(12,3),(12,8),(12,9),(12,16),(12,17),(12,18),(12,19),(12,10),(12,11),(12,12),(12,13),(12,14),(12,15),
	 (13,3),(13,3),(13,8),(13,9),(13,16),(13,17),(13,18),(13,19),(13,10),(13,11),(13,12),(13,13),(13,14),(13,15),
	 (14,3),(14,3),(14,8),(14,9),(14,16),(14,17),(14,18),(14,19),(14,10),(14,11),(14,12),(14,13),(14,14),(14,15),

	 (17,3),(17,3),(17,8),(17,9),(17,16),(17,17),(17,18),(17,19),(17,10),(17,11),(17,12),(17,13),(17,14),(17,15),
	 (18,3),(18,3),(18,8),(18,9),(18,16),(18,17),(18,18),(18,19),(18,10),(18,11),(18,12),(18,13),(18,14),(18,15),


---Habitacion matrimonial
     (1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42),(1,43),(1,44),(1,45),(1,46),(1,47),
     (2,16),(2,17),(2,18),(2,19),(2,20),(2,21),(2,22),(2,23),(2,24),(2,25),(2,26),(2,27),(2,28),(2,29),(2,30),(2,31),(2,32),(2,33),(2,34),(2,35),(2,36),(2,37),(2,38),(2,39),(2,40),(2,41),(2,42),(2,43),(2,44),(2,45),(2,46),(2,47),
     (3,16),(3,17),(3,18),(3,19),(3,20),(3,21),(3,22),(3,23),(3,24),(3,25),(3,26),(3,27),(3,28),(3,29),(3,30),(3,31),(3,32),(3,33),(3,34),(3,35),(3,36),(3,37),(3,38),(3,39),(3,40),(3,41),(3,42),(3,43),(3,44),(3,45),(3,46),(3,47),

     (7,16),(7,17),(7,18),(7,19),(7,20),(7,21),(7,22),(7,23),(7,24),(7,25),(7,26),(7,27),(7,28),(7,29),(7,30),(7,31),(7,32),(7,33),(7,34),(7,35),(7,36),(7,37),(7,38),(7,39),(7,40),(7,41),(7,42),(7,43),(7,44),(7,45),(7,46),(7,47),
     (8,16),(8,17),(8,18),(8,19),(8,20),(8,21),(8,22),(8,23),(8,24),(8,25),(8,26),(8,27),(8,28),(8,29),(8,30),(8,31),(8,32),(8,33),(8,34),(8,35),(8,36),(8,37),(8,38),(8,39),(8,40),(8,41),(8,42),(8,43),(8,44),(8,45),(8,46),(8,47),
     (9,16),(9,17),(9,18),(9,19),(9,20),(9,21),(9,22),(9,23),(9,24),(9,25),(9,26),(9,27),(9,28),(9,29),(9,30),(9,31),(9,32),(9,33),(9,34),(9,35),(9,36),(9,37),(9,38),(9,39),(9,40),(9,41),(9,42),(9,43),(9,44),(9,45),(9,46),(9,47),
     (10,16),(10,17),(10,18),(10,19),(10,20),(10,21),(10,22),(10,23),(10,24),(10,25),(10,26),(10,27),(10,28),(10,29),(10,30),(10,31),(10,32),(10,33),(10,34),(10,35),(10,36),(10,37),(10,38),(10,39),(10,40),(10,41),(10,42),(10,43),(10,44),(10,45),(10,46),(10,47),

	 (15,16),(15,17),(15,18),(15,19),(15,20),(15,21),(15,22),(15,23),(15,24),(15,25),(15,26),(15,27),(15,28),(15,29),(15,30),(15,31),(15,32),(15,33),(15,34),(15,35),(15,36),(15,37),(15,38),(15,39),(15,40),(15,41),(15,42),(15,43),(15,44),(15,45),(15,46),(15,47),
	 (16,16),(16,17),(16,18),(16,19),(16,20),(16,21),(16,22),(16,23),(16,24),(16,25),(16,26),(16,27),(16,28),(16,29),(16,30),(16,31),(16,32),(16,33),(16,34),(16,35),(16,36),(16,37),(16,38),(16,39),(16,40),(16,41),(16,42),(16,43),(16,44),(16,45),(16,46),(16,47),
---Habitacion Doble / Duplex
     (4,16),(4,17),(4,18),(4,19),(4,20),(4,21),(4,22),(4,23),(4,24),(4,25),(4,26),(4,27),(4,28),(4,29),(4,30),(4,31),(4,32),(4,33),(4,34),(4,35),(4,36),(4,37),(4,38),(4,39),(4,40),(4,41),(4,42),(4,43),(4,44),(4,45),(4,46),(4,47),
     (5,16),(5,17),(5,18),(5,19),(5,20),(5,21),(5,22),(5,23),(5,24),(5,25),(5,26),(5,27),(5,28),(5,29),(5,30),(5,31),(5,32),(5,33),(5,34),(5,35),(5,36),(5,37),(5,38),(5,39),(5,40),(5,41),(5,42),(5,43),(5,44),(5,45),(5,46),(5,47),
     (6,16),(6,17),(6,18),(6,19),(6,20),(6,21),(6,22),(6,23),(6,24),(6,25),(6,26),(6,27),(6,28),(6,29),(6,30),(6,31),(6,32),(6,33),(6,34),(6,35),(6,36),(6,37),(6,38),(6,39),(6,40),(6,41),(6,42),(6,43),(6,44),(6,45),(6,46),(6,47),

	 (11,16),(11,17),(11,18),(11,19),(11,20),(11,21),(11,22),(11,23),(11,24),(11,25),(11,26),(11,27),(11,28),(11,29),(11,30),(11,31),(11,32),(11,33),(11,34),(11,35),(11,36),(11,37),(11,38),(11,39),(11,40),(11,41),(11,42),(11,43),(11,44),(11,45),(11,46),(11,47),
	 (12,16),(12,17),(12,18),(12,19),(12,20),(12,21),(12,22),(12,23),(12,24),(12,25),(12,26),(12,27),(12,28),(12,29),(12,30),(12,31),(12,32),(12,33),(12,34),(12,35),(12,36),(12,37),(12,38),(12,39),(12,40),(12,41),(12,42),(12,43),(12,44),(12,45),(12,46),(12,47),
	 (13,16),(13,17),(13,18),(13,19),(13,20),(13,21),(13,22),(13,23),(13,24),(13,25),(13,26),(13,27),(13,28),(13,29),(13,30),(13,31),(13,32),(13,33),(13,34),(13,35),(13,36),(13,37),(13,38),(13,39),(13,40),(13,41),(13,42),(13,43),(13,44),(13,45),(13,46),(13,47),
	 (14,16),(14,17),(14,18),(14,19),(14,20),(14,21),(14,22),(14,23),(14,24),(14,25),(14,26),(14,27),(14,28),(14,29),(14,30),(14,31),(14,32),(14,33),(14,34),(14,35),(14,36),(14,37),(14,38),(14,39),(14,40),(14,41),(14,42),(14,43),(14,44),(14,45),(14,46),(14,47),

	 (17,16),(17,17),(17,18),(17,19),(17,20),(17,21),(17,22),(17,23),(17,24),(17,25),(17,26),(17,27),(17,28),(17,29),(17,30),(17,31),(17,32),(17,33),(17,34),(17,35),(17,36),(17,37),(17,38),(17,39),(17,40),(17,41),(17,42),(17,43),(17,44),(17,45),(17,46),(17,47),
	 (18,16),(18,17),(18,18),(18,19),(18,20),(18,21),(18,22),(18,23),(18,24),(18,25),(18,26),(18,27),(18,28),(18,29),(18,30),(18,31),(18,32),(18,33),(18,34),(18,35),(18,36),(18,37),(18,38),(18,39),(18,40),(18,41),(18,42),(18,43),(18,44),(18,45),(18,46),(18,47);

