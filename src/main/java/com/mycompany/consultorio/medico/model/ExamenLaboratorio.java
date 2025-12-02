package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad ExamenLaboratorio - Representa exámenes de laboratorio ordenados
 */
@Entity
@Table(name = "examenes_laboratorio")
public class ExamenLaboratorio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen")
    private Integer idExamen;
    
    @ManyToOne
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;
    
    @Column(name = "tipo_examen", nullable = false, length = 150)
    private String tipoExamen;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_solicitud", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaSolicitud;
    
    @Column(name = "fecha_resultado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate fechaResultado;
    
    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado;
    
    @Column(name = "archivo_resultado_url", length = 255)
    private String archivoResultadoUrl;
    
    @Column(name = "laboratorio", length = 150)
    private String laboratorio;
    
    @Column(name = "estado", length = 50)
    private String estado = "pendiente";
    
    // Constructores
    public ExamenLaboratorio() {
        this.fechaSolicitud = java.time.LocalDate.now();
        this.estado = "pendiente";
    }
    
    public ExamenLaboratorio(Consulta consulta, String tipoExamen, String descripcion) {
        this();
        this.consulta = consulta;
        this.tipoExamen = tipoExamen;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdExamen() {
        return idExamen;
    }
    
    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }
    
    public Consulta getConsulta() {
        return consulta;
    }
    
    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
    
    public String getTipoExamen() {
        return tipoExamen;
    }
    
    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public java.time.LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    public void setFechaSolicitud(java.time.LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public java.time.LocalDate getFechaResultado() {
        return fechaResultado;
    }
    
    public void setFechaResultado(java.time.LocalDate fechaResultado) {
        this.fechaResultado = fechaResultado;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public String getArchivoResultadoUrl() {
        return archivoResultadoUrl;
    }
    
    public void setArchivoResultadoUrl(String archivoResultadoUrl) {
        this.archivoResultadoUrl = archivoResultadoUrl;
    }
    
    public String getLaboratorio() {
        return laboratorio;
    }
    
    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "ExamenLaboratorio{" +
                "idExamen=" + idExamen +
                ", tipoExamen='" + tipoExamen + '\'' +
                ", fechaSolicitud=" + fechaSolicitud +
                ", estado='" + estado + '\'' +
                '}';
    }
}
