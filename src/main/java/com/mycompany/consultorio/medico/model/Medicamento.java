package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Medicamento - Representa medicamentos disponibles
 */
@Entity
@Table(name = "medicamentos")
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private Integer idMedicamento;
    
    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;
    
    @Column(name = "nombre_generico", nullable = false, length = 150)
    private String nombreGenerico;
    
    @Column(name = "presentacion", length = 100)
    private String presentacion;
    
    @Column(name = "concentracion", length = 50)
    private String concentracion;
    
    @Column(name = "fabricante", length = 100)
    private String fabricante;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Medicamento() {
        this.activo = true;
    }
    
    public Medicamento(String nombreComercial, String nombreGenerico, String presentacion) {
        this();
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
        this.presentacion = presentacion;
    }
    
    // Getters y Setters
    public Integer getIdMedicamento() {
        return idMedicamento;
    }
    
    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }
    
    public String getNombreComercial() {
        return nombreComercial;
    }
    
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    
    public String getNombreGenerico() {
        return nombreGenerico;
    }
    
    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }
    
    public String getPresentacion() {
        return presentacion;
    }
    
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
    
    public String getConcentracion() {
        return concentracion;
    }
    
    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }
    
    public String getFabricante() {
        return fabricante;
    }
    
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Medicamento{" +
                "idMedicamento=" + idMedicamento +
                ", nombreComercial='" + nombreComercial + '\'' +
                ", nombreGenerico='" + nombreGenerico + '\'' +
                '}';
    }
}
