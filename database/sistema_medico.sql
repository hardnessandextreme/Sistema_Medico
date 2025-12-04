--
-- PostgreSQL database dump
--

\restrict mM4dh1DluE9fHgqXFWCzntoUDHo4i6jHuOee92Pp8dKfChWWacep7TCtjDYqz2V

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.usuarios DROP CONSTRAINT IF EXISTS usuarios_id_rol_fkey;
ALTER TABLE IF EXISTS ONLY public.tratamientos DROP CONSTRAINT IF EXISTS tratamientos_id_medicamento_fkey;
ALTER TABLE IF EXISTS ONLY public.tratamientos DROP CONSTRAINT IF EXISTS tratamientos_id_consulta_fkey;
ALTER TABLE IF EXISTS ONLY public.seguimientos_tratamiento DROP CONSTRAINT IF EXISTS seguimientos_tratamiento_id_tratamiento_fkey;
ALTER TABLE IF EXISTS ONLY public.seguimientos_tratamiento DROP CONSTRAINT IF EXISTS seguimientos_tratamiento_id_paciente_fkey;
ALTER TABLE IF EXISTS ONLY public.recepcionistas DROP CONSTRAINT IF EXISTS recepcionistas_id_usuario_fkey;
ALTER TABLE IF EXISTS ONLY public.pacientes DROP CONSTRAINT IF EXISTS pacientes_id_usuario_fkey;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_id_usuario_fkey;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_id_especialidad_fkey;
ALTER TABLE IF EXISTS ONLY public.horarios_medicos DROP CONSTRAINT IF EXISTS horarios_medicos_id_medico_fkey;
ALTER TABLE IF EXISTS ONLY public.examenes_laboratorio DROP CONSTRAINT IF EXISTS examenes_laboratorio_id_consulta_fkey;
ALTER TABLE IF EXISTS ONLY public.consultas DROP CONSTRAINT IF EXISTS consultas_id_paciente_fkey;
ALTER TABLE IF EXISTS ONLY public.consultas DROP CONSTRAINT IF EXISTS consultas_id_medico_fkey;
ALTER TABLE IF EXISTS ONLY public.consultas DROP CONSTRAINT IF EXISTS consultas_id_cita_fkey;
ALTER TABLE IF EXISTS ONLY public.citas DROP CONSTRAINT IF EXISTS citas_id_paciente_fkey;
ALTER TABLE IF EXISTS ONLY public.citas DROP CONSTRAINT IF EXISTS citas_id_medico_fkey;
ALTER TABLE IF EXISTS ONLY public.citas DROP CONSTRAINT IF EXISTS citas_id_estado_cita_fkey;
ALTER TABLE IF EXISTS ONLY public.auditorias DROP CONSTRAINT IF EXISTS auditorias_id_usuario_fkey;
ALTER TABLE IF EXISTS ONLY public.archivos_medicos DROP CONSTRAINT IF EXISTS archivos_medicos_subido_por_fkey;
ALTER TABLE IF EXISTS ONLY public.archivos_medicos DROP CONSTRAINT IF EXISTS archivos_medicos_id_paciente_fkey;
ALTER TABLE IF EXISTS ONLY public.archivos_medicos DROP CONSTRAINT IF EXISTS archivos_medicos_id_consulta_fkey;
DROP INDEX IF EXISTS public.idx_usuarios_rol;
DROP INDEX IF EXISTS public.idx_usuarios_email;
DROP INDEX IF EXISTS public.idx_tratamientos_consulta;
DROP INDEX IF EXISTS public.idx_pacientes_cedula;
DROP INDEX IF EXISTS public.idx_pacientes_activo;
DROP INDEX IF EXISTS public.idx_medicos_especialidad;
DROP INDEX IF EXISTS public.idx_consultas_paciente;
DROP INDEX IF EXISTS public.idx_consultas_fecha;
DROP INDEX IF EXISTS public.idx_citas_paciente;
DROP INDEX IF EXISTS public.idx_citas_medico;
DROP INDEX IF EXISTS public.idx_citas_fecha;
DROP INDEX IF EXISTS public.idx_citas_estado;
ALTER TABLE IF EXISTS ONLY public.usuarios DROP CONSTRAINT IF EXISTS usuarios_pkey;
ALTER TABLE IF EXISTS ONLY public.usuarios DROP CONSTRAINT IF EXISTS usuarios_nombre_usuario_key;
ALTER TABLE IF EXISTS ONLY public.usuarios DROP CONSTRAINT IF EXISTS usuarios_email_key;
ALTER TABLE IF EXISTS ONLY public.tratamientos DROP CONSTRAINT IF EXISTS tratamientos_pkey;
ALTER TABLE IF EXISTS ONLY public.seguimientos_tratamiento DROP CONSTRAINT IF EXISTS seguimientos_tratamiento_pkey;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_pkey;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_nombre_rol_key;
ALTER TABLE IF EXISTS ONLY public.recepcionistas DROP CONSTRAINT IF EXISTS recepcionistas_pkey;
ALTER TABLE IF EXISTS ONLY public.recepcionistas DROP CONSTRAINT IF EXISTS recepcionistas_id_usuario_key;
ALTER TABLE IF EXISTS ONLY public.recepcionistas DROP CONSTRAINT IF EXISTS recepcionistas_cedula_key;
ALTER TABLE IF EXISTS ONLY public.pacientes DROP CONSTRAINT IF EXISTS pacientes_pkey;
ALTER TABLE IF EXISTS ONLY public.pacientes DROP CONSTRAINT IF EXISTS pacientes_id_usuario_key;
ALTER TABLE IF EXISTS ONLY public.pacientes DROP CONSTRAINT IF EXISTS pacientes_cedula_key;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_pkey;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_numero_licencia_key;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_id_usuario_key;
ALTER TABLE IF EXISTS ONLY public.medicos DROP CONSTRAINT IF EXISTS medicos_cedula_key;
ALTER TABLE IF EXISTS ONLY public.medicamentos DROP CONSTRAINT IF EXISTS medicamentos_pkey;
ALTER TABLE IF EXISTS ONLY public.horarios_medicos DROP CONSTRAINT IF EXISTS horarios_medicos_pkey;
ALTER TABLE IF EXISTS ONLY public.examenes_laboratorio DROP CONSTRAINT IF EXISTS examenes_laboratorio_pkey;
ALTER TABLE IF EXISTS ONLY public.estados_cita DROP CONSTRAINT IF EXISTS estados_cita_pkey;
ALTER TABLE IF EXISTS ONLY public.estados_cita DROP CONSTRAINT IF EXISTS estados_cita_nombre_estado_key;
ALTER TABLE IF EXISTS ONLY public.especialidades DROP CONSTRAINT IF EXISTS especialidades_pkey;
ALTER TABLE IF EXISTS ONLY public.especialidades DROP CONSTRAINT IF EXISTS especialidades_nombre_especialidad_key;
ALTER TABLE IF EXISTS ONLY public.consultas DROP CONSTRAINT IF EXISTS consultas_pkey;
ALTER TABLE IF EXISTS ONLY public.consultas DROP CONSTRAINT IF EXISTS consultas_id_cita_key;
ALTER TABLE IF EXISTS ONLY public.citas DROP CONSTRAINT IF EXISTS citas_pkey;
ALTER TABLE IF EXISTS ONLY public.auditorias DROP CONSTRAINT IF EXISTS auditorias_pkey;
ALTER TABLE IF EXISTS ONLY public.archivos_medicos DROP CONSTRAINT IF EXISTS archivos_medicos_pkey;
ALTER TABLE IF EXISTS public.usuarios ALTER COLUMN id_usuario DROP DEFAULT;
ALTER TABLE IF EXISTS public.tratamientos ALTER COLUMN id_tratamiento DROP DEFAULT;
ALTER TABLE IF EXISTS public.seguimientos_tratamiento ALTER COLUMN id_seguimiento DROP DEFAULT;
ALTER TABLE IF EXISTS public.roles ALTER COLUMN id_rol DROP DEFAULT;
ALTER TABLE IF EXISTS public.recepcionistas ALTER COLUMN id_recepcionista DROP DEFAULT;
ALTER TABLE IF EXISTS public.pacientes ALTER COLUMN id_paciente DROP DEFAULT;
ALTER TABLE IF EXISTS public.medicos ALTER COLUMN id_medico DROP DEFAULT;
ALTER TABLE IF EXISTS public.medicamentos ALTER COLUMN id_medicamento DROP DEFAULT;
ALTER TABLE IF EXISTS public.horarios_medicos ALTER COLUMN id_horario DROP DEFAULT;
ALTER TABLE IF EXISTS public.examenes_laboratorio ALTER COLUMN id_examen DROP DEFAULT;
ALTER TABLE IF EXISTS public.estados_cita ALTER COLUMN id_estado_cita DROP DEFAULT;
ALTER TABLE IF EXISTS public.especialidades ALTER COLUMN id_especialidad DROP DEFAULT;
ALTER TABLE IF EXISTS public.consultas ALTER COLUMN id_consulta DROP DEFAULT;
ALTER TABLE IF EXISTS public.citas ALTER COLUMN id_cita DROP DEFAULT;
ALTER TABLE IF EXISTS public.auditorias ALTER COLUMN id_auditoria DROP DEFAULT;
ALTER TABLE IF EXISTS public.archivos_medicos ALTER COLUMN id_archivo DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.usuarios_id_usuario_seq;
DROP TABLE IF EXISTS public.usuarios;
DROP SEQUENCE IF EXISTS public.tratamientos_id_tratamiento_seq;
DROP TABLE IF EXISTS public.tratamientos;
DROP SEQUENCE IF EXISTS public.seguimientos_tratamiento_id_seguimiento_seq;
DROP TABLE IF EXISTS public.seguimientos_tratamiento;
DROP SEQUENCE IF EXISTS public.roles_id_rol_seq;
DROP TABLE IF EXISTS public.roles;
DROP SEQUENCE IF EXISTS public.recepcionistas_id_recepcionista_seq;
DROP TABLE IF EXISTS public.recepcionistas;
DROP SEQUENCE IF EXISTS public.pacientes_id_paciente_seq;
DROP TABLE IF EXISTS public.pacientes;
DROP SEQUENCE IF EXISTS public.medicos_id_medico_seq;
DROP TABLE IF EXISTS public.medicos;
DROP SEQUENCE IF EXISTS public.medicamentos_id_medicamento_seq;
DROP TABLE IF EXISTS public.medicamentos;
DROP SEQUENCE IF EXISTS public.horarios_medicos_id_horario_seq;
DROP TABLE IF EXISTS public.horarios_medicos;
DROP SEQUENCE IF EXISTS public.examenes_laboratorio_id_examen_seq;
DROP TABLE IF EXISTS public.examenes_laboratorio;
DROP SEQUENCE IF EXISTS public.estados_cita_id_estado_cita_seq;
DROP TABLE IF EXISTS public.estados_cita;
DROP SEQUENCE IF EXISTS public.especialidades_id_especialidad_seq;
DROP TABLE IF EXISTS public.especialidades;
DROP SEQUENCE IF EXISTS public.consultas_id_consulta_seq;
DROP TABLE IF EXISTS public.consultas;
DROP SEQUENCE IF EXISTS public.citas_id_cita_seq;
DROP TABLE IF EXISTS public.citas;
DROP SEQUENCE IF EXISTS public.auditorias_id_auditoria_seq;
DROP TABLE IF EXISTS public.auditorias;
DROP SEQUENCE IF EXISTS public.archivos_medicos_id_archivo_seq;
DROP TABLE IF EXISTS public.archivos_medicos;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: archivos_medicos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.archivos_medicos (
    id_archivo integer NOT NULL,
    id_paciente integer NOT NULL,
    id_consulta integer,
    tipo_archivo character varying(50) NOT NULL,
    nombre_archivo character varying(255) NOT NULL,
    ruta_archivo character varying(500) NOT NULL,
    descripcion text,
    tamanio_kb integer,
    fecha_subida timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    subido_por integer
);


ALTER TABLE public.archivos_medicos OWNER TO postgres;

--
-- Name: archivos_medicos_id_archivo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.archivos_medicos_id_archivo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.archivos_medicos_id_archivo_seq OWNER TO postgres;

--
-- Name: archivos_medicos_id_archivo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.archivos_medicos_id_archivo_seq OWNED BY public.archivos_medicos.id_archivo;


--
-- Name: auditorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auditorias (
    id_auditoria integer NOT NULL,
    id_usuario integer,
    tabla_afectada character varying(100),
    operacion character varying(20),
    registro_id integer,
    datos_anteriores jsonb,
    datos_nuevos jsonb,
    fecha_operacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    direccion_ip character varying(50)
);


ALTER TABLE public.auditorias OWNER TO postgres;

--
-- Name: auditorias_id_auditoria_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.auditorias_id_auditoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.auditorias_id_auditoria_seq OWNER TO postgres;

--
-- Name: auditorias_id_auditoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.auditorias_id_auditoria_seq OWNED BY public.auditorias.id_auditoria;


--
-- Name: citas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.citas (
    id_cita integer NOT NULL,
    id_paciente integer NOT NULL,
    id_medico integer NOT NULL,
    fecha_cita date NOT NULL,
    hora_cita time without time zone NOT NULL,
    motivo_consulta text NOT NULL,
    sintomas text,
    id_estado_cita integer DEFAULT 1 NOT NULL,
    notas_recepcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    fecha_confirmacion timestamp without time zone,
    fecha_cancelacion timestamp without time zone,
    motivo_cancelacion text,
    recordatorio_enviado boolean DEFAULT false,
    activo boolean DEFAULT true
);


ALTER TABLE public.citas OWNER TO postgres;

--
-- Name: TABLE citas; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.citas IS 'Citas médicas programadas';


--
-- Name: citas_id_cita_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.citas_id_cita_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.citas_id_cita_seq OWNER TO postgres;

--
-- Name: citas_id_cita_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.citas_id_cita_seq OWNED BY public.citas.id_cita;


--
-- Name: consultas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consultas (
    id_consulta integer NOT NULL,
    id_cita integer NOT NULL,
    id_paciente integer NOT NULL,
    id_medico integer NOT NULL,
    fecha_consulta timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    motivo_consulta text NOT NULL,
    sintomas_presentados text,
    signos_vitales text,
    examen_fisico text,
    diagnostico text NOT NULL,
    observaciones text,
    recomendaciones text,
    proxima_cita date
);


ALTER TABLE public.consultas OWNER TO postgres;

--
-- Name: TABLE consultas; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.consultas IS 'Registro de consultas médicas realizadas';


--
-- Name: consultas_id_consulta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.consultas_id_consulta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.consultas_id_consulta_seq OWNER TO postgres;

--
-- Name: consultas_id_consulta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.consultas_id_consulta_seq OWNED BY public.consultas.id_consulta;


--
-- Name: especialidades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.especialidades (
    id_especialidad integer NOT NULL,
    nombre_especialidad character varying(100) NOT NULL,
    descripcion text,
    activo boolean DEFAULT true
);


ALTER TABLE public.especialidades OWNER TO postgres;

--
-- Name: especialidades_id_especialidad_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.especialidades_id_especialidad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.especialidades_id_especialidad_seq OWNER TO postgres;

--
-- Name: especialidades_id_especialidad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.especialidades_id_especialidad_seq OWNED BY public.especialidades.id_especialidad;


--
-- Name: estados_cita; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estados_cita (
    id_estado_cita integer NOT NULL,
    nombre_estado character varying(50) NOT NULL,
    descripcion text,
    color_hex character varying(7)
);


ALTER TABLE public.estados_cita OWNER TO postgres;

--
-- Name: estados_cita_id_estado_cita_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.estados_cita_id_estado_cita_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.estados_cita_id_estado_cita_seq OWNER TO postgres;

--
-- Name: estados_cita_id_estado_cita_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.estados_cita_id_estado_cita_seq OWNED BY public.estados_cita.id_estado_cita;


--
-- Name: examenes_laboratorio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.examenes_laboratorio (
    id_examen integer NOT NULL,
    id_consulta integer NOT NULL,
    tipo_examen character varying(150) NOT NULL,
    descripcion text,
    fecha_solicitud date NOT NULL,
    fecha_resultado date,
    resultado text,
    archivo_resultado_url character varying(255),
    laboratorio character varying(150),
    estado character varying(50) DEFAULT 'pendiente'::character varying
);


ALTER TABLE public.examenes_laboratorio OWNER TO postgres;

--
-- Name: examenes_laboratorio_id_examen_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.examenes_laboratorio_id_examen_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.examenes_laboratorio_id_examen_seq OWNER TO postgres;

--
-- Name: examenes_laboratorio_id_examen_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.examenes_laboratorio_id_examen_seq OWNED BY public.examenes_laboratorio.id_examen;


--
-- Name: horarios_medicos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.horarios_medicos (
    id_horario integer NOT NULL,
    id_medico integer NOT NULL,
    dia_semana integer NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fin time without time zone NOT NULL,
    activo boolean DEFAULT true,
    CONSTRAINT horarios_medicos_dia_semana_check CHECK (((dia_semana >= 0) AND (dia_semana <= 6)))
);


ALTER TABLE public.horarios_medicos OWNER TO postgres;

--
-- Name: horarios_medicos_id_horario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.horarios_medicos_id_horario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.horarios_medicos_id_horario_seq OWNER TO postgres;

--
-- Name: horarios_medicos_id_horario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.horarios_medicos_id_horario_seq OWNED BY public.horarios_medicos.id_horario;


--
-- Name: medicamentos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medicamentos (
    id_medicamento integer NOT NULL,
    nombre_comercial character varying(150) NOT NULL,
    nombre_generico character varying(150) NOT NULL,
    presentacion character varying(100),
    concentracion character varying(50),
    fabricante character varying(100),
    activo boolean DEFAULT true
);


ALTER TABLE public.medicamentos OWNER TO postgres;

--
-- Name: medicamentos_id_medicamento_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.medicamentos_id_medicamento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medicamentos_id_medicamento_seq OWNER TO postgres;

--
-- Name: medicamentos_id_medicamento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.medicamentos_id_medicamento_seq OWNED BY public.medicamentos.id_medicamento;


--
-- Name: medicos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medicos (
    id_medico integer NOT NULL,
    id_usuario integer NOT NULL,
    nombres character varying(100) NOT NULL,
    apellidos character varying(100) NOT NULL,
    cedula character varying(20) NOT NULL,
    id_especialidad integer NOT NULL,
    numero_licencia character varying(50),
    telefono character varying(20),
    fecha_nacimiento date,
    direccion text,
    foto_url character varying(255),
    fecha_contratacion date,
    activo boolean DEFAULT true
);


ALTER TABLE public.medicos OWNER TO postgres;

--
-- Name: TABLE medicos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.medicos IS 'Información profesional de médicos';


--
-- Name: medicos_id_medico_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.medicos_id_medico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medicos_id_medico_seq OWNER TO postgres;

--
-- Name: medicos_id_medico_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.medicos_id_medico_seq OWNED BY public.medicos.id_medico;


--
-- Name: pacientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pacientes (
    id_paciente integer NOT NULL,
    id_usuario integer,
    nombres character varying(100) NOT NULL,
    apellidos character varying(100) NOT NULL,
    cedula character varying(20) NOT NULL,
    fecha_nacimiento date NOT NULL,
    edad integer,
    genero character varying(20),
    telefono character varying(20) NOT NULL,
    email character varying(150),
    direccion text,
    ciudad character varying(100),
    provincia character varying(100),
    seguro_medico character varying(100),
    numero_seguro character varying(100),
    tipo_sangre character varying(5),
    alergias text,
    enfermedades_cronicas text,
    contacto_emergencia_nombre character varying(150),
    contacto_emergencia_telefono character varying(20),
    contacto_emergencia_relacion character varying(50),
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    activo boolean DEFAULT true
);


ALTER TABLE public.pacientes OWNER TO postgres;

--
-- Name: TABLE pacientes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.pacientes IS 'Información demográfica y médica de pacientes';


--
-- Name: pacientes_id_paciente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pacientes_id_paciente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pacientes_id_paciente_seq OWNER TO postgres;

--
-- Name: pacientes_id_paciente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pacientes_id_paciente_seq OWNED BY public.pacientes.id_paciente;


--
-- Name: recepcionistas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recepcionistas (
    id_recepcionista integer NOT NULL,
    id_usuario integer NOT NULL,
    nombres character varying(100) NOT NULL,
    apellidos character varying(100) NOT NULL,
    cedula character varying(20) NOT NULL,
    telefono character varying(20),
    fecha_nacimiento date,
    direccion text,
    fecha_contratacion date,
    activo boolean DEFAULT true
);


ALTER TABLE public.recepcionistas OWNER TO postgres;

--
-- Name: recepcionistas_id_recepcionista_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.recepcionistas_id_recepcionista_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recepcionistas_id_recepcionista_seq OWNER TO postgres;

--
-- Name: recepcionistas_id_recepcionista_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.recepcionistas_id_recepcionista_seq OWNED BY public.recepcionistas.id_recepcionista;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id_rol integer NOT NULL,
    nombre_rol character varying(50) NOT NULL,
    descripcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: TABLE roles; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.roles IS 'Roles de usuarios: Administrador, Médico, Recepcionista, Paciente';


--
-- Name: roles_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_rol_seq OWNER TO postgres;

--
-- Name: roles_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_rol_seq OWNED BY public.roles.id_rol;


--
-- Name: seguimientos_tratamiento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seguimientos_tratamiento (
    id_seguimiento integer NOT NULL,
    id_tratamiento integer NOT NULL,
    id_paciente integer NOT NULL,
    fecha_seguimiento date NOT NULL,
    cumpliendo_tratamiento character varying(20) NOT NULL,
    notas text,
    reportado_por character varying(50),
    CONSTRAINT seguimientos_tratamiento_cumpliendo_tratamiento_check CHECK (((cumpliendo_tratamiento)::text = ANY ((ARRAY['si'::character varying, 'no'::character varying, 'parcial'::character varying])::text[])))
);


ALTER TABLE public.seguimientos_tratamiento OWNER TO postgres;

--
-- Name: seguimientos_tratamiento_id_seguimiento_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seguimientos_tratamiento_id_seguimiento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seguimientos_tratamiento_id_seguimiento_seq OWNER TO postgres;

--
-- Name: seguimientos_tratamiento_id_seguimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.seguimientos_tratamiento_id_seguimiento_seq OWNED BY public.seguimientos_tratamiento.id_seguimiento;


--
-- Name: tratamientos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tratamientos (
    id_tratamiento integer NOT NULL,
    id_consulta integer NOT NULL,
    id_medicamento integer,
    medicamento_texto character varying(200),
    dosis character varying(100) NOT NULL,
    frecuencia character varying(100) NOT NULL,
    duracion character varying(100) NOT NULL,
    via_administracion character varying(50),
    indicaciones text,
    fecha_inicio date,
    fecha_fin date,
    activo boolean DEFAULT true
);


ALTER TABLE public.tratamientos OWNER TO postgres;

--
-- Name: TABLE tratamientos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tratamientos IS 'Tratamientos y medicamentos prescritos';


--
-- Name: tratamientos_id_tratamiento_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tratamientos_id_tratamiento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tratamientos_id_tratamiento_seq OWNER TO postgres;

--
-- Name: tratamientos_id_tratamiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tratamientos_id_tratamiento_seq OWNED BY public.tratamientos.id_tratamiento;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id_usuario integer NOT NULL,
    nombre_usuario character varying(100) NOT NULL,
    contrasena_hash character varying(255) NOT NULL,
    email character varying(150) NOT NULL,
    id_rol integer NOT NULL,
    activo boolean DEFAULT true,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso timestamp without time zone
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Name: TABLE usuarios; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.usuarios IS 'Credenciales y datos de autenticación';


--
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_usuario_seq OWNER TO postgres;

--
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_usuario_seq OWNED BY public.usuarios.id_usuario;


--
-- Name: archivos_medicos id_archivo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archivos_medicos ALTER COLUMN id_archivo SET DEFAULT nextval('public.archivos_medicos_id_archivo_seq'::regclass);


--
-- Name: auditorias id_auditoria; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditorias ALTER COLUMN id_auditoria SET DEFAULT nextval('public.auditorias_id_auditoria_seq'::regclass);


--
-- Name: citas id_cita; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.citas ALTER COLUMN id_cita SET DEFAULT nextval('public.citas_id_cita_seq'::regclass);


--
-- Name: consultas id_consulta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas ALTER COLUMN id_consulta SET DEFAULT nextval('public.consultas_id_consulta_seq'::regclass);


--
-- Name: especialidades id_especialidad; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.especialidades ALTER COLUMN id_especialidad SET DEFAULT nextval('public.especialidades_id_especialidad_seq'::regclass);


--
-- Name: estados_cita id_estado_cita; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados_cita ALTER COLUMN id_estado_cita SET DEFAULT nextval('public.estados_cita_id_estado_cita_seq'::regclass);


--
-- Name: examenes_laboratorio id_examen; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.examenes_laboratorio ALTER COLUMN id_examen SET DEFAULT nextval('public.examenes_laboratorio_id_examen_seq'::regclass);


--
-- Name: horarios_medicos id_horario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horarios_medicos ALTER COLUMN id_horario SET DEFAULT nextval('public.horarios_medicos_id_horario_seq'::regclass);


--
-- Name: medicamentos id_medicamento; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicamentos ALTER COLUMN id_medicamento SET DEFAULT nextval('public.medicamentos_id_medicamento_seq'::regclass);


--
-- Name: medicos id_medico; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos ALTER COLUMN id_medico SET DEFAULT nextval('public.medicos_id_medico_seq'::regclass);


--
-- Name: pacientes id_paciente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes ALTER COLUMN id_paciente SET DEFAULT nextval('public.pacientes_id_paciente_seq'::regclass);


--
-- Name: recepcionistas id_recepcionista; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recepcionistas ALTER COLUMN id_recepcionista SET DEFAULT nextval('public.recepcionistas_id_recepcionista_seq'::regclass);


--
-- Name: roles id_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id_rol SET DEFAULT nextval('public.roles_id_rol_seq'::regclass);


--
-- Name: seguimientos_tratamiento id_seguimiento; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seguimientos_tratamiento ALTER COLUMN id_seguimiento SET DEFAULT nextval('public.seguimientos_tratamiento_id_seguimiento_seq'::regclass);


--
-- Name: tratamientos id_tratamiento; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tratamientos ALTER COLUMN id_tratamiento SET DEFAULT nextval('public.tratamientos_id_tratamiento_seq'::regclass);


--
-- Name: usuarios id_usuario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuarios_id_usuario_seq'::regclass);


--
-- Data for Name: archivos_medicos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.archivos_medicos (id_archivo, id_paciente, id_consulta, tipo_archivo, nombre_archivo, ruta_archivo, descripcion, tamanio_kb, fecha_subida, subido_por) FROM stdin;
\.


--
-- Data for Name: auditorias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.auditorias (id_auditoria, id_usuario, tabla_afectada, operacion, registro_id, datos_anteriores, datos_nuevos, fecha_operacion, direccion_ip) FROM stdin;
\.


--
-- Data for Name: citas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.citas (id_cita, id_paciente, id_medico, fecha_cita, hora_cita, motivo_consulta, sintomas, id_estado_cita, notas_recepcion, fecha_creacion, fecha_confirmacion, fecha_cancelacion, motivo_cancelacion, recordatorio_enviado, activo) FROM stdin;
\.


--
-- Data for Name: consultas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consultas (id_consulta, id_cita, id_paciente, id_medico, fecha_consulta, motivo_consulta, sintomas_presentados, signos_vitales, examen_fisico, diagnostico, observaciones, recomendaciones, proxima_cita) FROM stdin;
\.


--
-- Data for Name: especialidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.especialidades (id_especialidad, nombre_especialidad, descripcion, activo) FROM stdin;
7	Medicina General	Atención médica general y primaria	t
8	Cardiología	Especialidad en enfermedades del corazón	t
9	Pediatría	Atención médica para niños	t
10	Medicina Interna	Diagnóstico y tratamiento de enfermedades internas	t
11	Dermatología	Enfermedades de la piel	t
12	Traumatología	Lesiones y enfermedades del sistema musculoesquelético	t
\.


--
-- Data for Name: estados_cita; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estados_cita (id_estado_cita, nombre_estado, descripcion, color_hex) FROM stdin;
7	Pendiente	Cita agendada, esperando atenciÃ³n	#FFC107
9	Completada	Cita atendida exitosamente	#4CAF50
10	Cancelada	Cita cancelada por paciente o mÃ©dico	#F44336
\.


--
-- Data for Name: examenes_laboratorio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.examenes_laboratorio (id_examen, id_consulta, tipo_examen, descripcion, fecha_solicitud, fecha_resultado, resultado, archivo_resultado_url, laboratorio, estado) FROM stdin;
\.


--
-- Data for Name: horarios_medicos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.horarios_medicos (id_horario, id_medico, dia_semana, hora_inicio, hora_fin, activo) FROM stdin;
\.


--
-- Data for Name: medicamentos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medicamentos (id_medicamento, nombre_comercial, nombre_generico, presentacion, concentracion, fabricante, activo) FROM stdin;
1	Paracetamol 500	Paracetamol	Tabletas	500mg	\N	t
2	Ibuprofeno	Ibuprofeno	Tabletas	400mg	\N	t
3	Amoxicilina	Amoxicilina	Cápsulas	500mg	\N	t
4	Losartán	Losartán	Tabletas	50mg	\N	t
5	Omeprazol	Omeprazol	Cápsulas	20mg	\N	t
\.


--
-- Data for Name: medicos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medicos (id_medico, id_usuario, nombres, apellidos, cedula, id_especialidad, numero_licencia, telefono, fecha_nacimiento, direccion, foto_url, fecha_contratacion, activo) FROM stdin;
\.


--
-- Data for Name: pacientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pacientes (id_paciente, id_usuario, nombres, apellidos, cedula, fecha_nacimiento, edad, genero, telefono, email, direccion, ciudad, provincia, seguro_medico, numero_seguro, tipo_sangre, alergias, enfermedades_cronicas, contacto_emergencia_nombre, contacto_emergencia_telefono, contacto_emergencia_relacion, fecha_registro, activo) FROM stdin;
\.


--
-- Data for Name: recepcionistas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recepcionistas (id_recepcionista, id_usuario, nombres, apellidos, cedula, telefono, fecha_nacimiento, direccion, fecha_contratacion, activo) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id_rol, nombre_rol, descripcion, fecha_creacion) FROM stdin;
1	Administrador	Acceso completo al sistema	2025-12-02 11:17:12.293938
2	Medico	Puede ver y gestionar consultas y pacientes	2025-12-02 11:17:12.293938
3	Recepcionista	Puede gestionar citas y datos basicos de pacientes	2025-12-02 11:17:12.293938
4	Paciente	Acceso a su propia informacion y citas	2025-12-02 11:17:12.293938
\.


--
-- Data for Name: seguimientos_tratamiento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seguimientos_tratamiento (id_seguimiento, id_tratamiento, id_paciente, fecha_seguimiento, cumpliendo_tratamiento, notas, reportado_por) FROM stdin;
\.


--
-- Data for Name: tratamientos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tratamientos (id_tratamiento, id_consulta, id_medicamento, medicamento_texto, dosis, frecuencia, duracion, via_administracion, indicaciones, fecha_inicio, fecha_fin, activo) FROM stdin;
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id_usuario, nombre_usuario, contrasena_hash, email, id_rol, activo, fecha_creacion, ultimo_acceso) FROM stdin;
1	admin	$2a$10$MM4V3GCI2byryadvzeMb9uFoph14xRtKnBoqtA7/9Biq3LX1DGWVm	admin@clinica.com	1	t	2025-12-02 15:02:38.933465	\N
\.


--
-- Name: archivos_medicos_id_archivo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.archivos_medicos_id_archivo_seq', 1, false);


--
-- Name: auditorias_id_auditoria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.auditorias_id_auditoria_seq', 1, false);


--
-- Name: citas_id_cita_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.citas_id_cita_seq', 1, false);


--
-- Name: consultas_id_consulta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.consultas_id_consulta_seq', 1, false);


--
-- Name: especialidades_id_especialidad_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.especialidades_id_especialidad_seq', 12, true);


--
-- Name: estados_cita_id_estado_cita_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.estados_cita_id_estado_cita_seq', 1, true);


--
-- Name: examenes_laboratorio_id_examen_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.examenes_laboratorio_id_examen_seq', 1, false);


--
-- Name: horarios_medicos_id_horario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.horarios_medicos_id_horario_seq', 1, false);


--
-- Name: medicamentos_id_medicamento_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.medicamentos_id_medicamento_seq', 5, true);


--
-- Name: medicos_id_medico_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.medicos_id_medico_seq', 1, false);


--
-- Name: pacientes_id_paciente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pacientes_id_paciente_seq', 1, false);


--
-- Name: recepcionistas_id_recepcionista_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.recepcionistas_id_recepcionista_seq', 1, false);


--
-- Name: roles_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_rol_seq', 5, false);


--
-- Name: seguimientos_tratamiento_id_seguimiento_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seguimientos_tratamiento_id_seguimiento_seq', 1, false);


--
-- Name: tratamientos_id_tratamiento_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tratamientos_id_tratamiento_seq', 1, false);


--
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_usuario_seq', 2, false);


--
-- Name: archivos_medicos archivos_medicos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archivos_medicos
    ADD CONSTRAINT archivos_medicos_pkey PRIMARY KEY (id_archivo);


--
-- Name: auditorias auditorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditorias
    ADD CONSTRAINT auditorias_pkey PRIMARY KEY (id_auditoria);


--
-- Name: citas citas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.citas
    ADD CONSTRAINT citas_pkey PRIMARY KEY (id_cita);


--
-- Name: consultas consultas_id_cita_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_id_cita_key UNIQUE (id_cita);


--
-- Name: consultas consultas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_pkey PRIMARY KEY (id_consulta);


--
-- Name: especialidades especialidades_nombre_especialidad_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.especialidades
    ADD CONSTRAINT especialidades_nombre_especialidad_key UNIQUE (nombre_especialidad);


--
-- Name: especialidades especialidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.especialidades
    ADD CONSTRAINT especialidades_pkey PRIMARY KEY (id_especialidad);


--
-- Name: estados_cita estados_cita_nombre_estado_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados_cita
    ADD CONSTRAINT estados_cita_nombre_estado_key UNIQUE (nombre_estado);


--
-- Name: estados_cita estados_cita_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estados_cita
    ADD CONSTRAINT estados_cita_pkey PRIMARY KEY (id_estado_cita);


--
-- Name: examenes_laboratorio examenes_laboratorio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.examenes_laboratorio
    ADD CONSTRAINT examenes_laboratorio_pkey PRIMARY KEY (id_examen);


--
-- Name: horarios_medicos horarios_medicos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horarios_medicos
    ADD CONSTRAINT horarios_medicos_pkey PRIMARY KEY (id_horario);


--
-- Name: medicamentos medicamentos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicamentos
    ADD CONSTRAINT medicamentos_pkey PRIMARY KEY (id_medicamento);


--
-- Name: medicos medicos_cedula_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_cedula_key UNIQUE (cedula);


--
-- Name: medicos medicos_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_id_usuario_key UNIQUE (id_usuario);


--
-- Name: medicos medicos_numero_licencia_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_numero_licencia_key UNIQUE (numero_licencia);


--
-- Name: medicos medicos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_pkey PRIMARY KEY (id_medico);


--
-- Name: pacientes pacientes_cedula_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes
    ADD CONSTRAINT pacientes_cedula_key UNIQUE (cedula);


--
-- Name: pacientes pacientes_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes
    ADD CONSTRAINT pacientes_id_usuario_key UNIQUE (id_usuario);


--
-- Name: pacientes pacientes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes
    ADD CONSTRAINT pacientes_pkey PRIMARY KEY (id_paciente);


--
-- Name: recepcionistas recepcionistas_cedula_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recepcionistas
    ADD CONSTRAINT recepcionistas_cedula_key UNIQUE (cedula);


--
-- Name: recepcionistas recepcionistas_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recepcionistas
    ADD CONSTRAINT recepcionistas_id_usuario_key UNIQUE (id_usuario);


--
-- Name: recepcionistas recepcionistas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recepcionistas
    ADD CONSTRAINT recepcionistas_pkey PRIMARY KEY (id_recepcionista);


--
-- Name: roles roles_nombre_rol_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_nombre_rol_key UNIQUE (nombre_rol);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id_rol);


--
-- Name: seguimientos_tratamiento seguimientos_tratamiento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seguimientos_tratamiento
    ADD CONSTRAINT seguimientos_tratamiento_pkey PRIMARY KEY (id_seguimiento);


--
-- Name: tratamientos tratamientos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tratamientos
    ADD CONSTRAINT tratamientos_pkey PRIMARY KEY (id_tratamiento);


--
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- Name: usuarios usuarios_nombre_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_nombre_usuario_key UNIQUE (nombre_usuario);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario);


--
-- Name: idx_citas_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_citas_estado ON public.citas USING btree (id_estado_cita);


--
-- Name: idx_citas_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_citas_fecha ON public.citas USING btree (fecha_cita);


--
-- Name: idx_citas_medico; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_citas_medico ON public.citas USING btree (id_medico);


--
-- Name: idx_citas_paciente; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_citas_paciente ON public.citas USING btree (id_paciente);


--
-- Name: idx_consultas_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_fecha ON public.consultas USING btree (fecha_consulta);


--
-- Name: idx_consultas_paciente; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_paciente ON public.consultas USING btree (id_paciente);


--
-- Name: idx_medicos_especialidad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_medicos_especialidad ON public.medicos USING btree (id_especialidad);


--
-- Name: idx_pacientes_activo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pacientes_activo ON public.pacientes USING btree (activo);


--
-- Name: idx_pacientes_cedula; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pacientes_cedula ON public.pacientes USING btree (cedula);


--
-- Name: idx_tratamientos_consulta; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tratamientos_consulta ON public.tratamientos USING btree (id_consulta);


--
-- Name: idx_usuarios_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_usuarios_email ON public.usuarios USING btree (email);


--
-- Name: idx_usuarios_rol; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_usuarios_rol ON public.usuarios USING btree (id_rol);


--
-- Name: archivos_medicos archivos_medicos_id_consulta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archivos_medicos
    ADD CONSTRAINT archivos_medicos_id_consulta_fkey FOREIGN KEY (id_consulta) REFERENCES public.consultas(id_consulta);


--
-- Name: archivos_medicos archivos_medicos_id_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archivos_medicos
    ADD CONSTRAINT archivos_medicos_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES public.pacientes(id_paciente);


--
-- Name: archivos_medicos archivos_medicos_subido_por_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.archivos_medicos
    ADD CONSTRAINT archivos_medicos_subido_por_fkey FOREIGN KEY (subido_por) REFERENCES public.usuarios(id_usuario);


--
-- Name: auditorias auditorias_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditorias
    ADD CONSTRAINT auditorias_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: citas citas_id_estado_cita_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.citas
    ADD CONSTRAINT citas_id_estado_cita_fkey FOREIGN KEY (id_estado_cita) REFERENCES public.estados_cita(id_estado_cita);


--
-- Name: citas citas_id_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.citas
    ADD CONSTRAINT citas_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES public.medicos(id_medico);


--
-- Name: citas citas_id_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.citas
    ADD CONSTRAINT citas_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES public.pacientes(id_paciente);


--
-- Name: consultas consultas_id_cita_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_id_cita_fkey FOREIGN KEY (id_cita) REFERENCES public.citas(id_cita);


--
-- Name: consultas consultas_id_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES public.medicos(id_medico);


--
-- Name: consultas consultas_id_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES public.pacientes(id_paciente);


--
-- Name: examenes_laboratorio examenes_laboratorio_id_consulta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.examenes_laboratorio
    ADD CONSTRAINT examenes_laboratorio_id_consulta_fkey FOREIGN KEY (id_consulta) REFERENCES public.consultas(id_consulta);


--
-- Name: horarios_medicos horarios_medicos_id_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horarios_medicos
    ADD CONSTRAINT horarios_medicos_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES public.medicos(id_medico);


--
-- Name: medicos medicos_id_especialidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_id_especialidad_fkey FOREIGN KEY (id_especialidad) REFERENCES public.especialidades(id_especialidad);


--
-- Name: medicos medicos_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: pacientes pacientes_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes
    ADD CONSTRAINT pacientes_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: recepcionistas recepcionistas_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recepcionistas
    ADD CONSTRAINT recepcionistas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: seguimientos_tratamiento seguimientos_tratamiento_id_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seguimientos_tratamiento
    ADD CONSTRAINT seguimientos_tratamiento_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES public.pacientes(id_paciente);


--
-- Name: seguimientos_tratamiento seguimientos_tratamiento_id_tratamiento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seguimientos_tratamiento
    ADD CONSTRAINT seguimientos_tratamiento_id_tratamiento_fkey FOREIGN KEY (id_tratamiento) REFERENCES public.tratamientos(id_tratamiento);


--
-- Name: tratamientos tratamientos_id_consulta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tratamientos
    ADD CONSTRAINT tratamientos_id_consulta_fkey FOREIGN KEY (id_consulta) REFERENCES public.consultas(id_consulta);


--
-- Name: tratamientos tratamientos_id_medicamento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tratamientos
    ADD CONSTRAINT tratamientos_id_medicamento_fkey FOREIGN KEY (id_medicamento) REFERENCES public.medicamentos(id_medicamento);


--
-- Name: usuarios usuarios_id_rol_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol);


--
-- PostgreSQL database dump complete
--

\unrestrict mM4dh1DluE9fHgqXFWCzntoUDHo4i6jHuOee92Pp8dKfChWWacep7TCtjDYqz2V

