package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Especialidad - Representa especialidades médicas
 */
@Entity
@Table(name = "especialidades")
public class Especialidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;
    
    @Column(name = "nombre_especialidad", nullable = false, unique = true, length = 100)
    private String nombreEspecialidad;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Especialidad() {
        this.activo = true;
    }
    
    public Especialidad(String nombreEspecialidad, String descripcion) {
        this();
        this.nombreEspecialidad = nombreEspecialidad;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }
    
    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }
    
    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }
    
    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Especialidad{" +
                "idEspecialidad=" + idEspecialidad +
                ", nombreEspecialidad='" + nombreEspecialidad + '\'' +
                '}';
    }
}
