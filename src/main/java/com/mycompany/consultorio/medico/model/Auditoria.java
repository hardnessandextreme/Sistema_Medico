package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Auditoria - Representa registro de auditoría del sistema
 */
@Entity
@Table(name = "auditorias")
public class Auditoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Column(name = "tabla_afectada", length = 100)
    private String tablaAfectada;
    
    @Column(name = "operacion", length = 20)
    private String operacion;
    
    @Column(name = "registro_id")
    private Integer registroId;
    
    @Column(name = "datos_anteriores", columnDefinition = "jsonb")
    private String datosAnteriores;
    
    @Column(name = "datos_nuevos", columnDefinition = "jsonb")
    private String datosNuevos;
    
    @Column(name = "fecha_operacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaOperacion;
    
    @Column(name = "direccion_ip", length = 50)
    private String direccionIp;
    
    // Constructores
    public Auditoria() {
        this.fechaOperacion = LocalDateTime.now();
    }
    
    public Auditoria(Usuario usuario, String tablaAfectada, String operacion) {
        this();
        this.usuario = usuario;
        this.tablaAfectada = tablaAfectada;
        this.operacion = operacion;
    }
    
    // Getters y Setters
    public Integer getIdAuditoria() {
        return idAuditoria;
    }
    
    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getTablaAfectada() {
        return tablaAfectada;
    }
    
    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }
    
    public String getOperacion() {
        return operacion;
    }
    
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
    
    public Integer getRegistroId() {
        return registroId;
    }
    
    public void setRegistroId(Integer registroId) {
        this.registroId = registroId;
    }
    
    public String getDatosAnteriores() {
        return datosAnteriores;
    }
    
    public void setDatosAnteriores(String datosAnteriores) {
        this.datosAnteriores = datosAnteriores;
    }
    
    public String getDatosNuevos() {
        return datosNuevos;
    }
    
    public void setDatosNuevos(String datosNuevos) {
        this.datosNuevos = datosNuevos;
    }
    
    public LocalDateTime getFechaOperacion() {
        return fechaOperacion;
    }
    
    public void setFechaOperacion(LocalDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    
    public String getDireccionIp() {
        return direccionIp;
    }
    
    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }
    
    @Override
    public String toString() {
        return "Auditoria{" +
                "idAuditoria=" + idAuditoria +
                ", operacion='" + operacion + '\'' +
                ", tablaAfectada='" + tablaAfectada + '\'' +
                ", fechaOperacion=" + fechaOperacion +
                '}';
    }
}
