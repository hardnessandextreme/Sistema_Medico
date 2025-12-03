package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.ExamenLaboratorio;
import com.mycompany.consultorio.medico.model.Consulta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad ExamenLaboratorio
 */
public class ExamenLaboratorioDAO {
    
    public Optional<ExamenLaboratorio> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ExamenLaboratorio examen = em.find(ExamenLaboratorio.class, id);
            return Optional.ofNullable(examen);
        } finally {
            em.close();
        }
    }
    
    public List<ExamenLaboratorio> findByConsulta(Consulta consulta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ExamenLaboratorio> query = em.createQuery(
                "SELECT e FROM ExamenLaboratorio e WHERE e.consulta = :consulta ORDER BY e.fechaSolicitud DESC", ExamenLaboratorio.class);
            query.setParameter("consulta", consulta);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ExamenLaboratorio> findByEstado(String estado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ExamenLaboratorio> query = em.createQuery(
                "SELECT e FROM ExamenLaboratorio e WHERE e.estado = :estado ORDER BY e.fechaSolicitud DESC", ExamenLaboratorio.class);
            query.setParameter("estado", estado);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ExamenLaboratorio> findPendientes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ExamenLaboratorio> query = em.createQuery(
                "SELECT e FROM ExamenLaboratorio e WHERE e.estado = 'pendiente' ORDER BY e.fechaSolicitud", ExamenLaboratorio.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ExamenLaboratorio> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ExamenLaboratorio> query = em.createQuery(
                "SELECT e FROM ExamenLaboratorio e ORDER BY e.fechaSolicitud DESC", ExamenLaboratorio.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public ExamenLaboratorio save(ExamenLaboratorio examen) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (examen.getIdExamen() == null) {
                em.persist(examen);
            } else {
                examen = em.merge(examen);
            }
            em.getTransaction().commit();
            return examen;
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
            ExamenLaboratorio examen = em.find(ExamenLaboratorio.class, id);
            if (examen != null) {
                em.remove(examen);
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
