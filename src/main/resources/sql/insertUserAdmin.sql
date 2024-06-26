DO
$do$
DECLARE
    v_email VARCHAR(255) := 'super_admin@ribera.com'; -- replace with the actual email
    v_exists INTEGER;

    v_password VARCHAR(255) := 'super_admin'; -- replace with the actual password
BEGIN
    SELECT COUNT(*) INTO v_exists
    FROM userAdmin
    WHERE email = v_email;

    IF v_exists = 0 THEN
        INSERT INTO userAdmin (
            email,
        	password,
        	role,
        	status,
        	permission,
        	createdat
        )
        VALUES (
            v_email,
            crypt(v_password, gen_salt('bf')),
            'ROLE_SUPER_ADMIN',
            'ACTIVE',
            'READ,WRITE,CREATE,DELETE',
            now()
        );
    ELSE
        RAISE NOTICE 'A user with the same email or document number already exists.';
    END IF;
END
$do$