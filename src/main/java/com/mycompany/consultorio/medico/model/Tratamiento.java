package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Tratamiento - Representa tratamientos prescritos en consultas
 */
@Entity
@Table(name = "tratamientos")
public class Tratamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamiento")
    private Integer idTratamiento;
    
    @ManyToOne
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;
    
    @ManyToOne
    @JoinColumn(name = "id_medicamento")
    private Medicamento medicamento;
    
    @Column(name = "medicamento_texto", length = 200)
    private String medicamentoTexto;
    
    @Column(name = "dosis", length = 100)
    private String dosis;
    
    @Column(name = "frecuencia", nullable = false, length = 100)
    private String frecuencia;
    
    @Column(name = "duracion", nullable = false, length = 100)
    private String duracion;
    
    @Column(name = "via_administracion", length = 50)
    private String viaAdministracion;
    
    @Column(name = "indicaciones", columnDefinition = "TEXT")
    private String indicaciones;
    
    @Column(name = "fecha_inicio")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaFin;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Tratamiento() {
        this.activo = true;
    }
    
    public Tratamiento(Consulta consulta, String dosis, String frecuencia, String duracion) {
        this();
        this.consulta = consulta;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
    }
    
    // Getters y Setters
    public Integer getIdTratamiento() {
        return idTratamiento;
    }
    
    public void setIdTratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }
    
    public Consulta getConsulta() {
        return consulta;
    }
    
    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
    
    public Medicamento getMedicamento() {
        return medicamento;
    }
    
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
    
    public String getMedicamentoTexto() {
        return medicamentoTexto;
    }
    
    public void setMedicamentoTexto(String medicamentoTexto) {
        this.medicamentoTexto = medicamentoTexto;
    }
    
    public String getDosis() {
        return dosis;
    }
    
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }
    
    public String getFrecuencia() {
        return frecuencia;
    }
    
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    public String getDuracion() {
        return duracion;
    }
    
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    
    public String getViaAdministracion() {
        return viaAdministracion;
    }
    
    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }
    
    public String getIndicaciones() {
        return indicaciones;
    }
    
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    
    public java.time.LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(java.time.LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public java.time.LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(java.time.LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Tratamiento{" +
                "idTratamiento=" + idTratamiento +
                ", medicamento=" + (medicamento != null ? medicamento.getNombreComercial() : "null") +
                ", dosis='" + dosis + '\'' +
                '}';
    }
}
