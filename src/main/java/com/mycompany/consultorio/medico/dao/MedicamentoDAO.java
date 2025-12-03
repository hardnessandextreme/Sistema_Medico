package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Medicamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Medicamento
 */
public class MedicamentoDAO {
    
    public Optional<Medicamento> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Medicamento medicamento = em.find(Medicamento.class, id);
            return Optional.ofNullable(medicamento);
        } finally {
            em.close();
        }
    }
    
    public List<Medicamento> searchByNombre(String termino) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medicamento> query = em.createQuery(
                "SELECT m FROM Medicamento m WHERE " +
                "LOWER(m.nombreComercial) LIKE LOWER(:termino) OR " +
                "LOWER(m.nombreGenerico) LIKE LOWER(:termino) " +
                "ORDER BY m.nombreComercial", Medicamento.class);
            query.setParameter("termino", "%" + termino + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Medicamento> findAllActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medicamento> query = em.createQuery(
                "SELECT m FROM Medicamento m WHERE m.activo = true ORDER BY m.nombreComercial", Medicamento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Medicamento> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Medicamento> query = em.createQuery(
                "SELECT m FROM Medicamento m ORDER BY m.nombreComercial", Medicamento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Medicamento save(Medicamento medicamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (medicamento.getIdMedicamento() == null) {
                em.persist(medicamento);
            } else {
                medicamento = em.merge(medicamento);
            }
            em.getTransaction().commit();
            return medicamento;
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
            Medicamento medicamento = em.find(Medicamento.class, id);
            if (medicamento != null) {
                em.remove(medicamento);
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
