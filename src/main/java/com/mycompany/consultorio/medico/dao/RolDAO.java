package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

/**
 * DAO para gestionar operaciones de Rol
 */
public class RolDAO {
    
    public List<Rol> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Rol r ORDER BY r.idRol", Rol.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public Optional<Rol> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Rol rol = em.find(Rol.class, id);
            return Optional.ofNullable(rol);
        } finally {
            em.close();
        }
    }
    
    public Optional<Rol> findByNombreRol(String nombreRol) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Rol rol = em.createQuery("SELECT r FROM Rol r WHERE r.nombreRol = :nombreRol", Rol.class)
                    .setParameter("nombreRol", nombreRol)
                    .getSingleResult();
            return Optional.of(rol);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    public Rol save(Rol rol) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (rol.getIdRol() == null) {
                em.persist(rol);
            } else {
                rol = em.merge(rol);
            }
            em.getTransaction().commit();
            return rol;
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
            Rol rol = em.find(Rol.class, id);
            if (rol != null) {
                em.remove(rol);
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
