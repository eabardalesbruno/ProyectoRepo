SELECT r.roomid,
    bk.roomofferid,
    rt.roomtypeid,
    bk.bookingid,
    bk.bookingstateid,
    bk.userclientid,
    bk.createdat AS datecreate,
    to_char(bk.createdat, 'DD/MM/YYYY HH12:MI'::text) AS datecreatestring,
    bk.bookingid AS numberbooking,
    bk.daybookinginit,
    to_char(bk.daybookinginit, 'DD/MM/YYYY HH12:MI'::text) AS daybookinginitstring,
    bk.daybookingend,
    to_char(bk.daybookingend, 'DD/MM/YYYY HH12:MI'::text) AS daybookingendstring,
    bk.checkin,
    to_char(bk.checkin, 'DD/MM/YYYY HH12:MI'::text) AS checkinstring,
    bk.checkout,
    to_char(bk.checkout, 'DD/MM/YYYY HH12:MI'::text) AS checkoutstring,
    r.roomnumber AS numberroom,
    rt.roomtypename AS typeroom,
    r.roomname AS nameroom,
    r.capacity,
    concat(uc.firstname, ' ', uc.lastname) AS client,
    dt.documenttypedesc AS typedocument,
    uc.documentnumber AS numberdocument,

    bk.costfinal AS costtotal,
    concat('S/', bk.costfinal) AS costtotalstring,

    COALESCE(
        (SELECT ROUND(SUM(pma.amount::NUMERIC) / 100000, 2)
         FROM pay_me_authorizations pma
         WHERE pma.idbooking = bk.bookingid),
        0.00) AS amountpaid,
    concat('S/', COALESCE(
        (SELECT ROUND(SUM(pma.amount::NUMERIC) / 100000, 2)
         FROM pay_me_authorizations pma
         WHERE pma.idbooking = bk.bookingid),
        0.00)) AS amountpaidstring,
    COALESCE(
        (SELECT ROUND(bk.costfinal::NUMERIC - ROUND(SUM(pma.amount::NUMERIC) / 100000, 2), 2)
         FROM pay_me_authorizations pma
         WHERE pma.idbooking = bk.bookingid),
        bk.costfinal) AS remainingamount,
    concat('S/', COALESCE(
        (SELECT ROUND(bk.costfinal::NUMERIC - ROUND(SUM(pma.amount::NUMERIC) / 100000, 2), 2)
         FROM pay_me_authorizations pma
         WHERE pma.idbooking = bk.bookingid),
        bk.costfinal)) AS remainingamountstring,

    bk.numberadults AS numberadult,
    bk.numberchildren AS numberchild,
    bk.numberbabies AS numberbaby,
    bks.bookingstatename AS state
   FROM booking bk
     JOIN roomoffer ro ON ro.roomofferid = bk.roomofferid
     JOIN userclient uc ON uc.userclientid = bk.userclientid
     JOIN documenttype dt ON dt.documenttypeid = uc.documenttypeid
     JOIN room r ON r.roomid = ro.roomid
     JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
     JOIN bookingstate bks ON bks.bookingstateid = bk.bookingstateid;




WITH pay_me AS (
    SELECT
        pma.idbooking,
        ROUND(SUM(pma.amount::NUMERIC) / 100000, 2) AS total_amount
    FROM
        pay_me_authorizations pma
    GROUP BY
        pma.idbooking
)

SELECT
    r.roomid,
    bk.roomofferid,
    rt.roomtypeid,
    bk.bookingid,
    bk.bookingstateid,
    bk.userclientid,
    bk.createdat AS datecreate,
    to_char(bk.createdat, 'DD/MM/YYYY HH12:MI'::text) AS datecreatestring,
    bk.bookingid AS numberbooking,
    bk.daybookinginit,
    to_char(bk.daybookinginit, 'DD/MM/YYYY HH12:MI'::text) AS daybookinginitstring,
    bk.daybookingend,
    to_char(bk.daybookingend, 'DD/MM/YYYY HH12:MI'::text) AS daybookingendstring,
    bk.checkin,
    to_char(bk.checkin, 'DD/MM/YYYY HH12:MI'::text) AS checkinstring,
    bk.checkout,
    to_char(bk.checkout, 'DD/MM/YYYY HH12:MI'::text) AS checkoutstring,
    r.roomnumber AS numberroom,
    rt.roomtypename AS typeroom,
    r.roomname AS nameroom,
    r.capacity,
    concat(uc.firstname, ' ', uc.lastname) AS client,
    dt.documenttypedesc AS typedocument,
    uc.documentnumber AS numberdocument,

    bk.costfinal AS costtotal,
    concat('S/', bk.costfinal) AS costtotalstring,

    COALESCE(pay_me.total_amount, 0.00) AS amountpaid,
    concat('S/', COALESCE(pay_me.total_amount, 0.00)) AS amountpaidstring,
    COALESCE(bk.costfinal - COALESCE(pay_me.total_amount, 0.00), bk.costfinal) AS remainingamount,
    concat('S/', COALESCE(bk.costfinal - COALESCE(pay_me.total_amount, 0.00), bk.costfinal)) AS remainingamountstring,

    bk.numberadults AS numberadult,
    bk.numberchildren AS numberchild,
    bk.numberbabies AS numberbaby,
    bks.bookingstatename AS state
FROM
    booking bk
    JOIN roomoffer ro ON ro.roomofferid = bk.roomofferid
    JOIN userclient uc ON uc.userclientid = bk.userclientid
    JOIN documenttype dt ON dt.documenttypeid = uc.documenttypeid
    JOIN room r ON r.roomid = ro.roomid
    JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
    JOIN bookingstate bks ON bks.bookingstateid = bk.bookingstateid
    LEFT JOIN pay_me ON pay_me.idbooking = bk.bookingid;
