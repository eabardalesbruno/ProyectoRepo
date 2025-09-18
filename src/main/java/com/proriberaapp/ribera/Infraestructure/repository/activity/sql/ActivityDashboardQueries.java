package com.proriberaapp.ribera.Infraestructure.repository.activity.sql;

public class ActivityDashboardQueries {
    public static final String BASE_QUERY = """
            SELECT
                r.roomid,
                r.roomnumber,
                r.roomname,
                rt.roomtypename,
                b.bookingid,
                b.daybookinginit,
                b.daybookingend,
                b.numberadults,
                b.numberchildren,
                b.numberbabies,
                b.numberadultsextra,
                b.numberadultsmayor,
                b.bookingstateid,
                uc.firstname,
                uc.lastname,
                uc.isuserinclub,
                CASE COALESCE(uc.isuserinclub, false)
                    WHEN true THEN 'Socio'
                    ELSE 'Externo'
                END as client_type,
                CASE 
                    WHEN b.daybookingend IS NOT NULL AND b.daybookinginit IS NOT NULL 
                    THEN COALESCE(DATE_PART('day', b.daybookingend - b.daybookinginit), 0)
                    ELSE 0 
                END as total_nights,
                COALESCE(b.numberadults, 0) + COALESCE(b.numberadultsextra, 0) + COALESCE(b.numberadultsmayor, 0) as total_adults,
                COALESCE(b.numberchildren, 0) + COALESCE(b.numberbabies, 0) as total_children,
                CASE 
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 30 THEN '30 dias'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 21 THEN '21 dias'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 14 THEN '14 dias'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 7 THEN '7 dias'
                    ELSE '48 horas'
                END as reservation_time,
                CASE 
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 30 AND COALESCE(uc.isuserinclub, false) = true THEN '24 horas'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 21 AND COALESCE(uc.isuserinclub, false) = true THEN '18 horas'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 14 THEN '12 horas'
                    WHEN DATE_PART('day', b.daybookingend - b.daybookinginit) >= 7 AND COALESCE(uc.isuserinclub, false) = true THEN '6 horas'
                    ELSE '2 horas'
                END as standby_time,
                CASE
                    WHEN b.bookingid IS NULL THEN 'DISPONIBLE'
                    WHEN COALESCE(b.bookingstateid, 0) = 1 THEN 'NO SHOW'
                    WHEN COALESCE(b.bookingstateid, 0) = 3 THEN 'RESERVADO'
                    WHEN COALESCE(pb.paymentstateid, 0) = 2 THEN 'PAGADO'
                    ELSE 'OTRO'
                END as status,
                pb.paymentstateid,
                pm.description as payment_method,
                bf.bookingfeedingid IS NOT NULL as has_feeding
            FROM room r
                LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                    AND DATE(b.daybookinginit) <= :dateStart
                    AND DATE(b.daybookingend) >= :dateEnd
                LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
                LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
            WHERE 1=1
            """;

    public static final String SEARCH_FILTER = 
        " AND (LOWER(r.roomname) LIKE LOWER(:search) OR LOWER(uc.firstname) LIKE LOWER(:search) OR LOWER(uc.lastname) LIKE LOWER(:search))";

    public static final String CLIENT_TYPE_FILTER = 
        " AND CASE WHEN uc.isuserinclub = true THEN 'Socio' ELSE 'Externo' END = :clientType";

    public static final String PAYMENT_TYPE_FILTER = 
        " AND pm.description = :paymentType";

    public static final String ROOM_TYPE_FILTER = 
        " AND rt.roomtypename = :roomType";

    public static final String STATUS_FILTER = 
        " AND CASE WHEN b.bookingid IS NULL THEN 'DISPONIBLE' " +
        "WHEN b.bookingstateid = 1 THEN 'NO SHOW' " +
        "WHEN b.bookingstateid = 3 THEN 'RESERVADO' " +
        "WHEN pb.paymentstateid = 2 THEN 'PAGADO' " +
        "ELSE 'OTRO' END = :status";

    public static final String ORDER_BY = " ORDER BY r.roomnumber";

    public static final String COUNT_QUERY = """
            SELECT COUNT(1) as total
            FROM room r
                LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                    AND DATE(b.daybookinginit) <= :dateStart
                    AND DATE(b.daybookingend) >= :dateEnd
                LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
                LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
            WHERE 1=1
            """;
}
