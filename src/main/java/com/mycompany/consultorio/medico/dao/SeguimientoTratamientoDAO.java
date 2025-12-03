package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.SeguimientoTratamiento;
import com.mycompany.consultorio.medico.model.Tratamiento;
import com.mycompany.consultorio.medico.model.Paciente;
import com.mycompany.consultorio.medico.dao.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SeguimientoTratamientoDAO {
    
    public Optional<SeguimientoTratamiento> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            SeguimientoTratamiento seguimiento = em.find(SeguimientoTratamiento.class, id);
            return Optional.ofNullable(seguimiento);
        } finally {
            em.close();
        }
    }
    
    public List<SeguimientoTratamiento> findByTratamiento(Tratamiento tratamiento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoTratamiento> query = em.createQuery(
                "SELECT s FROM SeguimientoTratamiento s WHERE s.tratamiento = :tratamiento ORDER BY s.fechaSeguimiento DESC",
                SeguimientoTratamiento.class);
            query.setParameter("tratamiento", tratamiento);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SeguimientoTratamiento> findByPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoTratamiento> query = em.createQuery(
                "SELECT s FROM SeguimientoTratamiento s WHERE s.paciente = :paciente ORDER BY s.fechaSeguimiento DESC",
                SeguimientoTratamiento.class);
            query.setParameter("paciente", paciente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SeguimientoTratamiento> findByPacienteAndFechaRange(Paciente paciente, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoTratamiento> query = em.createQuery(
                "SELECT s FROM SeguimientoTratamiento s WHERE s.paciente = :paciente " +
                "AND s.fechaSeguimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY s.fechaSeguimiento DESC",
                SeguimientoTratamiento.class);
            query.setParameter("paciente", paciente);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SeguimientoTratamiento> findByCumpliendoTratamiento(String cumpliendo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoTratamiento> query = em.createQuery(
                "SELECT s FROM SeguimientoTratamiento s WHERE s.cumpliendoTratamiento = :cumpliendo ORDER BY s.fechaSeguimiento DESC",
                SeguimientoTratamiento.class);
            query.setParameter("cumpliendo", cumpliendo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SeguimientoTratamiento> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoTratamiento> query = em.createQuery(
                "SELECT s FROM SeguimientoTratamiento s ORDER BY s.fechaSeguimiento DESC",
                SeguimientoTratamiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public SeguimientoTratamiento save(SeguimientoTratamiento seguimiento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (seguimiento.getIdSeguimiento() == null) {
                em.persist(seguimiento);
            } else {
                seguimiento = em.merge(seguimiento);
            }
            em.getTransaction().commit();
            return seguimiento;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            SeguimientoTratamiento seguimiento = em.find(SeguimientoTratamiento.class, id);
            if (seguimiento != null) {
                em.remove(seguimiento);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
