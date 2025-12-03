package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entidad HorarioMedico - Representa horarios de atención de médicos
 */
@Entity
@Table(name = "horarios_medicos")
public class HorarioMedico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;
    
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana; // 0=Domingo, 1=Lunes, 2=Martes, 3=Miércoles, 4=Jueves, 5=Viernes, 6=Sábado
    
    @Column(name = "hora_inicio", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFin;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public HorarioMedico() {
        this.activo = true;
    }
    
    public HorarioMedico(Medico medico, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this();
        this.medico = medico;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    // Getters y Setters
    public Integer getIdHorario() {
        return idHorario;
    }
    
    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public Integer getDiaSemana() {
        return diaSemana;
    }
    
    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "HorarioMedico{" +
                "idHorario=" + idHorario +
                ", diaSemana=" + diaSemana +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}
