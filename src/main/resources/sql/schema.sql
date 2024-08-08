-- Table definitions

CREATE TABLE IF NOT EXISTS userlevel (
    userlevelid SERIAL PRIMARY KEY,
    levelname VARCHAR(50),
    leveldescription VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS roomdetail (
    roomdetailid SERIAL PRIMARY KEY,
    information VARCHAR(255),
    terms VARCHAR(255),
    bedrooms INTEGER,
    squaremeters VARCHAR(50),
    oceanviewbalcony BOOLEAN,
    balconyoverlookingpool BOOLEAN
);
CREATE TABLE IF NOT EXISTS roomtype (
    roomtypeid SERIAL PRIMARY KEY,
    roomtypename VARCHAR(150),
    roomtypedescription VARCHAR(555)
);
CREATE TABLE IF NOT EXISTS stateroom (
    stateroomid SERIAL PRIMARY KEY,
    stateroomname VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS offertype (
    offertypeid SERIAL PRIMARY KEY,
    offertypename VARCHAR(50),
    offertypedescription VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS bookingstate (
    bookingstateid SERIAL PRIMARY KEY,
    bookingstatename VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS comforttype (
    comforttypeid SERIAL PRIMARY KEY,
    comforttypename VARCHAR(50),
    comforttypedescription VARCHAR(255),
    active BOOLEAN
);
CREATE TABLE IF NOT EXISTS paymentmethod (
    paymentmethodid SERIAL PRIMARY KEY,
    description VARCHAR(255),
    state VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS paymentstate (
    paymentstateid SERIAL PRIMARY KEY,
    paymentstatename VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS registertype (
    registertypeid SERIAL PRIMARY KEY,
    registertypename VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS statepointstype (
    statepointstypeid SERIAL PRIMARY KEY,
    statepointstypedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS exchangetype (
    exchangetypeid SERIAL PRIMARY KEY,
    exchangetypedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS requesttype (
    requesttypeid SERIAL PRIMARY KEY,
    requesttypedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS sendandreceive (
    sendandreceiveid SERIAL PRIMARY KEY,
    sendandreceivedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS pointstransfers (
    pointstransferid SERIAL PRIMARY KEY,
    senderid INTEGER,
    requesttypeid INTEGER,
    receiverid INTEGER,
    pointstypeid INTEGER,
    sendandreceiveid INTEGER,
    datetransfer TIMESTAMP,
    pointstransfered DOUBLE PRECISION
);
CREATE TABLE IF NOT EXISTS pointsexchange (
    pointsexchangeid SERIAL PRIMARY KEY,
    userclientid INTEGER,
    exchangetypeid INTEGER,
    serviceid INTEGER,
    pointstypeid INTEGER,
    bookingid INTEGER,
    dateuse TIMESTAMP,
    exchangecode VARCHAR(255),
    description VARCHAR(255),
    checkin TIMESTAMP,
    checkout TIMESTAMP,
    nights INTEGER,
    pointsquantity DOUBLE PRECISION,
    pointsused DOUBLE PRECISION
);
CREATE TABLE IF NOT EXISTS country (
    countryid SERIAL PRIMARY KEY,
    countrydesc VARCHAR(255),
    iso VARCHAR(255),
    nicename VARCHAR(255),
    iso3 VARCHAR(255),
    numcode integer,
    phonecode integer,
    symbol VARCHAR(255),
    courtesy VARCHAR(255),
    icon VARCHAR(500)
);
CREATE TABLE IF NOT EXISTS gender (
    genderid SERIAL PRIMARY KEY,
    genderdesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS areazone (
    areazoneid SERIAL PRIMARY KEY,
    areazonedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS nationality (
    nationalityid SERIAL PRIMARY KEY,
    nationalitydesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS documenttype (
    documenttypeid SERIAL PRIMARY KEY,
    documenttypedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS termsversion (
    versionid SERIAL PRIMARY KEY,
    s3url VARCHAR(255),
    createdat TIMESTAMP
);
CREATE TABLE IF NOT EXISTS refusereason (
    refusereasonid SERIAL PRIMARY KEY,
    refusereasonname VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS cancelreason (
    cancelreasonid SERIAL PRIMARY KEY,
    cancelreasonname VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS cancelpayment (
    cancelpaymentid SERIAL PRIMARY KEY,
    paymentbookid INTEGER,
    cancelreasonid INTEGER,
    detail VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS currencytype (
    currencytypeid SERIAL PRIMARY KEY,
    currencytypename VARCHAR(50),
    currencytypedescription VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS bedstype (
    bedtypeid SERIAL PRIMARY KEY,
    bedtypename VARCHAR(50),
    bedtypedescription VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS emaillog (
    emaillogid SERIAL PRIMARY KEY,
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    sentdate TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS service (
    serviceid SERIAL PRIMARY KEY,
    servicedesc VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS transactioncategory(
    transactioncategoryid integer PRIMARY KEY,
    name varchar(255)
);
CREATE TABLE IF NOT EXISTS room (
    roomid SERIAL PRIMARY KEY,
    roomtypeid INTEGER,
    stateroomid INTEGER,
    roomdetailid INTEGER,
    roomname VARCHAR(50),
    roomnumber VARCHAR(50),
    image VARCHAR(255),
    capacity INTEGER
);
CREATE TABLE IF NOT EXISTS roomoffer (
    roomofferid SERIAL PRIMARY KEY,
    roomid INTEGER,
    offertypeid INTEGER,
    cost DECIMAL,
    offertimeinit TIMESTAMP,
    offertimeend TIMESTAMP,
    offername VARCHAR(50),
    riberapoints INTEGER,
    inresortpoints INTEGER,
    points INTEGER
);
CREATE TABLE IF NOT EXISTS useradmin (
    useradminid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(50),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    phone VARCHAR(50),
    address VARCHAR(255),
    documenttypeid INTEGER,
    documentnumber VARCHAR(50),
    role VARCHAR(50),
    status VARCHAR(50),
    permission VARCHAR(50),
    createdat TIMESTAMP,
    createdid INTEGER,
    updatedat TIMESTAMP,
    updatedid INTEGER
);
CREATE TABLE IF NOT EXISTS userpromoter (
    userpromoterid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(50),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    phone VARCHAR(50),
    address VARCHAR(255),
    documenttypeid INTEGER,
    documentnumber VARCHAR(50),
    role VARCHAR(50),
    status VARCHAR(50),
    permission VARCHAR(50),
    createdat TIMESTAMP,
    createdid INTEGER,
    updatedat TIMESTAMP,
    updatedid INTEGER
);
CREATE TABLE IF NOT EXISTS userclient (
    userclientid SERIAL PRIMARY KEY,
    registertypeid INTEGER,
    userlevelid INTEGER,
    countryid INTEGER,
    genderid INTEGER,
    codeuser INTEGER,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    documenttypeid INTEGER,
    documentnumber VARCHAR(50),
    birthdate TIMESTAMP,
    role INTEGER,
    civilstatus VARCHAR(50),
    city VARCHAR(50),
    address VARCHAR(255),
    cellnumber VARCHAR(20),
    email VARCHAR(255),
    password VARCHAR(255),
    googleauth VARCHAR(255),
    googleid VARCHAR(255),
    googleemail VARCHAR(255),
    username VARCHAR(50),
    status VARCHAR(50),
    createdat TIMESTAMP
);
CREATE TABLE IF NOT EXISTS booking (
    bookingid SERIAL PRIMARY KEY,
    roomofferid INTEGER,
    bookingstateid INTEGER,
    userclientid INTEGER,
    costfinal DECIMAL,
    detail VARCHAR(255),

    numberadults INTEGER,
    numberchildren INTEGER,
    numberbabies INTEGER,

    daybookinginit TIMESTAMP,
    daybookingend TIMESTAMP,
    checkin TIMESTAMP,
    checkout TIMESTAMP,
    createdat TIMESTAMP
);
CREATE TABLE IF NOT EXISTS comfortbookingdetail (
    bookingid INTEGER,
    comforttypeid INTEGER
);
CREATE TABLE IF NOT EXISTS comfortservicedetail (
    roomofferid INTEGER,
    comforttypeid INTEGER
);
CREATE TABLE IF NOT EXISTS finalcostumer (
    finalcostumerid SERIAL PRIMARY KEY,
    bookingid INTEGER,
    documenttype VARCHAR(50),
    documentnumber VARCHAR(50),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    yearold INTEGER
);
CREATE TABLE IF NOT EXISTS partnerpoints (
    partnerpointid SERIAL PRIMARY KEY,
    userclientid INTEGER,
    points INTEGER
);
CREATE TABLE IF NOT EXISTS bookingincidents (
    roomincidentsid SERIAL PRIMARY KEY,
    bookingid INTEGER,
    useradminid INTEGER,
    evidenceimage VARCHAR(255),
    description VARCHAR(255),
    observation VARCHAR(255),
    actionstake VARCHAR(255),
    createdat TIMESTAMP,
    createdid INTEGER
);
CREATE TABLE IF NOT EXISTS paymentbook (
    paymentbookid SERIAL PRIMARY KEY,
    bookingid INTEGER NOT NULL,
    userclientid INTEGER NOT NULL,
    refusereasonid INTEGER NOT NULL,
    cancelreasonid INTEGER,
    paymentmethodid INTEGER,
    paymentstateid INTEGER,
    paymenttypeid INTEGER,
    paymentsubtypeid INTEGER,
    currencytypeid INTEGER,
    amount DECIMAL,
    description VARCHAR(255),
    paymentdate TIMESTAMP,
    operationcode VARCHAR(50),
    note VARCHAR(255),
    totalcost DECIMAL,
    imagevoucher VARCHAR(150000),
    totalpoints INTEGER,
    paymentcomplete BOOLEAN,
    pendingpay INTEGER DEFAULT 0
);
CREATE TABLE IF NOT EXISTS paymenttoken (
    paymenttokenid SERIAL PRIMARY KEY,
    paymenttoken VARCHAR(255),
	startdate TIMESTAMP,
	enddate TIMESTAMP,
	bookingid INTEGER NOT NULL,
	paymentbookid INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS refusepayment (
    refusepaymentid SERIAL PRIMARY KEY,
    paymentbookid INTEGER,
    refusereasonid INTEGER,
    detail VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS comfortroomofferdetail (
    roomofferid INTEGER,
    comforttypeid INTEGER
);
CREATE TABLE IF NOT EXISTS bedroom (
    bedroomid SERIAL PRIMARY KEY,
    roomid INTEGER,
    bedtypeid INTEGER,
    quantity INTEGER
);
CREATE TABLE IF NOT EXISTS solicitude (
    solicitudeid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(50),
    message VARCHAR(255),
    codesend VARCHAR(50),
    status VARCHAR(50),
    type VARCHAR(50),
    createdid INTEGER,
    createdat TIMESTAMP,
    responseid INTEGER,
    responseat TIMESTAMP
);
CREATE TABLE IF NOT EXISTS pointstype (
    pointstypeid SERIAL PRIMARY KEY,
    pointstypedesc VARCHAR(255),
    statepointstypeid INTEGER
);
CREATE TABLE IF NOT EXISTS userclientversion (
    userclientversionid SERIAL PRIMARY KEY,
    userclientid INTEGER,
    versionid INTEGER,
	active BOOLEAN,
	createdat TIMESTAMP
);
CREATE TABLE IF NOT EXISTS paymenttype (
    paymenttypeid SERIAL PRIMARY KEY,
    paymenttypedesc VARCHAR(255),
    countryid INTEGER,
    paymentmethodid INTEGER
);
CREATE TABLE IF NOT EXISTS paymentsubtype (
    paymentsubtypeid SERIAL PRIMARY KEY,
    paymentsubtypedesc VARCHAR(255),
    accountsoles VARCHAR(255),
    accountdollars VARCHAR(255),
    paymenttypeid INTEGER,
    soles DOUBLE PRECISION,
    dollars DOUBLE PRECISION,
    percentage DOUBLE PRECISION,
    statussoles INTEGER,
    statusdollars INTEGER
);
CREATE TABLE IF NOT EXISTS typepointstransaction(
    typepointstransactionid integer PRIMARY KEY,
    description varchar(255),
    status integer,
    transactioncategoryid integer,
    istransferbalanceavailable integer
);
CREATE TABLE IF NOT EXISTS pointstransaction(
    pointstransactionid SERIAL PRIMARY KEY,
    partnerpointid integer,
    typepointstransactionid integer,
    initialdate timestamp,
    points integer,
    isavailable integer,
    availabilitydate timestamp,
    referencedata varchar(255),
    successfultransaction integer
);
CREATE TABLE IF NOT EXISTS tokenpointstransaction(
    tokenpointstransactionid SERIAL PRIMARY KEY,
    codigotoken VARCHAR(255),
    datecreated TIMESTAMP,
    expirationdate TIMESTAMP,
    partnerpointid INTEGER,
    bookingid INTEGER
);

CREATE TABLE complaintsbook (
    id SERIAL PRIMARY KEY,
    persontype VARCHAR(50) NOT NULL,
    businessname VARCHAR(100),
    ruc VARCHAR(20),
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    isadult BOOLEAN NOT NULL,
    address VARCHAR(255),
    acceptedterms BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS documenttype( documenttypedesc)
	VALUES ('DNI'),('RUC');

CREATE TABLE IF NOT EXISTS public.implements_apartment(
    implements_apartment_id SERIAL PRIMARY KEY,
    implements_apartment_name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS public.implements_comfort(
    implements_apartment_id integer,
    comforttypeid integer,
    cuantity integer
);

CREATE TABLE IF NOT EXISTS public.pay_me_authorizations (
    idAuthorization SERIAL PRIMARY KEY,
    idBooking INTEGER NOT NULL,
    idUser INTEGER NOT NULL,
    role VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    action VARCHAR(255),
    id VARCHAR(255),
    success BOOLEAN,
    currency VARCHAR(255),
    amount VARCHAR(255),
    internal_operation_number VARCHAR(255),
    description TEXT,
    processor_authorization_code VARCHAR(255),
    additional_fields TEXT, -- Assuming JSON format or similar for simplicity
    status_code VARCHAR(255),
    message_ilgn TEXT -- Assuming JSON format for list of messages
);



/*
ALTER TABLE pointstransfers ADD CONSTRAINT fk_sender_pt FOREIGN KEY (senderid) REFERENCES userclient(userclientid);
ALTER TABLE pointstransfers ADD CONSTRAINT fk_requesttype_pt FOREIGN KEY (requesttypeid) REFERENCES requesttype(requesttypeid);
ALTER TABLE pointstransfers ADD CONSTRAINT fk_receiver_pt FOREIGN KEY (receiverid) REFERENCES userclient(userclientid);
ALTER TABLE pointstransfers ADD CONSTRAINT fk_pointstype_pt FOREIGN KEY (pointstypeid) REFERENCES pointstype(pointstypeid);
ALTER TABLE pointstransfers ADD CONSTRAINT fk_sendandreceive_pt FOREIGN KEY (sendandreceiveid) REFERENCES sendandreceive(sendandreceiveid);

ALTER TABLE pointsexchange ADD CONSTRAINT fk_userclient_pe FOREIGN KEY (userclientid) REFERENCES userclient(userclientid);
ALTER TABLE pointsexchange ADD CONSTRAINT fk_exchangetype_pe FOREIGN KEY (exchangetypeid) REFERENCES exchangetype(exchangetypeid);
ALTER TABLE pointsexchange ADD CONSTRAINT fk_service_pe FOREIGN KEY (serviceid) REFERENCES service(serviceid);
ALTER TABLE pointsexchange ADD CONSTRAINT fk_pointstype_pe FOREIGN KEY (pointstypeid) REFERENCES pointstype(pointstypeid);
ALTER TABLE pointsexchange ADD CONSTRAINT fk_booking_pe FOREIGN KEY (bookingid) REFERENCES booking(bookingid);

ALTER TABLE tokenpointstransaction ADD CONSTRAINT fk_partnerpoint FOREIGN KEY (partnerpointid) REFERENCES partnerpoints (partnerpointid);
ALTER TABLE tokenpointstransaction ADD CONSTRAINT fk_booking FOREIGN KEY (bookingid) REFERENCES booking (bookingid);

ALTER TABLE pointstransaction ADD CONSTRAINT fk_partnerpoint FOREIGN KEY (partnerpointid) REFERENCES partnerpoints (partnerpointid);
ALTER TABLE pointstransaction ADD CONSTRAINT fk_typepointstransaction FOREIGN KEY (typepointstransactionid) REFERENCES typepointstransaction (typepointstransactionid);

ALTER TABLE typepointstransaction ADD CONSTRAINT fk_transactioncategory FOREIGN KEY (transactioncategoryid) REFERENCES transactioncategory (transactioncategoryid);

ALTER TABLE paymentsubtype ADD CONSTRAINT fk_paymenttype_uc FOREIGN KEY (paymenttypeid) REFERENCES paymenttype(paymenttypeid);

ALTER TABLE paymenttype ADD CONSTRAINT fk_country_uc FOREIGN KEY (countryid) REFERENCES country(countryid);
ALTER TABLE paymenttype ADD CONSTRAINT fk_paymentmethod_uc FOREIGN KEY (paymentmethodid) REFERENCES paymentmethod(paymentmethodid);

ALTER TABLE userclientversion ADD CONSTRAINT fk_userclient_uv FOREIGN KEY (userclientid) REFERENCES userclient(userclientid);
ALTER TABLE userclientversion ADD CONSTRAINT fk_termsversion_uv FOREIGN KEY (versionid) REFERENCES termsversion(versionid);

ALTER TABLE pointstype ADD CONSTRAINT fk_statepointstypeid_s FOREIGN KEY (statepointstypeid) REFERENCES statepointstype(statepointstypeid);

ALTER TABLE solicitude ADD CONSTRAINT fk_createduser_s FOREIGN KEY (createdid) REFERENCES useradmin(useradminid);
ALTER TABLE solicitude ADD CONSTRAINT fk_responseuser_s FOREIGN KEY (responseid) REFERENCES userclient(userclientid);

ALTER TABLE bedroom ADD CONSTRAINT fk_room_bed FOREIGN KEY (roomid) REFERENCES room(roomid);
ALTER TABLE bedroom ADD CONSTRAINT fk_bedtype_bed FOREIGN KEY (bedtypeid) REFERENCES bedstype(bedtypeid);

ALTER TABLE comfortroomofferdetail ADD CONSTRAINT fk_roomoffer_cr FOREIGN KEY (roomofferid) REFERENCES roomoffer(roomofferid);
ALTER TABLE comfortroomofferdetail ADD CONSTRAINT fk_comfort_cr FOREIGN KEY (comforttypeid) REFERENCES comforttype(comforttypeid);

ALTER TABLE refusepayment ADD CONSTRAINT fk_paymentbook_rp FOREIGN KEY (paymentbookid) REFERENCES paymentbook(paymentbookid);
ALTER TABLE refusepayment ADD CONSTRAINT fk_refusereason_rp FOREIGN KEY (refusereasonid) REFERENCES refusereason(refusereasonid);

ALTER TABLE paymenttoken ADD CONSTRAINT fk_booking_pb FOREIGN KEY (bookingid) REFERENCES booking(bookingid);
ALTER TABLE paymenttoken ADD CONSTRAINT fk_paymentbook_pb FOREIGN KEY (paymentbookid) REFERENCES paymentbook(paymentbookid);

ALTER TABLE paymentbook ADD CONSTRAINT fk_currencytype_pb FOREIGN KEY (currencytypeid) REFERENCES currencytype(currencytypeid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_booking_pb FOREIGN KEY (bookingid) REFERENCES booking(bookingid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_paymentmethod_pb FOREIGN KEY (paymentmethodid) REFERENCES paymentmethod(paymentmethodid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_paymentstate_pb FOREIGN KEY (paymentstateid) REFERENCES paymentstate(paymentstateid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_paymenttype_pb FOREIGN KEY (paymenttypeid) REFERENCES paymenttype(paymenttypeid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_paymentsubtype_pb FOREIGN KEY (paymentsubtypeid) REFERENCES paymentsubtype(paymentsubtypeid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_userclient_pb FOREIGN KEY (userclientid) REFERENCES userclient(userclientid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_refusereason_pb FOREIGN KEY (refusereasonid) REFERENCES refusereason(refusereasonid);
ALTER TABLE paymentbook ADD CONSTRAINT fk_cancelreason_pb FOREIGN KEY (cancelreasonid) REFERENCES cancelreason(cancelreasonid);

ALTER TABLE cancelpayment ADD CONSTRAINT fk_paymentbook_rp FOREIGN KEY (paymentbookid) REFERENCES paymentbook(paymentbookid);
ALTER TABLE cancelpayment ADD CONSTRAINT fk_cancelreason_rp FOREIGN KEY (cancelreasonid) REFERENCES cancelreason(cancelreasonid);

ALTER TABLE bookingincidents ADD CONSTRAINT fk_booking_bi FOREIGN KEY (bookingid) REFERENCES booking(bookingid);
ALTER TABLE bookingincidents ADD CONSTRAINT fk_useradmin_bi FOREIGN KEY (useradminid) REFERENCES useradmin(useradminid);

ALTER TABLE partnerpoints ADD CONSTRAINT fk_userclient_pp FOREIGN KEY (userclientid) REFERENCES userclient(userclientid);

ALTER TABLE finalcostumer ADD CONSTRAINT fk_booking_fc FOREIGN KEY (bookingid) REFERENCES booking(bookingid);

ALTER TABLE comfortservicedetail ADD CONSTRAINT fk_roomoffer_cb FOREIGN KEY (roomofferid) REFERENCES roomoffer(roomofferid);
ALTER TABLE comfortservicedetail ADD CONSTRAINT fk_comfort_cb FOREIGN KEY (comforttypeid) REFERENCES comforttype(comforttypeid);

ALTER TABLE roomoffer ADD CONSTRAINT fk_room_ro FOREIGN KEY (roomid) REFERENCES room(roomid);
ALTER TABLE roomoffer ADD CONSTRAINT fk_offertype_ro FOREIGN KEY (offertypeid) REFERENCES offertype(offertypeid);

ALTER TABLE useradmin ADD CONSTRAINT fk_documenttype_uc FOREIGN KEY (documenttypeid) REFERENCES documenttype(documenttypeid);

ALTER TABLE userpromoter ADD CONSTRAINT fk_documenttype_uc FOREIGN KEY (documenttypeid) REFERENCES documenttype(documenttypeid);

ALTER TABLE userclient ADD CONSTRAINT fk_registertype_uc FOREIGN KEY (registertypeid) REFERENCES registertype(registertypeid);
ALTER TABLE userclient ADD CONSTRAINT fk_userlevel_uc FOREIGN KEY (userlevelid) REFERENCES userlevel(userlevelid);
ALTER TABLE userclient ADD CONSTRAINT fk_country_uc FOREIGN KEY (countryid) REFERENCES country(countryid);
ALTER TABLE userclient ADD CONSTRAINT fk_gender_uc FOREIGN KEY (genderid) REFERENCES gender(genderid);
ALTER TABLE userclient ADD CONSTRAINT fk_documenttype_uc FOREIGN KEY (documenttypeid) REFERENCES documenttype(documenttypeid);

ALTER TABLE booking ADD CONSTRAINT fk_roomoffer FOREIGN KEY (roomofferid) REFERENCES roomoffer(roomofferid);
ALTER TABLE booking ADD CONSTRAINT fk_bookingstate FOREIGN KEY (bookingstateid) REFERENCES bookingstate(bookingstateid);
ALTER TABLE booking ADD CONSTRAINT fk_userclient FOREIGN KEY (userclientid) REFERENCES userclient(userclientid);

ALTER TABLE comfortbookingdetail ADD CONSTRAINT fk_booking_cb FOREIGN KEY (bookingid) REFERENCES booking(bookingid);
ALTER TABLE comfortbookingdetail ADD CONSTRAINT fk_comfort_cb FOREIGN KEY (comforttypeid) REFERENCES comforttype(comforttypeid);

ALTER TABLE room ADD CONSTRAINT fk_roomtype_r FOREIGN KEY (roomtypeid) REFERENCES roomtype(roomtypeid);
ALTER TABLE room ADD CONSTRAINT fk_stateroom_r FOREIGN KEY (stateroomid) REFERENCES stateroom(stateroomid);
ALTER TABLE room ADD CONSTRAINT fk_roomdetail_r FOREIGN KEY (roomdetailid) REFERENCES roomdetail(roomdetailid);
*/
