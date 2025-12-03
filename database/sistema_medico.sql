-- ============================================
-- SCHEMA PARA SISTEMA MÉDICO
-- Base de datos: PostgreSQL 18
-- ============================================

-- Crear base de datos (ejecutar como superusuario)
-- CREATE DATABASE sistema_medico;

-- Conectar a la base de datos
-- \c sistema_medico;

-- ============================================
-- TABLA: roles
-- Descripción: Roles de usuarios del sistema
-- ============================================
CREATE TABLE roles (
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLA: usuarios
-- Descripción: Usuarios del sistema (médicos, recepcionistas, pacientes)
-- ============================================
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    id_rol INTEGER NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- ============================================
-- TABLA: especialidades
-- Descripción: Especialidades médicas
-- ============================================
CREATE TABLE especialidades (
    id_especialidad SERIAL PRIMARY KEY,
    nombre_especialidad VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

-- ============================================
-- TABLA: medicos
-- Descripción: Información de médicos
-- ============================================
CREATE TABLE medicos (
    id_medico SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    id_especialidad INTEGER NOT NULL,
    numero_licencia VARCHAR(50) UNIQUE,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    direccion TEXT,
    foto_url VARCHAR(255),
    fecha_contratacion DATE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_especialidad) REFERENCES especialidades(id_especialidad)
);

-- ============================================
-- TABLA: pacientes
-- Descripción: Información de pacientes
-- ============================================
CREATE TABLE pacientes (
    id_paciente SERIAL PRIMARY KEY,
    id_usuario INTEGER UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    edad INTEGER,
    genero VARCHAR(20),
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    direccion TEXT,
    ciudad VARCHAR(100),
    provincia VARCHAR(100),
    seguro_medico VARCHAR(100),
    numero_seguro VARCHAR(100),
    tipo_sangre VARCHAR(5),
    alergias TEXT,
    enfermedades_cronicas TEXT,
    contacto_emergencia_nombre VARCHAR(150),
    contacto_emergencia_telefono VARCHAR(20),
    contacto_emergencia_relacion VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================
-- TABLA: recepcionistas
-- Descripción: Información de recepcionistas
-- ============================================
CREATE TABLE recepcionistas (
    id_recepcionista SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    direccion TEXT,
    fecha_contratacion DATE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================
-- TABLA: horarios_medicos
-- Descripción: Horarios de atención de médicos
-- ============================================
CREATE TABLE horarios_medicos (
    id_horario SERIAL PRIMARY KEY,
    id_medico INTEGER NOT NULL,
    dia_semana INTEGER NOT NULL CHECK (dia_semana BETWEEN 0 AND 6), -- 0=Domingo, 6=Sábado
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
);

-- ============================================
-- TABLA: estados_cita
-- Descripción: Estados posibles de una cita
-- ============================================
CREATE TABLE estados_cita (
    id_estado_cita SERIAL PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    color_hex VARCHAR(7) -- Para UI
);

-- ============================================
-- TABLA: citas
-- Descripción: Citas médicas programadas
-- ============================================
CREATE TABLE citas (
    id_cita SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    id_medico INTEGER NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    motivo_consulta TEXT,
    sintomas TEXT,
    id_estado_cita INTEGER NOT NULL DEFAULT 1,
    notas_recepcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_confirmacion TIMESTAMP,
    fecha_cancelacion TIMESTAMP,
    motivo_cancelacion TEXT,
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_medico) REFERENCES medicos(id_medico),
    FOREIGN KEY (id_estado_cita) REFERENCES estados_cita(id_estado_cita)
);

-- ============================================
-- TABLA: consultas
-- Descripción: Registro de consultas realizadas
-- ============================================
CREATE TABLE consultas (
    id_consulta SERIAL PRIMARY KEY,
    id_cita INTEGER NOT NULL UNIQUE,
    id_paciente INTEGER NOT NULL,
    id_medico INTEGER NOT NULL,
    fecha_consulta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo_consulta TEXT,
    sintomas_presentados TEXT,
    signos_vitales TEXT, -- Temperatura, presión, etc.
    examen_fisico TEXT,
    diagnostico TEXT,
    observaciones TEXT,
    recomendaciones TEXT,
    proxima_cita DATE,
    FOREIGN KEY (id_cita) REFERENCES citas(id_cita),
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
);

-- ============================================
-- TABLA: medicamentos
-- Descripción: Catálogo de medicamentos
-- ============================================
CREATE TABLE medicamentos (
    id_medicamento SERIAL PRIMARY KEY,
    nombre_comercial VARCHAR(150) NOT NULL,
    nombre_generico VARCHAR(150) NOT NULL,
    presentacion VARCHAR(100), -- Tabletas, jarabe, etc.
    concentracion VARCHAR(50),
    fabricante VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

-- ============================================
-- TABLA: tratamientos
-- Descripción: Tratamientos prescritos en consultas
-- ============================================
CREATE TABLE tratamientos (
    id_tratamiento SERIAL PRIMARY KEY,
    id_consulta INTEGER NOT NULL,
    id_medicamento INTEGER,
    medicamento_texto VARCHAR(200), -- Por si no está en catálogo
    dosis VARCHAR(100),
    frecuencia VARCHAR(100) NOT NULL,
    duracion VARCHAR(100) NOT NULL,
    via_administracion VARCHAR(50),
    indicaciones TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_consulta) REFERENCES consultas(id_consulta),
    FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id_medicamento)
);

-- ============================================
-- TABLA: seguimientos_tratamiento
-- Descripción: Seguimiento de cumplimiento de tratamientos
-- ============================================
CREATE TABLE seguimientos_tratamiento (
    id_seguimiento SERIAL PRIMARY KEY,
    id_tratamiento INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    fecha_seguimiento DATE NOT NULL,
    cumpliendo_tratamiento VARCHAR(20) NOT NULL CHECK (cumpliendo_tratamiento IN ('si', 'no', 'parcial')),
    notas TEXT,
    reportado_por VARCHAR(50), -- 'paciente', 'medico', 'recepcionista'
    FOREIGN KEY (id_tratamiento) REFERENCES tratamientos(id_tratamiento),
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
);

-- ============================================
-- TABLA: examenes_laboratorio
-- Descripción: Exámenes de laboratorio solicitados
-- ============================================
CREATE TABLE examenes_laboratorio (
    id_examen SERIAL PRIMARY KEY,
    id_consulta INTEGER NOT NULL,
    tipo_examen VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_solicitud DATE NOT NULL,
    fecha_resultado DATE,
    resultado TEXT,
    archivo_resultado_url VARCHAR(255),
    laboratorio VARCHAR(150),
    estado VARCHAR(50) DEFAULT 'pendiente', -- pendiente, completado, cancelado
    FOREIGN KEY (id_consulta) REFERENCES consultas(id_consulta)
);

-- ============================================
-- TABLA: archivos_medicos
-- Descripción: Archivos adjuntos (imágenes, PDFs, etc.)
-- ============================================
CREATE TABLE archivos_medicos (
    id_archivo SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    id_consulta INTEGER,
    tipo_archivo VARCHAR(50) NOT NULL, -- 'imagen', 'pdf', 'laboratorio', etc.
    nombre_archivo VARCHAR(255) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    descripcion TEXT,
    tamanio_kb INTEGER,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subido_por INTEGER,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (id_consulta) REFERENCES consultas(id_consulta),
    FOREIGN KEY (subido_por) REFERENCES usuarios(id_usuario)
);

-- ============================================
-- TABLA: auditorias
-- Descripción: Registro de auditoría del sistema
-- ============================================
CREATE TABLE auditorias (
    id_auditoria SERIAL PRIMARY KEY,
    id_usuario INTEGER,
    tabla_afectada VARCHAR(100),
    operacion VARCHAR(20), -- INSERT, UPDATE, DELETE
    registro_id INTEGER,
    datos_anteriores jsonb,
    datos_nuevos jsonb,
    fecha_operacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    direccion_ip VARCHAR(50),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- ============================================
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(id_rol);
CREATE INDEX idx_medicos_especialidad ON medicos(id_especialidad);
CREATE INDEX idx_pacientes_cedula ON pacientes(cedula);
CREATE INDEX idx_pacientes_activo ON pacientes(activo);
CREATE INDEX idx_citas_fecha ON citas(fecha_cita);
CREATE INDEX idx_citas_paciente ON citas(id_paciente);
CREATE INDEX idx_citas_medico ON citas(id_medico);
CREATE INDEX idx_citas_estado ON citas(id_estado_cita);
CREATE INDEX idx_consultas_fecha ON consultas(fecha_consulta);
CREATE INDEX idx_consultas_paciente ON consultas(id_paciente);
CREATE INDEX idx_tratamientos_consulta ON tratamientos(id_consulta);

-- ============================================
-- COMENTARIOS EN TABLAS
-- ============================================
COMMENT ON TABLE roles IS 'Roles de usuarios: Administrador, Médico, Recepcionista, Paciente';
COMMENT ON TABLE usuarios IS 'Credenciales y datos de autenticación';
COMMENT ON TABLE pacientes IS 'Información demográfica y médica de pacientes';
COMMENT ON TABLE medicos IS 'Información profesional de médicos';
COMMENT ON TABLE citas IS 'Citas médicas programadas';
COMMENT ON TABLE consultas IS 'Registro de consultas médicas realizadas';
COMMENT ON TABLE tratamientos IS 'Tratamientos y medicamentos prescritos';

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar roles del sistema
INSERT INTO roles (nombre_rol, descripcion) VALUES
('Administrador', 'Acceso completo al sistema'),
('Médico', 'Acceso a consultas, pacientes y tratamientos'),
('Recepcionista', 'Gestión de citas y registro de pacientes'),
('Paciente', 'Acceso limitado a su información médica');

-- Insertar estados de citas
INSERT INTO estados_cita (nombre_estado, descripcion, color_hex) VALUES
('Programada', 'Cita programada y confirmada', '#3B82F6'),
('Confirmada', 'Paciente confirmó asistencia', '#10B981'),
('En Atención', 'Paciente siendo atendido', '#F59E0B'),
('Completada', 'Consulta finalizada', '#6B7280'),
('Cancelada', 'Cita cancelada por paciente o médico', '#EF4444'),
('No Asistió', 'Paciente no se presentó', '#DC2626');

-- Insertar especialidades médicas comunes
INSERT INTO especialidades (nombre_especialidad, descripcion) VALUES
('Medicina General', 'Atención médica general y preventiva'),
('Pediatría', 'Atención médica infantil'),
('Cardiología', 'Especialista en corazón y sistema cardiovascular'),
('Dermatología', 'Especialista en piel'),
('Ginecología', 'Salud femenina y reproductiva'),
('Traumatología', 'Lesiones y enfermedades del sistema músculo-esquelético'),
('Oftalmología', 'Especialista en ojos y visión'),
('Odontología', 'Salud dental'),
('Psicología', 'Salud mental y emocional'),
('Nutrición', 'Alimentación y nutrición');

-- Insertar algunos medicamentos comunes
INSERT INTO medicamentos (nombre_comercial, nombre_generico, presentacion, concentracion) VALUES
('Paracetamol', 'Paracetamol', 'Tabletas', '500mg'),
('Ibuprofeno', 'Ibuprofeno', 'Tabletas', '400mg'),
('Amoxicilina', 'Amoxicilina', 'Cápsulas', '500mg'),
('Omeprazol', 'Omeprazol', 'Cápsulas', '20mg'),
('Loratadina', 'Loratadina', 'Tabletas', '10mg');
