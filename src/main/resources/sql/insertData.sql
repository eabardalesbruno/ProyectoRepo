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
	 ('LISTO_PARA_LLEGADA'),
	 ('OCUPADO_HUESPED'),
	 ('EN_MANTENIMIENTO'),
	 ('REQUIERE_LIMPIEZA_PROFUNDA'),
	 ('EN_RENOVACION'),
	 ('LISTO_PARA_OCUPAR'),
	 ('SIN_DISPONIBILIDAD'),
	 ('SIN_MANTENIMIENTO'),
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
	 (3,2,1,'Departamento con vista al jardín','301','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/a8b6adaa-4191-4a17-ba04-e46a134aef96-resized_IMG_7582.jpg',2),
	 (3,2,3,'Departamento con vista al jardín','203','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/56dc5341-1f57-4729-b4d2-03101b0ccb6e-resized_IMG_7534.JPG',2),
	 (3,2,3,'Departamento con vista al jardín','303','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/77bb88db-250d-47ce-bd4a-fea99328c43e-resized_IMG_7535.jpg',2),
	 (4,2,5,'Departamento con vista al jardín','205','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/d06fee02-dc0b-44c8-81e7-1e0f2499b78b-resized_IMG_7498.jpg',4),
	 (4,2,5,'Departamento con vista al jardín','305','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/e5ce87b8-5903-46c9-805b-583f9247173e-IMG_1425.JPEG',4),
	 (4,2,7,'Departamento con vista al jardín','307','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/d0427e0c-c636-4e77-89bb-258eb9d3a7e6-IMG_1426.JPEG',4),
	 (1,2,2,'Departamento con vista al río','202','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/feb6ae4a-75dd-4db9-8d31-a0ba9a462c22-resized_IMG_7568.jpg',2),
	 (1,2,2,'Departamento con vista al río','302','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/30ff668b-ee83-4d4a-814d-2904352e3573-resized_IMG_7593.jpg',2),
	 (1,2,4,'Departamento con vista al río','204','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/909b1d71-a92b-4a6e-bce9-6cbc1cac6574-resized_IMG_7577.jpg',2),
	 (2,2,4,'Departamento con vista al río','304','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/78f3ee93-8845-41d3-a0c8-1063d014507b-resized_IMG_7567.jpg',4),
	 (4,2,7,'Departamento con vista al jardín','207','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/ba64568f-a82e-4a8b-a71e-c3886e7c67d4-resized_IMG_7531.JPG',4),
	 (2,2,6,'Departamento con vista al río','206','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/a8b6adaa-4191-4a17-ba04-e46a134aef96-resized_IMG_7582.jpg',4),
	 (2,2,6,'Departamento con vista al río','306','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/909b1d71-a92b-4a6e-bce9-6cbc1cac6574-resized_IMG_7577.jpg',4),
	 (5,2,8,'Departamento con vista doble','208','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/78f3ee93-8845-41d3-a0c8-1063d014507b-resized_IMG_7567.jpg',2),
	 (5,2,8,'Departamento con vista doble','308','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/67abc9bf-d956-4646-8904-9d03f364247c-resized_IMG_7492.jpg',2),
	 (6,2,9,'Departamento con vista doble','309','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/7a3e8902-7b84-4353-8c80-4070af79d4b2-resized_IMG_7522.JPG',4),
	 (3,2,1,'Departamento con vista al jardín','201','https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/080b7545-d1a1-415b-9f94-79d1699d017c-compressed_IMG_1384.JPEG',2),
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

----COMFORT----
INSERT INTO public.comforttype (comforttypename,comforttypedescription,active) VALUES
	 ('Wi-Fi gratis','Conexión a internet gratuita en todas las áreas del resort.',true),
	 ('Servicio de habitaciones','Disfrute de la comodidad de solicitar comida y bebida en la intimidad de su habitación.',true),
	 ('Aire acondicionado','Ambientes frescos y confortables durante todo el año.',true),
	 ('TV de pantalla plana','Entretenimiento con una amplia selección de canales en alta definición.',true),
	 ('Baño privado','Baño exclusivo para mayor privacidad y comodidad.',true),
	 ('Mini bar','Bebidas y aperitivos disponibles en la habitación para disfrutar en cualquier momento.',true),
	 ('Caja de seguridad','Mantenga sus objetos de valor seguros durante su estancia.',true),
	 ('Secador de pelo','Facilidad para el secado del cabello después de la ducha.',true),
	 ('Plancha y tabla de planchar','Para mantener su ropa impecable durante su viaje.',true),
	 ('Zona de estar','Espacio adicional para relajarse y disfrutar de la comodidad de su habitación.',true),
	 ('Escritorio','Área de trabajo conveniente para aquellos que necesitan mantenerse productivos durante su estancia.',true),
	 ('Vistas al mar','Disfrute de impresionantes vistas al océano desde la comodidad de su habitación.',true),
	 ('Jacuzzi privado','Relájese y rejuvenezca con un jacuzzi privado en su habitación.',true),
	 ('Terraza privada','Espacio al aire libre exclusivo para disfrutar del clima y las vistas circundantes.',true),
	 ('Cama King-size','Descanse cómodamente en una espaciosa cama King-size durante su estadía.',true),
	 ('Cama Queen-size','Confort y amplitud en una cama Queen-size para una noche de sueño reparador.',true),
	 ('Cama doble','Espacio adicional en una cama doble para aquellos que prefieren más espacio para dormir.',true),
	 ('Camas individuales','Perfecto para amigos o familiares que prefieren dormir en camas separadas.',true),
	 ('Piscina privada','Disfrute de la exclusividad de su propia piscina privada durante su estancia.',true),
	 ('Gimnasio en el resort','Manténgase activo y en forma con acceso gratuito a un gimnasio totalmente equipado.',true),
	 ('Spa en el resort','Relájese y rejuvenezca con una amplia gama de tratamientos en nuestro spa de lujo.',true),
	 ('Restaurante gourmet','Deléitese con exquisitos platos preparados por chefs expertos en nuestro restaurante de primera categoría.',true),
	 ('Club de playa privado','Exclusividad y comodidad en nuestro club de playa privado con servicio de bebidas y comidas.',true),
	 ('Servicio de conserjería','Asistencia personalizada para satisfacer todas sus necesidades y deseos durante su estancia.',true),
	 ('Servicio de transporte','Traslados convenientes desde y hacia el aeropuerto o lugares de interés locales.',true),
	 ('Actividades de entretenimiento','Una variedad de actividades emocionantes para toda la familia, desde deportes acuáticos hasta excursiones guiadas.',true),
	 ('Servicio de niñera','Disfrute de un momento de tranquilidad sabiendo que sus hijos están en buenas manos con nuestro servicio de niñera.',true);



----BOOKING_STATUS----
INSERT INTO public.bookingstate (bookingstatename) VALUES
	 ('RECHAZADO'),
	 ('ACEPTADO'),
	 ('PENDIENTE'),
	 ('ANULADO'),
	 ('FINALIZADO'),
	 ('EN USO');

----BEDS_TYPE----
INSERT INTO public.bedstype (bedtypename,bedtypedescription) VALUES
	 ('King','Cama King-size para una experiencia de lujo y espacio amplio para dormir cómodamente.'),
	 ('Queen','Cama Queen-size, un estándar de confort en la mayoría de los hoteles, ideal para parejas.'),
	 ('Double','Cama Doble, ofreciendo espacio adicional para una persona que prefiere más espacio.'),
	 ('Twin','Dos camas individuales, perfectas para amigos o familiares que prefieren dormir separados.'),
	 ('Waterbed','Cama de agua que proporciona una sensación de flotación y alivio del estrés, perfecta para una experiencia de sueño relajante.');
