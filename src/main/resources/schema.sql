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
    roomtypename VARCHAR(50),
    roomtypedescription VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS stateroom (
    stateroomid SERIAL PRIMARY KEY,
    stateroomname VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS room (
    roomid SERIAL PRIMARY KEY,
    roomtypeid INTEGER,
    stateroomid INTEGER,
    roomdetailid INTEGER,
    roomname VARCHAR(50),
    roomnumber VARCHAR(50),
    image VARCHAR(255),
    capacity INTEGER,
    CONSTRAINT fk_roomtype FOREIGN KEY (roomtypeid) REFERENCES roomtype(roomtypeid),
    CONSTRAINT fk_stateroom FOREIGN KEY (stateroomid) REFERENCES stateroom(stateroomid),
    CONSTRAINT fk_roomdetail FOREIGN KEY (roomdetailid) REFERENCES roomdetail(roomdetailid)
);

CREATE TABLE IF NOT EXISTS offertype (
    offertypeid SERIAL PRIMARY KEY,
    offertypename VARCHAR(50),
    offertypedescription VARCHAR(255)
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
    points INTEGER,
    CONSTRAINT fk_room_ro FOREIGN KEY (roomid) REFERENCES room(roomid),
    CONSTRAINT fk_offertype_ro FOREIGN KEY (offertypeid) REFERENCES offertype(offertypeid)
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

CREATE TABLE IF NOT EXISTS useradmin (
    useradminid SERIAL PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(50),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    phone VARCHAR(50),
    address VARCHAR(255),
    typedocument VARCHAR(50),
    document VARCHAR(50),
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
    codeuser INTEGER,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    nationality VARCHAR(50),
    documenttype VARCHAR(50),
    documentnumber VARCHAR(50),
    birthdate TIMESTAMP,
    sex VARCHAR(10),
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
    CONSTRAINT fk_registertype_uc FOREIGN KEY (registertypeid) REFERENCES registertype(registertypeid),
    CONSTRAINT fk_userlevel_uc FOREIGN KEY (userlevelid) REFERENCES userlevel(userlevelid)
    CONSTRAINT fk_country_uc FOREIGN KEY (countryid) REFERENCES country(countryid)
);

CREATE TABLE IF NOT EXISTS booking (
    bookingid SERIAL PRIMARY KEY,
    roomofferid INTEGER,
    bookingstateid INTEGER,
    userclientid INTEGER,
    costfinal DECIMAL,
    detail VARCHAR(255),
    daybookinginit TIMESTAMP,
    daybookingend TIMESTAMP,
    checkin TIMESTAMP,
    checkout TIMESTAMP,
    createdat TIMESTAMP,

    CONSTRAINT fk_roomoffer FOREIGN KEY (roomofferid) REFERENCES roomoffer(roomofferid),
    CONSTRAINT fk_bookingstate FOREIGN KEY (bookingstateid) REFERENCES bookingstate(bookingstateid),
    CONSTRAINT fk_userclient FOREIGN KEY (userclientid) REFERENCES userclient(userclientid)
);

CREATE TABLE IF NOT EXISTS comfortbookingdetail (
    bookingid INTEGER,
    comforttypeid INTEGER,
    CONSTRAINT fk_booking_cb FOREIGN KEY (bookingid) REFERENCES booking(bookingid),
    CONSTRAINT fk_comfort_cb FOREIGN KEY (comforttypeid) REFERENCES comforttype(comforttypeid)
);

CREATE TABLE IF NOT EXISTS finalcostumer (
    finalcostumerid SERIAL PRIMARY KEY,
    bookingid INTEGER,
    documenttype VARCHAR(50),
    documentnumber VARCHAR(50),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    yearold INTEGER,
    CONSTRAINT fk_booking_fc FOREIGN KEY (bookingid) REFERENCES booking(bookingid)
);

CREATE TABLE IF NOT EXISTS partnerpoints (
    partnerpointid SERIAL PRIMARY KEY,
    userclientid INTEGER,
    points INTEGER,
    CONSTRAINT fk_userclient_pp FOREIGN KEY (userclientid) REFERENCES userclient(userclientid)
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
    createdid INTEGER,
    CONSTRAINT fk_booking_bi FOREIGN KEY (bookingid) REFERENCES booking(bookingid),
    CONSTRAINT fk_useradmin_bi FOREIGN KEY (useradminid) REFERENCES useradmin(useradminid)
);

CREATE TABLE IF NOT EXISTS refusereason (
    refusereasonid SERIAL PRIMARY KEY,
    refusereasonname VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS currencytype (
    currencytypeid SERIAL PRIMARY KEY,
    currencytypename VARCHAR(50),
    currencytypedescription VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paymentbook (
    paymentbookid SERIAL PRIMARY KEY,
    bookingid INTEGER,
    paymentmethodid INTEGER,
    paymentstateid INTEGER,
    currencytypeid INTEGER,
    amount DECIMAL,
    description VARCHAR(255),
    paymentdate TIMESTAMP,
    operationcode VARCHAR(50),
    note VARCHAR(255),
    totalcost DECIMAL,
    imagevoucher VARCHAR(255),
    totalpoints INTEGER,
    paymentcomplete BOOLEAN,
    CONSTRAINT fk_currencytype_pb FOREIGN KEY (currencytypeid) REFERENCES currencytype(currencytypeid),
    CONSTRAINT fk_booking_pb FOREIGN KEY (bookingid) REFERENCES booking(bookingid),
    CONSTRAINT fk_paymentmethod_pb FOREIGN KEY (paymentmethodid) REFERENCES paymentmethod(paymentmethodid),
    CONSTRAINT fk_paymentstate_pb FOREIGN KEY (paymentstateid) REFERENCES paymentstate(paymentstateid)
);

CREATE TABLE IF NOT EXISTS refusepayment (
    refusepaymentid SERIAL PRIMARY KEY,
    paymentbookid INTEGER,
    refusereasonid INTEGER,
    detail VARCHAR(255),
    CONSTRAINT fk_paymentbook_rp FOREIGN KEY (paymentbookid) REFERENCES paymentbook(paymentbookid),
    CONSTRAINT fk_refusereason_rp FOREIGN KEY (refusereasonid) REFERENCES refusereason(refusereasonid)
);

CREATE TABLE IF NOT EXISTS comfortroomofferdetail (
    roomofferid INTEGER,
    comforttypeid INTEGER,
    CONSTRAINT fk_roomoffer_cr FOREIGN KEY (roomofferid) REFERENCES roomoffer(roomofferid),
    CONSTRAINT fk_comfort_cr FOREIGN KEY (comforttypeid) REFERENCES comforttype(comforttypeid)
);

CREATE TABLE IF NOT EXISTS bedstype (
    bedtypeid SERIAL PRIMARY KEY,
    bedtypename VARCHAR(50),
    bedtypedescription VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS bedroom (
    bedroomid SERIAL PRIMARY KEY,
    roomid INTEGER,
    bedtypeid INTEGER,
    quantity INTEGER,
    CONSTRAINT fk_room_bed FOREIGN KEY (roomid) REFERENCES room(roomid),
    CONSTRAINT fk_bedtype_bed FOREIGN KEY (bedtypeid) REFERENCES bedstype(bedtypeid)
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
    responseat TIMESTAMP,
    CONSTRAINT fk_createduser_s FOREIGN KEY (createdid) REFERENCES useradmin(useradminid),
    CONSTRAINT fk_responseuser_s FOREIGN KEY (responseid) REFERENCES userclient(userclientid)
);

CREATE TABLE IF NOT EXISTS service (
    serviceid SERIAL PRIMARY KEY,
    servicedesc VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pointstype (
    pointstypeid SERIAL PRIMARY KEY,
    pointstypedesc VARCHAR(255)
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
    senderid INTEGER REFERENCES userclient(userclientid),
    requesttypeid INTEGER REFERENCES requesttype(requesttypeid),
    datetransfer TIMESTAMP,
    receiverid INTEGER REFERENCES userclient(userclientid),
    pointstransfered DOUBLE PRECISION,
    pointstypeid INTEGER REFERENCES pointstype(pointstypeid),
    sendandreceiveid INTEGER REFERENCES sendandreceive(sendandreceiveid)
);

CREATE TABLE IF NOT EXISTS pointsexchange (
    pointsexchangeid SERIAL PRIMARY KEY,
    userclientid INTEGER REFERENCES userclient(userclientid),
    echangetypeid INTEGER REFERENCES exchangetype(exchangetypeid),
    dateuse TIMESTAMP,
    exchangecode VARCHAR(255),
    serviceid INTEGER REFERENCES service(serviceid),
    description VARCHAR(255),
    checkin TIMESTAMP,
    checkout TIMESTAMP,
    nights INTEGER,
    pointsquantity DOUBLE PRECISION,
    pointsused DOUBLE PRECISION,
    pointstypeid INTEGER REFERENCES pointstype(pointstypeid),
    bookingid INTEGER REFERENCES booking(bookingid)
);

CREATE TABLE IF NOT EXISTS country (
    countryid SERIAL PRIMARY KEY,
    countrydesc VARCHAR(255)
);