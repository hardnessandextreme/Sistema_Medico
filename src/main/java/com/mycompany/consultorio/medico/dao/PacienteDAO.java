package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Paciente
 */
public class PacienteDAO {
    
    public Optional<Paciente> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Paciente paciente = em.find(Paciente.class, id);
            return Optional.ofNullable(paciente);
        } finally {
            em.close();
        }
    }
    
    public Optional<Paciente> findByCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Paciente> query = em.createQuery(
                "SELECT p FROM Paciente p WHERE p.cedula = :cedula", Paciente.class);
            query.setParameter("cedula", cedula);
            List<Paciente> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public Optional<Paciente> findByUsuarioId(Integer idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Paciente> query = em.createQuery(
                "SELECT p FROM Paciente p WHERE p.usuario.idUsuario = :idUsuario", Paciente.class);
            query.setParameter("idUsuario", idUsuario);
            List<Paciente> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Paciente> searchByName(String searchTerm) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Paciente> query = em.createQuery(
                "SELECT p FROM Paciente p WHERE p.activo = true AND " +
                "(LOWER(p.nombres) LIKE LOWER(:term) OR LOWER(p.apellidos) LIKE LOWER(:term) OR p.cedula LIKE :term) " +
                "ORDER BY p.apellidos, p.nombres", Paciente.class);
            query.setParameter("term", "%" + searchTerm + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Paciente> findAllActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Paciente> query = em.createQuery(
                "SELECT p FROM Paciente p WHERE p.activo = true ORDER BY p.apellidos, p.nombres", Paciente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Paciente> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Paciente> query = em.createQuery(
                "SELECT p FROM Paciente p ORDER BY p.apellidos, p.nombres", Paciente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Paciente save(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (paciente.getIdPaciente() == null) {
                em.persist(paciente);
            } else {
                paciente = em.merge(paciente);
            }
            em.getTransaction().commit();
            return paciente;
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
            Paciente paciente = em.find(Paciente.class, id);
            if (paciente != null) {
                // Borrado lógico: desactivar en lugar de eliminar físicamente
                paciente.setActivo(false);
                if (paciente.getUsuario() != null) {
                    paciente.getUsuario().setActivo(false);
                }
                em.merge(paciente);
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
