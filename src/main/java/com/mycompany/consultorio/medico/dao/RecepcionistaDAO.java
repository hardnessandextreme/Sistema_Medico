package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Recepcionista;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Recepcionista
 */
public class RecepcionistaDAO {
    
    public Optional<Recepcionista> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Recepcionista recepcionista = em.find(Recepcionista.class, id);
            return Optional.ofNullable(recepcionista);
        } finally {
            em.close();
        }
    }
    
    public Optional<Recepcionista> findByCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Recepcionista> query = em.createQuery(
                "SELECT r FROM Recepcionista r WHERE r.cedula = :cedula", Recepcionista.class);
            query.setParameter("cedula", cedula);
            List<Recepcionista> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Recepcionista> searchByName(String termino) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Recepcionista> query = em.createQuery(
                "SELECT r FROM Recepcionista r WHERE " +
                "LOWER(r.nombres) LIKE LOWER(:termino) OR " +
                "LOWER(r.apellidos) LIKE LOWER(:termino) " +
                "ORDER BY r.apellidos, r.nombres", Recepcionista.class);
            query.setParameter("termino", "%" + termino + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Optional<Recepcionista> findByUsuarioId(Integer idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Recepcionista> query = em.createQuery(
                "SELECT r FROM Recepcionista r WHERE r.usuario.idUsuario = :idUsuario", Recepcionista.class);
            query.setParameter("idUsuario", idUsuario);
            List<Recepcionista> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Recepcionista> findAllActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Recepcionista> query = em.createQuery(
                "SELECT r FROM Recepcionista r WHERE r.activo = true ORDER BY r.apellidos, r.nombres", Recepcionista.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Recepcionista> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Recepcionista> query = em.createQuery(
                "SELECT r FROM Recepcionista r ORDER BY r.apellidos, r.nombres", Recepcionista.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Recepcionista save(Recepcionista recepcionista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (recepcionista.getIdRecepcionista() == null) {
                em.persist(recepcionista);
            } else {
                recepcionista = em.merge(recepcionista);
            }
            em.getTransaction().commit();
            return recepcionista;
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
            Recepcionista recepcionista = em.find(Recepcionista.class, id);
            if (recepcionista != null) {
                // Borrado lógico: desactivar en lugar de eliminar físicamente
                recepcionista.setActivo(false);
                if (recepcionista.getUsuario() != null) {
                    recepcionista.getUsuario().setActivo(false);
                }
                em.merge(recepcionista);
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
