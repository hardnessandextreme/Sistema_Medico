// ============================================
// GESTIÓN DE PACIENTES - PANEL RECEPCIONISTA
// ============================================

// ============================================
// ABRIR MODAL NUEVO PACIENTE
// ============================================
function abrirModalNuevoPaciente() {
    limpiarFormularioPaciente();
    $('#modalNuevoPaciente').modal('show');
}

// ============================================
// LIMPIAR FORMULARIO PACIENTE
// ============================================
function limpiarFormularioPaciente() {
    document.getElementById('pac-nombres').value = '';
    document.getElementById('pac-apellidos').value = '';
    document.getElementById('pac-cedula').value = '';
    document.getElementById('pac-fecha-nacimiento').value = '';
    document.getElementById('pac-genero').value = '';
    document.getElementById('pac-telefono').value = '';
    document.getElementById('pac-email').value = '';
    document.getElementById('pac-ciudad').value = '';
    document.getElementById('pac-direccion').value = '';
    document.getElementById('pac-tipo-sangre').value = '';
    document.getElementById('pac-seguro').value = '';
    document.getElementById('pac-numero-seguro').value = '';
    document.getElementById('pac-alergias').value = '';
    document.getElementById('pac-enfermedades').value = '';
    document.getElementById('pac-contacto-nombre').value = '';
    document.getElementById('pac-contacto-telefono').value = '';
    document.getElementById('pac-contacto-relacion').value = '';
    document.getElementById('pac-usuario').value = '';
    document.getElementById('pac-contrasena').value = '';
}

// ============================================
// CALCULAR EDAD
// ============================================
function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    
    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    
    return edad;
}

// ============================================
// GUARDAR NUEVO PACIENTE
// ============================================
async function guardarNuevoPaciente() {
    try {
        // Obtener valores
        const nombres = document.getElementById('pac-nombres').value.trim().toUpperCase();
        const apellidos = document.getElementById('pac-apellidos').value.trim().toUpperCase();
        const cedula = document.getElementById('pac-cedula').value.trim();
        const fechaNacimiento = document.getElementById('pac-fecha-nacimiento').value;
        const telefono = document.getElementById('pac-telefono').value.trim();
        const email = document.getElementById('pac-email').value.trim();
        
        // Validaciones básicas
        if (!nombres || !apellidos || !cedula || !fechaNacimiento || !telefono) {
            showError('Por favor complete todos los campos obligatorios (*)');
            return;
        }
        
        // Validar cédula (10 dígitos)
        if (!/^\d{10}$/.test(cedula)) {
            showError('La cédula debe tener 10 dígitos numéricos');
            return;
        }
        
        // Validar teléfono (10 dígitos)
        if (!/^\d{10}$/.test(telefono)) {
            showError('El teléfono debe tener 10 dígitos numéricos');
            return;
        }
        
        // Validar email si se proporcionó
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showError('El formato del email no es válido');
            return;
        }
        
        // Validar que la fecha de nacimiento no sea futura
        const hoy = new Date();
        const nacimiento = new Date(fechaNacimiento);
        if (nacimiento > hoy) {
            showError('La fecha de nacimiento no puede ser futura');
            return;
        }
        
        // Validar edad mínima (debe tener al menos 0 años)
        const edadValidacion = calcularEdad(fechaNacimiento);
        if (edadValidacion < 0 || edadValidacion > 150) {
            showError('La fecha de nacimiento no es válida');
            return;
        }
        
        // Validar contacto de emergencia si se completó algún campo
        const contactoNombre = document.getElementById('pac-contacto-nombre').value.trim();
        const contactoTelefono = document.getElementById('pac-contacto-telefono').value.trim();
        
        if (contactoNombre && contactoTelefono && !/^\d{10}$/.test(contactoTelefono)) {
            showError('El teléfono de emergencia debe tener 10 dígitos numéricos');
            return;
        }
        
        // Validar campos de usuario y contraseña
        const nombreUsuario = document.getElementById('pac-usuario').value.trim();
        const contrasena = document.getElementById('pac-contrasena').value;
        
        if (!nombreUsuario) {
            showError('El nombre de usuario es obligatorio');
            return;
        }
        
        if (!contrasena) {
            showError('La contraseña es obligatoria');
            return;
        }
        
        if (contrasena.length < 6) {
            showError('La contraseña debe tener al menos 6 caracteres');
            return;
        }
        
        // Calcular edad
        const edad = edadValidacion;
        
        // Crear objeto paciente
        const paciente = {
            nombres: nombres,
            apellidos: apellidos,
            cedula: cedula,
            fechaNacimiento: fechaNacimiento,
            edad: edad,
            genero: document.getElementById('pac-genero').value || null,
            telefono: telefono,
            email: document.getElementById('pac-email').value.trim() || null,
            direccion: document.getElementById('pac-direccion').value.trim() || null,
            ciudad: document.getElementById('pac-ciudad').value.trim() || null,
            tipoSangre: document.getElementById('pac-tipo-sangre').value || null,
            seguroMedico: document.getElementById('pac-seguro').value.trim() || null,
            numeroSeguro: document.getElementById('pac-numero-seguro').value.trim() || null,
            alergias: document.getElementById('pac-alergias').value.trim() || null,
            enfermedadesCronicas: document.getElementById('pac-enfermedades').value.trim() || null,
            contactoEmergenciaNombre: document.getElementById('pac-contacto-nombre').value.trim() || null,
            contactoEmergenciaTelefono: document.getElementById('pac-contacto-telefono').value.trim() || null,
            contactoEmergenciaRelacion: document.getElementById('pac-contacto-relacion').value.trim() || null,
            activo: true,
            usuario: {
                nombreUsuario: nombreUsuario,
                email: email || (nombreUsuario + '@paciente.clinica'),
                contrasenaHash: contrasena,
                rol: { idRol: 4 },
                activo: true
            }
        };
        
        // Enviar al servidor
        const response = await fetch(`${API_URL}/pacientes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(paciente)
        });
        
        if (response.ok) {
            showSuccess('Paciente registrado exitosamente');
            $('#modalNuevoPaciente').modal('hide');
            
            // Recargar lista de pacientes
            await cargarDatosIniciales();
            
        } else {
            const error = await response.text();
            if (response.status === 409) {
                showError('Ya existe un paciente con esa cédula');
            } else {
                showError('Error al registrar paciente: ' + error);
            }
        }
        
    } catch (error) {
        console.error('Error guardando paciente:', error);
        showError('Error al guardar el paciente');
    }
}

// ============================================
// VALIDAR CÉDULA (opcional - Ecuador)
// ============================================
function validarCedulaEcuatoriana(cedula) {
    if (cedula.length !== 10) return false;
    
    const digitos = cedula.split('').map(Number);
    const provincia = digitos[0] * 10 + digitos[1];
    
    if (provincia < 1 || provincia > 24) return false;
    
    const coeficientes = [2, 1, 2, 1, 2, 1, 2, 1, 2];
    let suma = 0;
    
    for (let i = 0; i < 9; i++) {
        let producto = digitos[i] * coeficientes[i];
        if (producto >= 10) producto -= 9;
        suma += producto;
    }
    
    const digitoVerificador = suma % 10 === 0 ? 0 : 10 - (suma % 10);
    
    return digitoVerificador === digitos[9];
}
