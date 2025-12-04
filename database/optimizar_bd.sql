-- ============================================
-- SCRIPT DE OPTIMIZACIÓN DE BASE DE DATOS
-- Elimina tablas innecesarias para simplificar
-- ============================================

-- Conectar a la base de datos
-- \c sistema_medico;

-- Eliminar tablas que no se están usando
DROP TABLE IF EXISTS seguimientos_tratamiento CASCADE;
DROP TABLE IF EXISTS archivos_medicos CASCADE;
DROP TABLE IF EXISTS auditorias CASCADE;

-- Verificar tablas restantes
-- \dt

-- Las tablas esenciales que permanecen:
-- 1. roles
-- 2. usuarios
-- 3. especialidades
-- 4. medicos
-- 5. pacientes
-- 6. recepcionistas
-- 7. horarios_medicos
-- 8. estados_cita
-- 9. citas
-- 10. consultas
-- 11. medicamentos
-- 12. tratamientos
-- 13. examenes_laboratorio

COMMENT ON DATABASE sistema_medico IS 'Sistema Médico Optimizado - 13 tablas esenciales';
