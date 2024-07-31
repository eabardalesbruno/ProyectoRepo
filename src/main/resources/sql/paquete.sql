-- Enum types
CREATE TYPE paquete AS ENUM ('FULL_DAY', 'CAMPING', 'EVENTOS');
CREATE TYPE habitaciones_tipo AS ENUM ('HABITACIONES_DUPLEX', 'HABITACIONES_MATRIMONIALES');
CREATE TYPE departamento_tipo AS ENUM ('DEPARTAMENTO_VISTA_PISCINA', 'DEPARTAMMENTO_VISTA_JARDIN', 'DEPARTAMENTO_VISTA_RIO');
CREATE TYPE restaurante AS ENUM ('DESAYUNO', 'ENTRADAS', 'PLATOS_A_LA_CARTA', 'TODOS');
CREATE TYPE bebidas AS ENUM ('DESAYUNO', 'ENTRADAS', 'PLATOS_A_LA_CARTA', 'TODOS');
CREATE TYPE entretenimiento AS ENUM ('YENGA_FAMILIAR', 'TWISTER_GIGANTE', 'GYNCANA', 'TIRA_DE_LA_CUERDA', 'TALLER_DE_DIBUJO_LIBRE', 'FOLLOW_THE_LEADER', 'CARITAS_PINTADAS', 'VOLEY', 'FUTBOL', 'PISCINA', 'AGUAGYM', 'BINGO', 'TODAS_LAS_ACTIVIDADES_RECREATIVAS');
CREATE TYPE comodidades AS ENUM ('CARPA', 'HAMACA', 'ESTACIONAMIENTO_24_HORAS', 'TODOS');
CREATE TYPE servicios_adicionales AS ENUM ('KARAOKE', 'ORQUESTA', 'SHOW_INFANTIL', 'CAMPING', 'ANIMACION', 'OTROS');
CREATE TYPE tipo_descuento AS ENUM ('PORCENTAJE', 'MONTO');

-- Table creation
CREATE TABLE package_offer (
    package_offer_id SERIAL PRIMARY KEY,
    paquete paquete,
    offer_name VARCHAR(255),
    url_image VARCHAR(255),
    room_type_habitacion habitaciones_tipo,
    room_type_departamento departamento_tipo,
    restaurant restaurante[],
    bebidas bebidas[],
    entretenimiento entretenimiento[],
    comodidades comodidades[],
    detalle_del_paquete TEXT,
    terminos_condiciones TEXT,
    politicas_cancelacion TEXT,
    date_init DATE,
    date_end DATE,
    time_init TIME,
    time_end TIME,
    number_days INTEGER,
    number_nights INTEGER,
    is_all_day BOOLEAN,
    adultos INTEGER,
    ninos INTEGER,
    infantes INTEGER,
    servicios_adicionales servicios_adicionales[],
    cost_adulto NUMERIC(10, 2),
    cost_adulto_extra NUMERIC(10, 2),
    cost_nino NUMERIC(10, 2),
    is_socio BOOLEAN,
    cost_adulto_canje NUMERIC(10, 2),
    cost_adulto_extra_canje NUMERIC(10, 2),
    cost_nino_canje NUMERIC(10, 2),
    is_promocion BOOLEAN,
    lista_descuentos JSONB,
    ribera_points_semana INTEGER,
    ribera_points_fin_de_semana INTEGER,
    in_resort_points_semana INTEGER,
    in_resort_points_fin_de_semana INTEGER,
    points INTEGER
);