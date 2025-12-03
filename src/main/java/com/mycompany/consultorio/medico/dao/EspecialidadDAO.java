package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Especialidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Especialidad
 */
public class EspecialidadDAO {
    
    public Optional<Especialidad> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Especialidad especialidad = em.find(Especialidad.class, id);
            return Optional.ofNullable(especialidad);
        } finally {
            em.close();
        }
    }
    
    public Optional<Especialidad> findByNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Especialidad> query = em.createQuery(
                "SELECT e FROM Especialidad e WHERE e.nombreEspecialidad = :nombre", Especialidad.class);
            query.setParameter("nombre", nombre);
            List<Especialidad> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Especialidad> findAllActivas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Especialidad> query = em.createQuery(
                "SELECT e FROM Especialidad e WHERE e.activo = true ORDER BY e.nombreEspecialidad", Especialidad.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Especialidad> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Especialidad> query = em.createQuery(
                "SELECT e FROM Especialidad e ORDER BY e.nombreEspecialidad", Especialidad.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Especialidad save(Especialidad especialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (especialidad.getIdEspecialidad() == null) {
                em.persist(especialidad);
            } else {
                especialidad = em.merge(especialidad);
            }
            em.getTransaction().commit();
            return especialidad;
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
            Especialidad especialidad = em.find(Especialidad.class, id);
            if (especialidad != null) {
                em.remove(especialidad);
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
