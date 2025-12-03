package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Tratamiento;
import com.mycompany.consultorio.medico.model.Consulta;
import com.mycompany.consultorio.medico.model.Medicamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para entidad Tratamiento
 */
public class TratamientoDAO {
    
    public Optional<Tratamiento> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Tratamiento tratamiento = em.find(Tratamiento.class, id);
            return Optional.ofNullable(tratamiento);
        } finally {
            em.close();
        }
    }
    
    public List<Tratamiento> findByConsulta(Consulta consulta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Tratamiento> query = em.createQuery(
                "SELECT t FROM Tratamiento t WHERE t.consulta = :consulta ORDER BY t.fechaInicio DESC", Tratamiento.class);
            query.setParameter("consulta", consulta);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Tratamiento> findByMedicamento(Medicamento medicamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Tratamiento> query = em.createQuery(
                "SELECT t FROM Tratamiento t WHERE t.medicamento = :medicamento ORDER BY t.fechaInicio DESC", Tratamiento.class);
            query.setParameter("medicamento", medicamento);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Tratamiento> findActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Tratamiento> query = em.createQuery(
                "SELECT t FROM Tratamiento t WHERE t.activo = true ORDER BY t.fechaInicio DESC", Tratamiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Tratamiento> findActivosByPaciente(Integer idPaciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Tratamiento> query = em.createQuery(
                "SELECT t FROM Tratamiento t WHERE t.consulta.paciente.idPaciente = :idPaciente " +
                "AND t.activo = true ORDER BY t.fechaInicio DESC", Tratamiento.class);
            query.setParameter("idPaciente", idPaciente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Tratamiento> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Tratamiento> query = em.createQuery(
                "SELECT t FROM Tratamiento t ORDER BY t.fechaInicio DESC", Tratamiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Tratamiento save(Tratamiento tratamiento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (tratamiento.getIdTratamiento() == null) {
                em.persist(tratamiento);
            } else {
                tratamiento = em.merge(tratamiento);
            }
            em.getTransaction().commit();
            return tratamiento;
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
            Tratamiento tratamiento = em.find(Tratamiento.class, id);
            if (tratamiento != null) {
                em.remove(tratamiento);
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
