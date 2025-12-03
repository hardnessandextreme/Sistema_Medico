package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Consulta;
import com.mycompany.consultorio.medico.model.Cita;
import com.mycompany.consultorio.medico.model.Paciente;
import com.mycompany.consultorio.medico.model.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Consulta
 */
public class ConsultaDAO {
    
    public Optional<Consulta> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Consulta consulta = em.find(Consulta.class, id);
            return Optional.ofNullable(consulta);
        } finally {
            em.close();
        }
    }
    
    public Optional<Consulta> findByCita(Cita cita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c WHERE c.cita = :cita", Consulta.class);
            query.setParameter("cita", cita);
            List<Consulta> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Consulta> findByPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c WHERE c.paciente = :paciente ORDER BY c.fechaConsulta DESC", Consulta.class);
            query.setParameter("paciente", paciente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Consulta> findByMedico(Medico medico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c WHERE c.medico = :medico ORDER BY c.fechaConsulta DESC", Consulta.class);
            query.setParameter("medico", medico);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Consulta> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c WHERE c.fechaConsulta BETWEEN :inicio AND :fin " +
                "ORDER BY c.fechaConsulta DESC", Consulta.class);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Consulta> findByPacienteAndFechaRange(Paciente paciente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c WHERE c.paciente = :paciente AND " +
                "c.fechaConsulta BETWEEN :inicio AND :fin ORDER BY c.fechaConsulta DESC", Consulta.class);
            query.setParameter("paciente", paciente);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Consulta> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Consulta> query = em.createQuery(
                "SELECT c FROM Consulta c ORDER BY c.fechaConsulta DESC", Consulta.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Consulta save(Consulta consulta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (consulta.getIdConsulta() == null) {
                em.persist(consulta);
            } else {
                consulta = em.merge(consulta);
            }
            em.getTransaction().commit();
            return consulta;
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
            Consulta consulta = em.find(Consulta.class, id);
            if (consulta != null) {
                em.remove(consulta);
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
