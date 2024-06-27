----REGISTER_TYPE----
INSERT INTO public.registertype (registertypename) VALUES
	 ('MANUAL'),
	 ('GOOGLE'),
	 ('USUARIO');


----USER_ADMIN----
INSERT INTO public.useradmin (email,"password",username,firstname,lastname,phone,address,documenttypeid,documentnumber,"role",status,"permission",createdat,createdid,updatedat,updatedid) VALUES
	 ('super_admin@ribera.com','$2a$12$Pl.AmYKBj3y4jh3DwNNmFOr8y2f0kAvdhTBsNv3OUmuLOxXaOBMvm',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ROLE_SUPER_ADMIN','ACTIVE','READ,WRITE,CREATE,DELETE',now(),NULL,NULL,NULL);


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
	 (1 ,1,150.50,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,3,3,3),
	 (2 ,2,220.75,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,5,4),
	 (3 ,3,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,6,7,6),
	 (4 ,1,400.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,8,10,8),
	 (5 ,2,180.25,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,4,3),
	 (6 ,3,150.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,3,3,3),
	 (8 ,1,280.75,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,6,7,5),
	 (9 ,2,350.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),
	 (10,3,350.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),
	 (11,1,350.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,7,8,7),
	 (12,2,200.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,4,5,4),
	 (13,1,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (14,2,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (15,3,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (16,1,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (17,2,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (18,3,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),
	 (19,1,300.00,'2024-01-01 00:00:00','2025-01-01 00:00:00',NULL,5,5,5),

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

---Habitacion Doble / Duplex

---Habitacion matrimonial
     (4,4),(4,3),(4,8),(4,9),(4,16),(4,17),(4,18),(4,19),(4,10),(4,11),(4,12),(4,13),(4,14),(4,15),
     (5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,9),(5,10),(5,11),(5,12),(5,13),(5,14),(5,15),
     (6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(6,10),(6,11),(6,12),(6,13),(6,14),(6,15),


	 (2,2),

	 (2,2),
	 (3,2),

	 (4,3),
	 (5,3),
	 (6,3),

	 (7,2),
	 (8,2),
	 (9,2),
	 (10,2),

	 (11,3),
	 (12,3),
	 (13,3),
	 (14,3),

	 (15,2),
	 (16,2),

	 (17,3),
	 (18,3);