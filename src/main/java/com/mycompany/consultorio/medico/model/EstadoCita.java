package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad EstadoCita - Representa estados posibles de una cita
 */
@Entity
@Table(name = "estados_cita")
public class EstadoCita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cita")
    private Integer idEstadoCita;
    
    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    private String nombreEstado;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "color_hex", length = 7)
    private String colorHex;
    
    // Constructores
    public EstadoCita() {
    }
    
    public EstadoCita(String nombreEstado, String descripcion) {
        this.nombreEstado = nombreEstado;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdEstadoCita() {
        return idEstadoCita;
    }
    
    public void setIdEstadoCita(Integer idEstadoCita) {
        this.idEstadoCita = idEstadoCita;
    }
    
    public String getNombreEstado() {
        return nombreEstado;
    }
    
    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getColorHex() {
        return colorHex;
    }
    
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    
    @Override
    public String toString() {
        return "EstadoCita{" +
                "idEstadoCita=" + idEstadoCita +
                ", nombreEstado='" + nombreEstado + '\'' +
                '}';
    }
}
