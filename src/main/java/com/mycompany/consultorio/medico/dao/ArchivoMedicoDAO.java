package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.ArchivoMedico;
import com.mycompany.consultorio.medico.model.Paciente;
import com.mycompany.consultorio.medico.model.Consulta;
import com.mycompany.consultorio.medico.model.Usuario;
import com.mycompany.consultorio.medico.dao.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ArchivoMedicoDAO {
    
    public Optional<ArchivoMedico> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ArchivoMedico archivo = em.find(ArchivoMedico.class, id);
            return Optional.ofNullable(archivo);
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findByPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a WHERE a.paciente = :paciente ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            query.setParameter("paciente", paciente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findByConsulta(Consulta consulta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a WHERE a.consulta = :consulta ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            query.setParameter("consulta", consulta);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findByTipo(String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a WHERE a.tipoArchivo = :tipo ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            query.setParameter("tipo", tipo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findByPacienteAndTipo(Paciente paciente, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a WHERE a.paciente = :paciente AND a.tipoArchivo = :tipo ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            query.setParameter("paciente", paciente);
            query.setParameter("tipo", tipo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a WHERE a.fechaSubida BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ArchivoMedico> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ArchivoMedico> query = em.createQuery(
                "SELECT a FROM ArchivoMedico a ORDER BY a.fechaSubida DESC",
                ArchivoMedico.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public ArchivoMedico save(ArchivoMedico archivo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (archivo.getIdArchivo() == null) {
                em.persist(archivo);
            } else {
                archivo = em.merge(archivo);
            }
            em.getTransaction().commit();
            return archivo;
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
            ArchivoMedico archivo = em.find(ArchivoMedico.class, id);
            if (archivo != null) {
                em.remove(archivo);
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
