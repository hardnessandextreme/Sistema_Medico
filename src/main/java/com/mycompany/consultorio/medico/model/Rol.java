package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Rol - Representa roles de usuarios
 */
@Entity
@Table(name = "roles")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;
    
    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    private String nombreRol;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public Rol() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Rol(String nombreRol, String descripcion) {
        this();
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }
    
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }
    
    public String getNombreRol() {
        return nombreRol;
    }
    
    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "Rol{" +
                "idRol=" + idRol +
                ", nombreRol='" + nombreRol + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
