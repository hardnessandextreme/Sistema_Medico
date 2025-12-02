package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad SeguimientoTratamiento - Representa seguimiento de tratamientos
 */
@Entity
@Table(name = "seguimientos_tratamiento")
public class SeguimientoTratamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguimiento")
    private Integer idSeguimiento;
    
    @ManyToOne
    @JoinColumn(name = "id_tratamiento", nullable = false)
    private Tratamiento tratamiento;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    
    @Column(name = "fecha_seguimiento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaSeguimiento;
    
    @Column(name = "cumpliendo_tratamiento", nullable = false, length = 20)
    private String cumpliendoTratamiento;
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    @Column(name = "reportado_por", length = 50)
    private String reportadoPor;
    
    // Constructores
    public SeguimientoTratamiento() {
        this.fechaSeguimiento = java.time.LocalDate.now();
    }
    
    public SeguimientoTratamiento(Tratamiento tratamiento, Paciente paciente, String cumpliendoTratamiento) {
        this();
        this.tratamiento = tratamiento;
        this.paciente = paciente;
        this.cumpliendoTratamiento = cumpliendoTratamiento;
    }
    
    // Getters y Setters
    public Integer getIdSeguimiento() {
        return idSeguimiento;
    }
    
    public void setIdSeguimiento(Integer idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }
    
    public Tratamiento getTratamiento() {
        return tratamiento;
    }
    
    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }
    
    public java.time.LocalDate getFechaSeguimiento() {
        return fechaSeguimiento;
    }
    
    public void setFechaSeguimiento(java.time.LocalDate fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public String getCumpliendoTratamiento() {
        return cumpliendoTratamiento;
    }
    
    public void setCumpliendoTratamiento(String cumpliendoTratamiento) {
        this.cumpliendoTratamiento = cumpliendoTratamiento;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public String getReportadoPor() {
        return reportadoPor;
    }
    
    public void setReportadoPor(String reportadoPor) {
        this.reportadoPor = reportadoPor;
    }
    
    @Override
    public String toString() {
        return "SeguimientoTratamiento{" +
                "idSeguimiento=" + idSeguimiento +
                ", fechaSeguimiento=" + fechaSeguimiento +
                ", cumpliendoTratamiento='" + cumpliendoTratamiento + '\'' +
                '}';
    }
}
