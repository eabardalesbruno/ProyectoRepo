CREATE TABLE IF NOT EXISTS paymentstate (
    paymentstateid SERIAL PRIMARY KEY,
    paymentstatename VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS registertype (
    registertypeid SERIAL PRIMARY KEY,
    registertypename VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS userlevel (
    userlevelid SERIAL PRIMARY KEY,
    levelname VARCHAR(255),
    leveldescription TEXT
);

CREATE TABLE IF NOT EXISTS room (
    roomid SERIAL PRIMARY KEY,
    roomname VARCHAR(255),
    image TEXT,
    occupation VARCHAR(255),
    capacity INT,
    termsid INT,
    wifi BOOLEAN,
    beds INT,
    roomnumber VARCHAR(255),
    roomtype VARCHAR(255),
    info TEXT,
    state VARCHAR(255) CHECK (state IN ('Available', 'Occupied', 'Reserved', 'Under Maintenance', 'Out of Service'))
);

CREATE TABLE IF NOT EXISTS bookingstate (
    bookingstateid SERIAL PRIMARY KEY,
    bookingstatename VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS paymentmethod (
    paymentmethodid SERIAL PRIMARY KEY,
    description VARCHAR(255),
    state VARCHAR(255) CHECK (state IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE IF NOT EXISTS userclient (
    userclientid SERIAL PRIMARY KEY,
    registertypeid INT,
    userlevelid INT,
    codeuser INT,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    nationality VARCHAR(255),
    documenttype VARCHAR(255),
    documentnumber VARCHAR(255),
    birthdate TIMESTAMP,
    sex VARCHAR(255),
    role INT,
    civilstatus VARCHAR(255),
    city VARCHAR(255),
    address VARCHAR(255),
    cellnumber VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    googleauth VARCHAR(255),
    googleid VARCHAR(255),
    googleemail VARCHAR(255),
    username VARCHAR(255),
    status VARCHAR(255),
    FOREIGN KEY (registertypeid) REFERENCES registertype(registertypeid),
    FOREIGN KEY (userlevelid) REFERENCES userlevel(userlevelid)
);

CREATE TABLE IF NOT EXISTS booking (
    bookingid SERIAL PRIMARY KEY,
    roomid INT,
    paymentstateid INT,
    costrelative NUMERIC,
    riberapoints INT,
    inresortspoints INT,
    points INT,
    detail TEXT,
    amenities TEXT,
    services TEXT,
    FOREIGN KEY (paymentstateid) REFERENCES paymentstate(paymentstateid)
);

CREATE TABLE IF NOT EXISTS roomdetail (
    roomdetailid SERIAL PRIMARY KEY,
    roomid INT,
    information TEXT,
    FOREIGN KEY (roomid) REFERENCES room(roomid)
);

CREATE TABLE IF NOT EXISTS partnerpoints (
    partnerpointid SERIAL PRIMARY KEY,
    userclientid INT,
    points INT,
    FOREIGN KEY (userclientid) REFERENCES userclient(userclientid)
);

CREATE TABLE IF NOT EXISTS bookingdetail (
    bookingdetailid SERIAL PRIMARY KEY,
    bookingid INT,
    userclientid INT,
    paymentstateid INT,
    checkin TIMESTAMP,
    checkout TIMESTAMP,
    costfinal NUMERIC,
    customer VARCHAR(255),
    documenttype VARCHAR(255),
    documentnumber VARCHAR(255),
    adultsnumber INT,
    childrennumber INT,
    babiesnumber INT,
    FOREIGN KEY (bookingid) REFERENCES booking(bookingid),
    FOREIGN KEY (userclientid) REFERENCES userclient(userclientid),
    FOREIGN KEY (paymentstateid) REFERENCES paymentstate(paymentstateid)
);

CREATE TABLE IF NOT EXISTS solicitude (
    solicitudeid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(255),
    message TEXT,
    codesend VARCHAR(255),
    status VARCHAR(255) CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    type VARCHAR(255),
    createdid INT,
    createdat TIMESTAMP,
    responseid INT,
    responseat TIMESTAMP
);

CREATE TABLE IF NOT EXISTS useradmin (
    useradminid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    phone VARCHAR(255),
    address VARCHAR(255),
    typedocument VARCHAR(255),
    document VARCHAR(255),
    role VARCHAR(255),
    status VARCHAR(255),
    createdat TIMESTAMP,
    createdid INT,
    updatedat TIMESTAMP,
    updatedid INT
);
