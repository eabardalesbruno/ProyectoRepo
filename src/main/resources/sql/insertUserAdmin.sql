----USER_ADMIN----
INSERT INTO public.useradmin (email,"password",username,firstname,lastname,phone,address,documenttypeid,documentnumber,"role",status,"permission",createdat,createdid,updatedat,updatedid) VALUES
	 ('super_admin@ribera.com','$2a$12$Pl.AmYKBj3y4jh3DwNNmFOr8y2f0kAvdhTBsNv3OUmuLOxXaOBMvm',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ROLE_SUPER_ADMIN','ACTIVE','READ,WRITE,CREATE,DELETE',now(),NULL,NULL,NULL);
