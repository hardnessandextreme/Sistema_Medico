-- ============================================
-- LIMPIAR BASE DE DATOS - DEJAR SOLO ADMIN
-- Sistema Médico
-- Fecha: 3 de diciembre de 2025
-- ============================================

-- IMPORTANTE: Este script elimina TODOS los datos excepto:
-- - Roles del sistema
-- - Estados de cita
-- - Especialidades
-- - Medicamentos base
-- - Usuario administrador

-- ============================================
-- PASO 1: ELIMINAR DATOS DE TABLAS DEPENDIENTES
-- ============================================

-- Eliminar auditorías
DELETE FROM auditorias;

-- Eliminar archivos médicos
DELETE FROM archivos_medicos;

-- Eliminar seguimientos de tratamiento
DELETE FROM seguimientos_tratamiento;

-- Eliminar tratamientos
DELETE FROM tratamientos;

-- Eliminar exámenes de laboratorio
DELETE FROM examenes_laboratorio;

-- Eliminar consultas
DELETE FROM consultas;

-- Eliminar citas
DELETE FROM citas;

-- Eliminar horarios médicos
DELETE FROM horarios_medicos;

-- ============================================
-- PASO 2: ELIMINAR USUARIOS NO ADMIN
-- ============================================

-- Eliminar recepcionistas
DELETE FROM recepcionistas;

-- Eliminar pacientes
DELETE FROM pacientes;

-- Eliminar médicos
DELETE FROM medicos;

-- Eliminar usuarios que NO sean administrador
DELETE FROM usuarios WHERE id_rol != 1;

-- ============================================
-- PASO 3: RESETEAR SECUENCIAS (OPCIONAL)
-- ============================================

-- Resetear secuencias para que los IDs empiecen desde el siguiente número
ALTER SEQUENCE auditorias_id_auditoria_seq RESTART WITH 1;
ALTER SEQUENCE archivos_medicos_id_archivo_seq RESTART WITH 1;
ALTER SEQUENCE seguimientos_tratamiento_id_seguimiento_seq RESTART WITH 1;
ALTER SEQUENCE tratamientos_id_tratamiento_seq RESTART WITH 1;
ALTER SEQUENCE examenes_laboratorio_id_examen_seq RESTART WITH 1;
ALTER SEQUENCE consultas_id_consulta_seq RESTART WITH 1;
ALTER SEQUENCE citas_id_cita_seq RESTART WITH 1;
ALTER SEQUENCE horarios_medicos_id_horario_seq RESTART WITH 1;
ALTER SEQUENCE recepcionistas_id_recepcionista_seq RESTART WITH 1;
ALTER SEQUENCE pacientes_id_paciente_seq RESTART WITH 1;
ALTER SEQUENCE medicos_id_medico_seq RESTART WITH 1;

-- No resetear usuarios porque el admin ya tiene un ID
-- ALTER SEQUENCE usuarios_id_usuario_seq RESTART WITH 2;

-- ============================================
-- PASO 4: VERIFICACIÓN
-- ============================================

-- Verificar usuario administrador
SELECT u.id_usuario, u.nombre_usuario, u.email, r.nombre_rol 
FROM usuarios u
JOIN roles r ON u.id_rol = r.id_rol
WHERE r.nombre_rol = 'Administrador';

-- Contar registros en cada tabla
SELECT 'roles' as tabla, COUNT(*) as total FROM roles
UNION ALL
SELECT 'estados_cita', COUNT(*) FROM estados_cita
UNION ALL
SELECT 'especialidades', COUNT(*) FROM especialidades
UNION ALL
SELECT 'medicamentos', COUNT(*) FROM medicamentos
UNION ALL
SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'medicos', COUNT(*) FROM medicos
UNION ALL
SELECT 'pacientes', COUNT(*) FROM pacientes
UNION ALL
SELECT 'recepcionistas', COUNT(*) FROM recepcionistas
UNION ALL
SELECT 'citas', COUNT(*) FROM citas
UNION ALL
SELECT 'consultas', COUNT(*) FROM consultas
UNION ALL
SELECT 'tratamientos', COUNT(*) FROM tratamientos
UNION ALL
SELECT 'auditorias', COUNT(*) FROM auditorias
ORDER BY tabla;

-- ============================================
-- RESULTADO ESPERADO:
-- ============================================
-- roles: 4 registros (Administrador, Médico, Recepcionista, Paciente)
-- estados_cita: 9 registros
-- especialidades: 10 registros
-- medicamentos: 5 registros
-- usuarios: 1 registro (solo admin)
-- medicos: 0 registros
-- pacientes: 0 registros
-- recepcionistas: 0 registros
-- citas: 0 registros
-- consultas: 0 registros
-- tratamientos: 0 registros
-- auditorias: 0 registros
-- ============================================

