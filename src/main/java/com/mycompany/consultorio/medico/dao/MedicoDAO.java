package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Medico;
import com.mycompany.consultorio.medico.model.Especialidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Medico
 */
public class MedicoDAO {
    
    public Optional<Medico> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Medico medico = em.find(Medico.class, id);
            return Optional.ofNullable(medico);
        } finally {
            em.close();
        }
    }
    
    public Optional<Medico> findByCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.cedula = :cedula", Medico.class);
            query.setParameter("cedula", cedula);
            List<Medico> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Medico> findByEspecialidad(Especialidad especialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.especialidad = :especialidad AND m.activo = true", Medico.class);
            query.setParameter("especialidad", especialidad);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Medico> findByEspecialidadId(Integer idEspecialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.especialidad.idEspecialidad = :idEspecialidad AND m.activo = true", Medico.class);
            query.setParameter("idEspecialidad", idEspecialidad);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Optional<Medico> findByUsuarioId(Integer idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.usuario.idUsuario = :idUsuario", Medico.class);
            query.setParameter("idUsuario", idUsuario);
            List<Medico> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<Medico> findAllActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.activo = true ORDER BY m.apellidos, m.nombres", Medico.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Medico> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m ORDER BY m.apellidos, m.nombres", Medico.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Medico save(Medico medico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (medico.getIdMedico() == null) {
                em.persist(medico);
            } else {
                medico = em.merge(medico);
            }
            em.getTransaction().commit();
            return medico;
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
            Medico medico = em.find(Medico.class, id);
            if (medico != null) {
                // Borrado lógico: desactivar en lugar de eliminar físicamente
                medico.setActivo(false);
                if (medico.getUsuario() != null) {
                    medico.getUsuario().setActivo(false);
                }
                em.merge(medico);
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
