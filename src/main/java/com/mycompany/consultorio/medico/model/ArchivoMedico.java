package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad ArchivoMedico - Representa archivos adjuntos a consultas
 */
@Entity
@Table(name = "archivos_medicos")
public class ArchivoMedico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivo")
    private Integer idArchivo;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "id_consulta")
    private Consulta consulta;
    
    @Column(name = "tipo_archivo", nullable = false, length = 50)
    private String tipoArchivo;
    
    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;
    
    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "tamanio_kb")
    private Integer tamanioKb;
    
    @Column(name = "fecha_subida")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaSubida;
    
    @ManyToOne
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;
    
    // Constructores
    public ArchivoMedico() {
        this.fechaSubida = LocalDateTime.now();
    }
    
    public ArchivoMedico(Paciente paciente, String tipoArchivo, String nombreArchivo, String rutaArchivo) {
        this();
        this.paciente = paciente;
        this.tipoArchivo = tipoArchivo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
    }
    
    // Getters y Setters
    public Integer getIdArchivo() {
        return idArchivo;
    }
    
    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Consulta getConsulta() {
        return consulta;
    }
    
    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
    
    public String getTipoArchivo() {
        return tipoArchivo;
    }
    
    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getTamanioKb() {
        return tamanioKb;
    }
    
    public void setTamanioKb(Integer tamanioKb) {
        this.tamanioKb = tamanioKb;
    }
    
    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }
    
    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    
    public Usuario getSubidoPor() {
        return subidoPor;
    }
    
    public void setSubidoPor(Usuario subidoPor) {
        this.subidoPor = subidoPor;
    }
    
    @Override
    public String toString() {
        return "ArchivoMedico{" +
                "idArchivo=" + idArchivo +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", tipoArchivo='" + tipoArchivo + '\'' +
                '}';
    }
}
