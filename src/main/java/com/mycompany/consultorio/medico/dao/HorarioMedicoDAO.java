package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.HorarioMedico;
import com.mycompany.consultorio.medico.model.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad HorarioMedico
 */
public class HorarioMedicoDAO {
    
    public Optional<HorarioMedico> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            HorarioMedico horario = em.find(HorarioMedico.class, id);
            return Optional.ofNullable(horario);
        } finally {
            em.close();
        }
    }
    
    public List<HorarioMedico> findByMedico(Medico medico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<HorarioMedico> query = em.createQuery(
                "SELECT h FROM HorarioMedico h WHERE h.medico = :medico ORDER BY h.diaSemana, h.horaInicio", HorarioMedico.class);
            query.setParameter("medico", medico);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<HorarioMedico> findByMedicoActivos(Medico medico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<HorarioMedico> query = em.createQuery(
                "SELECT h FROM HorarioMedico h WHERE h.medico = :medico AND h.activo = true " +
                "ORDER BY h.diaSemana, h.horaInicio", HorarioMedico.class);
            query.setParameter("medico", medico);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<HorarioMedico> findByDiaSemana(Integer diaSemana) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<HorarioMedico> query = em.createQuery(
                "SELECT h FROM HorarioMedico h WHERE h.diaSemana = :dia AND h.activo = true " +
                "ORDER BY h.medico.nombres, h.horaInicio", HorarioMedico.class);
            query.setParameter("dia", diaSemana);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<HorarioMedico> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<HorarioMedico> query = em.createQuery(
                "SELECT h FROM HorarioMedico h ORDER BY h.medico.nombres, h.diaSemana, h.horaInicio", HorarioMedico.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public HorarioMedico save(HorarioMedico horario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (horario.getIdHorario() == null) {
                em.persist(horario);
            } else {
                horario = em.merge(horario);
            }
            em.getTransaction().commit();
            return horario;
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
            HorarioMedico horario = em.find(HorarioMedico.class, id);
            if (horario != null) {
                em.remove(horario);
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
