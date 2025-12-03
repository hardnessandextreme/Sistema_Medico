package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Cita;
import com.mycompany.consultorio.medico.model.Medico;
import com.mycompany.consultorio.medico.model.Paciente;
import com.mycompany.consultorio.medico.model.EstadoCita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Cita
 */
public class CitaDAO {
    
    public Optional<Cita> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Cita cita = em.find(Cita.class, id);
            return Optional.ofNullable(cita);
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByMedico(Medico medico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.medico = :medico ORDER BY c.fechaCita DESC, c.horaCita DESC", Cita.class);
            query.setParameter("medico", medico);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByMedicoAndFecha(Medico medico, java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.medico = :medico AND c.fechaCita BETWEEN :inicio AND :fin " +
                "ORDER BY c.fechaCita, c.horaCita", Cita.class);
            query.setParameter("medico", medico);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.paciente = :paciente ORDER BY c.fechaCita DESC, c.horaCita DESC", Cita.class);
            query.setParameter("paciente", paciente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByEstado(EstadoCita estadoCita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.estadoCita = :estadoCita ORDER BY c.fechaCita, c.horaCita", Cita.class);
            query.setParameter("estadoCita", estadoCita);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByEstadoId(Integer idEstadoCita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.estadoCita.idEstadoCita = :idEstado ORDER BY c.fechaCita, c.horaCita", Cita.class);
            query.setParameter("idEstado", idEstadoCita);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findByFechaRange(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :inicio AND :fin " +
                "ORDER BY c.fechaCita, c.horaCita", Cita.class);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cita> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c ORDER BY c.fechaCita DESC, c.horaCita DESC", Cita.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Cita save(Cita cita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (cita.getIdCita() == null) {
                em.persist(cita);
            } else {
                cita = em.merge(cita);
            }
            em.getTransaction().commit();
            return cita;
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
            Cita cita = em.find(Cita.class, id);
            if (cita != null) {
                em.remove(cita);
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
