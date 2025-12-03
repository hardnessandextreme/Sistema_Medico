package com.mycompany.consultorio.medico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Paciente - Representa pacientes del consultorio
 */
@Entity
@Table(name = "pacientes")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_usuario", unique = true)
    private Usuario usuario;
    
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;
    
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;
    
    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    
    @Column(name = "edad")
    private Integer edad;
    
    @Column(name = "genero", length = 20)
    private String genero;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;
    
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Column(name = "provincia", length = 100)
    private String provincia;
    
    @Column(name = "seguro_medico", length = 100)
    private String seguroMedico;
    
    @Column(name = "numero_seguro", length = 100)
    private String numeroSeguro;
    
    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;
    
    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;
    
    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    private String enfermedadesCronicas;
    
    @Column(name = "contacto_emergencia_nombre", length = 150)
    private String contactoEmergenciaNombre;
    
    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;
    
    @Column(name = "contacto_emergencia_relacion", length = 50)
    private String contactoEmergenciaRelacion;
    
    @Column(name = "fecha_registro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Paciente() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
    
    public Paciente(String nombres, String apellidos, String cedula) {
        this();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
    }
    
    // Getters y Setters
    public Integer getIdPaciente() {
        return idPaciente;
    }
    
    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getProvincia() {
        return provincia;
    }
    
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public String getSeguroMedico() {
        return seguroMedico;
    }
    
    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }
    
    public String getNumeroSeguro() {
        return numeroSeguro;
    }
    
    public void setNumeroSeguro(String numeroSeguro) {
        this.numeroSeguro = numeroSeguro;
    }
    
    public String getTipoSangre() {
        return tipoSangre;
    }
    
    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
    
    public String getAlergias() {
        return alergias;
    }
    
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
    
    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }
    
    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }
    
    public String getContactoEmergenciaNombre() {
        return contactoEmergenciaNombre;
    }
    
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) {
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
    }
    
    public String getContactoEmergenciaTelefono() {
        return contactoEmergenciaTelefono;
    }
    
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) {
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
    }
    
    public String getContactoEmergenciaRelacion() {
        return contactoEmergenciaRelacion;
    }
    
    public void setContactoEmergenciaRelacion(String contactoEmergenciaRelacion) {
        this.contactoEmergenciaRelacion = contactoEmergenciaRelacion;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    

    
    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", cedula='" + cedula + '\'' +
                '}';
    }
}
