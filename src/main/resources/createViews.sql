
CREATE OR REPLACE VIEW ViewBedsType AS
SELECT
    b.bookingid AS bookingId,
    bt.*
FROM
    booking b
JOIN
    roomoffer ro ON b.roomofferid = ro.roomofferid
JOIN
    room r ON ro.roomid = r.roomid
JOIN
	bedroom br ON r.roomid = br.roomid
JOIN
	bedstype bt ON br.bedtypeid = bt.bedtypeid;



CREATE OR REPLACE VIEW viewcomfortdata AS
SELECT
    b.bookingid AS bookingId,
    ct.*
FROM
    booking b
JOIN
    roomoffer ro ON b.roomofferid = ro.roomofferid
JOIN
    comfortroomofferdetail crod ON ro.roomofferid = crod.roomofferid
JOIN
    comforttype ct ON crod.comforttypeid = ct.comforttypeid;



CREATE OR REPLACE VIEW ViewBookingReturn AS
SELECT
    b.bookingid AS bookingId,
	b.userclientid,
	b.bookingstateid,
    r.image AS image,
    bs.bookingstatename AS state,
    rd.information AS description,
    rd.bedrooms AS bedrooms,
    rd.squaremeters AS squareMeters,
    rd.oceanviewbalcony AS oceanViewBalcony,
    rd.balconyoverlookingpool AS balconyOverlookingPool,
    r.capacity AS capacity,
    b.daybookinginit AS dayBookingInit,
    b.daybookingend AS dayBookingEnd,
    ro.cost AS price,
    ro.inresortpoints AS pointsInResort,
    ro.riberapoints AS pointsRibera
FROM
    booking b
JOIN
    roomoffer ro ON b.roomofferid = ro.roomofferid
JOIN
    room r ON ro.roomid = r.roomid
JOIN
    roomtype rt ON r.roomtypeid = rt.roomtypeid
JOIN
    roomdetail rd ON r.roomdetailid = rd.roomdetailid
JOIN
    bookingstate bs ON b.bookingstateid = bs.bookingstateid;

CREATE OR REPLACE VIEW ViewAdminBookingInventoryReturn AS
SELECT
	r.roomid,
	ro.roomofferid,
	rt.roomtypeid,

	ro.offertimeinit as dateInit,
	ro.offertimeend as dateEnd,
	CONCAT('Inicio: ', to_char(ro.offertimeinit, 'DD/MM/YYYY'), E'\n', 'Fin: ', to_char(ro.offertimeend, 'DD/MM/YYYY')) AS datestring,
	ROW_NUMBER() OVER (ORDER BY roomofferid) AS item,
	r.roomnumber as numberroom,
	r.roomname as typeroom,

	(SELECT COUNT(public.roomoffer.roomid) as stock
	 FROM roomoffer
	 WHERE roomoffer.roomid = ro.roomid
	 GROUP BY roomoffer.roomid),

	ro.cost AS costRegular,
	ro.cost AS costTotal,
	ro.cost AS costExchange,
	ro.cost AS costTotalExchange,
	ro.riberapoints AS pointRibera,
	ro.inresortpoints AS pointInResort,

	ro.cost AS costRegularstring,
	ro.cost AS costTotalstring,
	ro.cost AS costExchangestring,
	ro.cost AS costTotalExchangestring,
	ro.riberapoints AS pointRiberastring,
	ro.inresortpoints AS pointInResortstring

FROM roomoffer ro
JOIN room r ON r.roomid = ro.roomid
JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid;

CREATE OR REPLACE VIEW ViewAdminBookingReturn AS
SELECT
	r.roomid,
	bk.roomofferid,
	rt.roomtypeid,
	bk.bookingid,
	bk.bookingstateid,
	bk.userclientid,

	bk.createdat as dateCreate,
	to_char(bk.createdat, 'DD/MM/YYYY HH12:MI') as dateCreateString,
	bk.bookingid as numberBooking,

	bk.daybookinginit,
	to_char(bk.daybookinginit, 'DD/MM/YYYY HH12:MI') as daybookinginitString,
	bk.daybookingend,
	to_char(bk.daybookingend, 'DD/MM/YYYY HH12:MI') as daybookingendString,
	bk.checkin,
	to_char(bk.checkin, 'DD/MM/YYYY HH12:MI') as checkinString,
	bk.checkout,
	to_char(bk.checkout, 'DD/MM/YYYY HH12:MI') as checkoutString,

	r.roomnumber as numberroom,
	rt.roomtypename as typeroom,
	r.roomname as nameRoom,
	r.capacity as capacity,
	CONCAT(uc.firstname, ' ', uc.lastname) as client,
	dt.documenttypedesc as typeDocument,
	uc.documentnumber as numberDocument,

	bk.costfinal as costTotal,
	CONCAT('S/', bk.costfinal) as costTotalString,

	null as numberAdult,
	null as numberChild,
	null as numberBaby

FROM booking bk
JOIN roomoffer ro ON ro.roomofferid = bk.roomofferid
JOIN userclient uc ON uc.userclientid = bk.userclientid
JOIN documenttype dt ON dt.documenttypeid = uc.documenttypeid
JOIN room r ON r.roomid = ro.roomid
JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid;

CREATE OR REPLACE VIEW viewadminbookingavailabilityreturn AS
SELECT
	r.roomid,
	bk.roomofferid,
	rt.roomtypeid,
	bk.bookingid,
	bk.bookingstateid,
	bk.userclientid,

	bk.createdat as dateCreate,
	to_char(bk.createdat, 'DD/MM/YYYY HH12:MI') as dateCreateString,
	bk.bookingid as numberBooking,

	bk.daybookinginit,
	to_char(bk.daybookinginit, 'DD/MM/YYYY HH12:MI') as daybookinginitString,
	bk.daybookingend,
	to_char(bk.daybookingend, 'DD/MM/YYYY HH12:MI') as daybookingendString,
	bk.checkin,
	to_char(bk.checkin, 'DD/MM/YYYY HH12:MI') as checkinString,
	bk.checkout,
	to_char(bk.checkout, 'DD/MM/YYYY HH12:MI') as checkoutString,

	r.roomnumber as numberroom,
	rt.roomtypename as typeroom,
	r.roomname as nameRoom,
	r.capacity as capacity,
	CONCAT(uc.firstname, ' ', uc.lastname) as client,
	dt.documenttypedesc as typeDocument,
	uc.documentnumber as numberDocument,

	bk.costfinal as costTotal,
	CONCAT('S/', bk.costfinal) as costTotalString,

	null as numberAdult,
	null as numberChild,
	null as numberBaby,

	bks.bookingstatename as state

FROM booking bk
JOIN roomoffer ro ON ro.roomofferid = bk.roomofferid
JOIN userclient uc ON uc.userclientid = bk.userclientid
JOIN documenttype dt ON dt.documenttypeid = uc.documenttypeid
JOIN room r ON r.roomid = ro.roomid
JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
JOIN bookingstate bks ON bks.bookingstateid = bk.bookingstateid;