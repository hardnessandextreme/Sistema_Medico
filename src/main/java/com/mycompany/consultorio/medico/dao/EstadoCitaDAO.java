package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.EstadoCita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad EstadoCita
 */
public class EstadoCitaDAO {
    
    public Optional<EstadoCita> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EstadoCita estado = em.find(EstadoCita.class, id);
            return Optional.ofNullable(estado);
        } finally {
            em.close();
        }
    }
    
    public Optional<EstadoCita> findByNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EstadoCita> query = em.createQuery(
                "SELECT e FROM EstadoCita e WHERE e.nombreEstado = :nombre", EstadoCita.class);
            query.setParameter("nombre", nombre);
            List<EstadoCita> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<EstadoCita> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EstadoCita> query = em.createQuery(
                "SELECT e FROM EstadoCita e ORDER BY e.nombreEstado", EstadoCita.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public EstadoCita save(EstadoCita estado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (estado.getIdEstadoCita() == null) {
                em.persist(estado);
            } else {
                estado = em.merge(estado);
            }
            em.getTransaction().commit();
            return estado;
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
            EstadoCita estado = em.find(EstadoCita.class, id);
            if (estado != null) {
                em.remove(estado);
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
