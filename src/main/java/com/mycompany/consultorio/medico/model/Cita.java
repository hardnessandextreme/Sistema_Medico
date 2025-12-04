package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Cita - Representa citas médicas
 */
@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    
    @Column(name = "fecha_cita", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaCita;
    
    @Column(name = "hora_cita", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private java.time.LocalTime horaCita;
    
    @ManyToOne
    @JoinColumn(name = "id_estado_cita", nullable = false)
    private EstadoCita estadoCita;
    
    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta;
    
    @Column(name = "sintomas", columnDefinition = "TEXT")
    private String sintomas;
    
    @Column(name = "notas_recepcion", columnDefinition = "TEXT")
    private String notasRecepcion;
    
    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_confirmacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaConfirmacion;
    
    @Column(name = "fecha_cancelacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCancelacion;
    
    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String motivoCancelacion;
    
    @Column(name = "recordatorio_enviado")
    private Boolean recordatorioEnviado = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Cita() {
        this.fechaCreacion = LocalDateTime.now();
        this.recordatorioEnviado = false;
        this.activo = true;
    }
    
    public Cita(Paciente paciente, Medico medico, java.time.LocalDate fechaCita, java.time.LocalTime horaCita, EstadoCita estadoCita) {
        this();
        this.paciente = paciente;
        this.medico = medico;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.estadoCita = estadoCita;
    }
    
    // Getters y Setters
    public Integer getIdCita() {
        return idCita;
    }
    
    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
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
    
    public java.time.LocalDate getFechaCita() {
        return fechaCita;
    }
    
    public void setFechaCita(java.time.LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }
    
    public java.time.LocalTime getHoraCita() {
        return horaCita;
    }
    
    public void setHoraCita(java.time.LocalTime horaCita) {
        this.horaCita = horaCita;
    }
    
    public EstadoCita getEstadoCita() {
        return estadoCita;
    }
    
    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }
    
    public String getMotivoConsulta() {
        return motivoConsulta;
    }
    
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
    
    public String getSintomas() {
        return sintomas;
    }
    
    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }
    
    public String getNotasRecepcion() {
        return notasRecepcion;
    }
    
    public void setNotasRecepcion(String notasRecepcion) {
        this.notasRecepcion = notasRecepcion;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }
    
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }
    
    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }
    
    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
    
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }
    
    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }
    
    public Boolean getRecordatorioEnviado() {
        return recordatorioEnviado;
    }
    
    public void setRecordatorioEnviado(Boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaCita=" + fechaCita +
                ", horaCita=" + horaCita +
                ", paciente=" + (paciente != null ? paciente.getNombres() : "null") +
                ", medico=" + (medico != null ? medico.getNombres() : "null") +
                '}';
    }
}
