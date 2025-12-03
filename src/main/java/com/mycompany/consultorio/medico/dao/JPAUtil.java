package com.mycompany.consultorio.medico.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Factory para obtener EntityManager
 */
public class JPAUtil {
    
    private static final String PERSISTENCE_UNIT_NAME = "SistemaMedicoPU";
    private static volatile EntityManagerFactory emf;
    private static final Object lock = new Object();
    
    private static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (lock) {
                if (emf == null) {
                    try {
                        System.out.println("Inicializando EntityManagerFactory...");
                        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                        System.out.println("EntityManagerFactory inicializado correctamente");
                    } catch (Exception e) {
                        System.err.println("Error al crear EntityManagerFactory: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("No se pudo inicializar JPA", e);
                    }
                }
            }
        }
        return emf;
    }
    
    public static EntityManager getEntityManager() {
        try {
            EntityManagerFactory factory = getEntityManagerFactory();
            if (factory == null || !factory.isOpen()) {
                throw new IllegalStateException("EntityManagerFactory no está disponible");
            }
            return factory.createEntityManager();
        } catch (Exception e) {
            System.err.println("Error al obtener EntityManager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
