package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Usuario - Representa usuarios del sistema
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    private String nombreUsuario;
    
    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String contrasenaHash;
    
    @Column(name = "email", unique = true, length = 150)
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "ultimo_acceso")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ultimoAcceso;
    
    // Constructores
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }
    
    public Usuario(String nombreUsuario, String contrasenaHash, String email, Rol rol) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.email = email;
        this.rol = rol;
    }
    
    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getContrasenaHash() {
        return contrasenaHash;
    }
    
    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + (rol != null ? rol.getNombreRol() : "null") +
                ", activo=" + activo +
                '}';
    }
}
