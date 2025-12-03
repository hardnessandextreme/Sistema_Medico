package com.mycompany.consultorio.medico.dao;

import com.mycompany.consultorio.medico.model.Auditoria;
import com.mycompany.consultorio.medico.model.Usuario;
import com.mycompany.consultorio.medico.dao.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AuditoriaDAO {
    
    public Optional<Auditoria> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Auditoria auditoria = em.find(Auditoria.class, id);
            return Optional.ofNullable(auditoria);
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findByUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a WHERE a.usuario = :usuario ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findByTabla(String tabla) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a WHERE a.tablaAfectada = :tabla ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setParameter("tabla", tabla);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findByOperacion(String operacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a WHERE a.operacion = :operacion ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setParameter("operacion", operacion);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a WHERE a.fechaOperacion BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findByTablaAndRegistro(String tabla, Integer registroId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a WHERE a.tablaAfectada = :tabla AND a.registroId = :registroId ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setParameter("tabla", tabla);
            query.setParameter("registroId", registroId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findRecientes(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Auditoria> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Auditoria> query = em.createQuery(
                "SELECT a FROM Auditoria a ORDER BY a.fechaOperacion DESC",
                Auditoria.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Auditoria save(Auditoria auditoria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (auditoria.getIdAuditoria() == null) {
                em.persist(auditoria);
            } else {
                auditoria = em.merge(auditoria);
            }
            em.getTransaction().commit();
            return auditoria;
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
            Auditoria auditoria = em.find(Auditoria.class, id);
            if (auditoria != null) {
                em.remove(auditoria);
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
