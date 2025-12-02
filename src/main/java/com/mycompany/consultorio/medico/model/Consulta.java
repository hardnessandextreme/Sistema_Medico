package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Consulta - Representa consultas médicas realizadas
 */
@Entity
@Table(name = "consultas")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;
    
    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private Cita cita;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    
    @Column(name = "fecha_consulta", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaConsulta;
    
    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta;
    
    @Column(name = "sintomas_presentados", columnDefinition = "TEXT")
    private String sintomasPresentados;
    
    @Column(name = "signos_vitales", columnDefinition = "TEXT")
    private String signosVitales;
    
    @Column(name = "examen_fisico", columnDefinition = "TEXT")
    private String examenFisico;
    
    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;
    
    @Column(name = "proxima_cita")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate proximaCita;
    
    // Constructores
    public Consulta() {
        this.fechaConsulta = LocalDateTime.now();
    }
    
    public Consulta(Cita cita, Paciente paciente, Medico medico, String motivoConsulta, String diagnostico) {
        this();
        this.cita = cita;
        this.paciente = paciente;
        this.medico = medico;
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
    }
    
    // Getters y Setters
    public Integer getIdConsulta() {
        return idConsulta;
    }
    
    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }
    
    public Cita getCita() {
        return cita;
    }
    
    public void setCita(Cita cita) {
        this.cita = cita;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }
    
    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }
    
    public String getMotivoConsulta() {
        return motivoConsulta;
    }
    
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
    
    public String getSintomasPresentados() {
        return sintomasPresentados;
    }
    
    public void setSintomasPresentados(String sintomasPresentados) {
        this.sintomasPresentados = sintomasPresentados;
    }
    
    public String getSignosVitales() {
        return signosVitales;
    }
    
    public void setSignosVitales(String signosVitales) {
        this.signosVitales = signosVitales;
    }
    
    public String getExamenFisico() {
        return examenFisico;
    }
    
    public void setExamenFisico(String examenFisico) {
        this.examenFisico = examenFisico;
    }
    
    public String getDiagnostico() {
        return diagnostico;
    }
    
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getRecomendaciones() {
        return recomendaciones;
    }
    
    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
    
    public java.time.LocalDate getProximaCita() {
        return proximaCita;
    }
    
    public void setProximaCita(java.time.LocalDate proximaCita) {
        this.proximaCita = proximaCita;
    }
    
    @Override
    public String toString() {
        return "Consulta{" +
                "idConsulta=" + idConsulta +
                ", fechaConsulta=" + fechaConsulta +
                ", diagnostico='" + diagnostico + '\'' +
                '}';
    }
}
