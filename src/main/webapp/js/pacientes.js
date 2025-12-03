// Función para convertir texto a MAYÚSCULAS
function capitalizeText(text) {
    if (!text) return '';
    return text.toUpperCase();
}

// Cargar pacientes al iniciar
window.addEventListener('DOMContentLoaded', () => {
    loadPacientes();
    
    // Calcular edad automáticamente cuando cambie la fecha de nacimiento
    const fechaNacimientoInput = document.getElementById('fechaNacimiento');
    if (fechaNacimientoInput) {
        fechaNacimientoInput.addEventListener('change', calcularEdad);
    }
    
    // Validar que cédula y teléfono solo acepten números
    const cedulaInput = document.getElementById('cedula');
    const telefonoInput = document.getElementById('telefono');
    
    if (cedulaInput) {
        cedulaInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '').substring(0, 10);
        });
    }
    
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '').substring(0, 10);
        });
    }
    
    // Convertir a mayúsculas en tiempo real mientras se escribe
    const nombresInput = document.getElementById('nombres');
    const apellidosInput = document.getElementById('apellidos');
    const direccionInput = document.getElementById('direccion');
    const emailInput = document.getElementById('email');
    
    if (nombresInput) {
        nombresInput.addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
    }
    
    if (apellidosInput) {
        apellidosInput.addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
    }
    
    if (direccionInput) {
        direccionInput.addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
    }
    
    if (emailInput) {
        emailInput.addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
        // Validar formato de email al salir del campo
        emailInput.addEventListener('blur', function() {
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/i;
            if (this.value && !emailPattern.test(this.value)) {
                alert('Por favor ingrese un correo electrónico válido');
                this.focus();
            }
        });
    }
});

// Función para abrir modal y resetear campos de usuario
function openModalNuevoPaciente() {
    document.getElementById('pacienteForm').reset();
    document.getElementById('pacienteId').value = '';
    document.getElementById('nombreUsuario').setAttribute('required', 'required');
    document.getElementById('contrasena').setAttribute('required', 'required');
    document.getElementById('fechaNacimiento').setAttribute('required', 'required');
    document.getElementById('modalTitle').textContent = 'Nuevo Paciente';
    openModal('pacienteModal');
}

// Función para calcular la edad basada en la fecha de nacimiento
function calcularEdad() {
    const fechaNacimiento = document.getElementById('fechaNacimiento').value;
    if (!fechaNacimiento) {
        document.getElementById('edad').value = '';
        return;
    }
    
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    
    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    
    document.getElementById('edad').value = edad;
}

async function loadPacientes() {
    try {
        const response = await fetch(`${API_URL}/pacientes`, { credentials: 'include' });
        const pacientes = await response.json();
        
        // Separar activos e inactivos
        const activos = pacientes.filter(p => p.activo);
        const inactivos = pacientes.filter(p => !p.activo);
        
        // Renderizar tabla de ACTIVOS
        const tbodyActivos = document.getElementById('pacientesActivosBody');
        if (activos.length === 0) {
            tbodyActivos.innerHTML = '<tr><td colspan="6">No hay pacientes activos</td></tr>';
        } else {
            tbodyActivos.innerHTML = activos.map(p => `
                <tr>
                    <td>${p.cedula}</td>
                    <td>${p.nombres} ${p.apellidos}</td>
                    <td>${p.edad || 'N/A'}</td>
                    <td>${p.telefono || 'N/A'}</td>
                    <td>${p.email || 'N/A'}</td>
                    <td>
                        <button onclick="editPaciente(${p.idPaciente})" class="btn btn-secondary">✏️ Editar</button>
                        <button onclick="deletePaciente(${p.idPaciente}, '${p.nombres} ${p.apellidos}')" class="btn btn-danger">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
        
        // Renderizar tabla de INACTIVOS
        const tbodyInactivos = document.getElementById('pacientesInactivosBody');
        if (inactivos.length === 0) {
            tbodyInactivos.innerHTML = '<tr><td colspan="6">No hay pacientes inactivos</td></tr>';
        } else {
            tbodyInactivos.innerHTML = inactivos.map(p => `
                <tr style="background-color: #ffebee;">
                    <td>${p.cedula}</td>
                    <td>${p.nombres} ${p.apellidos}</td>
                    <td>${p.edad || 'N/A'}</td>
                    <td>${p.telefono || 'N/A'}</td>
                    <td>${p.email || 'N/A'}</td>
                    <td>
                        <button onclick="reactivarPaciente(${p.idPaciente}, '${p.nombres} ${p.apellidos}')" class="btn btn-primary">♻️ Reactivar</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error cargando pacientes:', error);
        showError('Error cargando pacientes');
    }
}

async function searchPacientes() {
    const searchTerm = document.getElementById('searchInput').value;
    if (!searchTerm) {
        loadPacientes();
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/pacientes/search/${searchTerm}`, { credentials: 'include' });
        const pacientes = await response.json();
        
        // Separar activos e inactivos en búsqueda también
        const activos = pacientes.filter(p => p.activo);
        const inactivos = pacientes.filter(p => !p.activo);
        
        // Actualizar tabla de activos
        const tbodyActivos = document.getElementById('pacientesActivosBody');
        if (activos.length === 0) {
            tbodyActivos.innerHTML = '<tr><td colspan="6">No se encontraron pacientes activos</td></tr>';
        } else {
            tbodyActivos.innerHTML = activos.map(p => `
                <tr>
                    <td>${p.cedula}</td>
                    <td>${p.nombres} ${p.apellidos}</td>
                    <td>${p.edad || 'N/A'}</td>
                    <td>${p.telefono || 'N/A'}</td>
                    <td>${p.email || 'N/A'}</td>
                    <td>
                        <button onclick="editPaciente(${p.idPaciente})" class="btn btn-secondary">✏️ Editar</button>
                        <button onclick="deletePaciente(${p.idPaciente}, '${p.nombres} ${p.apellidos}')" class="btn btn-danger">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
        
        // Actualizar tabla de inactivos
        const tbodyInactivos = document.getElementById('pacientesInactivosBody');
        if (inactivos.length === 0) {
            tbodyInactivos.innerHTML = '<tr><td colspan="6">No se encontraron pacientes inactivos</td></tr>';
        } else {
            tbodyInactivos.innerHTML = inactivos.map(p => `
                <tr style="background-color: #ffebee;">
                    <td>${p.cedula}</td>
                    <td>${p.nombres} ${p.apellidos}</td>
                    <td>${p.edad || 'N/A'}</td>
                    <td>${p.telefono || 'N/A'}</td>
                    <td>${p.email || 'N/A'}</td>
                    <td>
                        <button onclick="reactivarPaciente(${p.idPaciente}, '${p.nombres} ${p.apellidos}')" class="btn btn-primary">♻️ Reactivar</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error buscando pacientes:', error);
    }
}

document.getElementById('pacienteForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const pacienteId = document.getElementById('pacienteId').value;
    
    const emailValue = document.getElementById('email').value.trim();
    
    const fechaNacimiento = document.getElementById('fechaNacimiento').value;
    if (!fechaNacimiento) {
        showError('La fecha de nacimiento es obligatoria');
        return;
    }
    
    const paciente = {
        cedula: document.getElementById('cedula').value,
        nombres: document.getElementById('nombres').value,
        apellidos: document.getElementById('apellidos').value,
        fechaNacimiento: fechaNacimiento,
        edad: parseInt(document.getElementById('edad').value) || null,
        genero: document.getElementById('genero').value,
        telefono: document.getElementById('telefono').value || '',
        email: emailValue || null,
        direccion: document.getElementById('direccion').value || '',
        ciudad: document.getElementById('ciudad').value || '',
        tipoSangre: document.getElementById('tipoSangre').value || null,
        activo: true
        // NO enviar fechaRegistro - el backend lo genera automáticamente
    };
    
    // Si es nuevo paciente, crear usuario
    if (!pacienteId) {
        const nombreUsuario = document.getElementById('nombreUsuario').value;
        const contrasena = document.getElementById('contrasena').value;
        
        if (!nombreUsuario || !contrasena) {
            showError('Nombre de usuario y contraseña son obligatorios');
            return;
        }
        
        if (contrasena.length < 6) {
            showError('La contraseña debe tener al menos 6 caracteres');
            return;
        }
        
        paciente.usuario = {
            nombreUsuario: nombreUsuario,
            email: emailValue || nombreUsuario + '@clinica.local',
            contrasenaHash: contrasena,
            rol: { idRol: 4 },
            activo: true
        };
    }
    
    try {
        let url, method;
        
        if (pacienteId) {
            // Editar paciente existente
            paciente.idPaciente = parseInt(pacienteId);
            url = `${API_URL}/pacientes/${pacienteId}`;
            method = 'PUT';
        } else {
            // Crear nuevo paciente
            url = `${API_URL}/pacientes`;
            method = 'POST';
        }
        
        console.log('Enviando:', method, url, paciente);
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(paciente)
        });
        
        if (response.ok) {
            showSuccess('Paciente guardado exitosamente');
            closeModal('pacienteModal');
            loadPacientes();
            document.getElementById('pacienteForm').reset();
            document.getElementById('pacienteId').value = '';
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            showError('Error guardando paciente: ' + errorText);
        }
    } catch (error) {
        console.error('Error completo:', error);
        showError('Error de conexión: ' + error.message);
    }
});

async function editPaciente(id) {
    try {
        const response = await fetch(`${API_URL}/pacientes/${id}`, { credentials: 'include' });
        const paciente = await response.json();
        
        document.getElementById('pacienteId').value = paciente.idPaciente;
        document.getElementById('cedula').value = paciente.cedula;
        document.getElementById('nombres').value = paciente.nombres;
        document.getElementById('apellidos').value = paciente.apellidos;
        document.getElementById('fechaNacimiento').value = paciente.fechaNacimiento || '';
        document.getElementById('edad').value = paciente.edad || '';
        document.getElementById('tipoSangre').value = paciente.tipoSangre || '';
        document.getElementById('genero').value = paciente.genero;
        document.getElementById('telefono').value = paciente.telefono || '';
        document.getElementById('email').value = paciente.email || '';
        document.getElementById('direccion').value = paciente.direccion || '';
        document.getElementById('ciudad').value = paciente.ciudad || '';
        
        document.getElementById('modalTitle').textContent = 'Editar Paciente';
        openModal('pacienteModal');
    } catch (error) {
        console.error('Error cargando paciente:', error);
        showError('Error cargando datos del paciente');
    }
}

async function deletePaciente(id, nombre) {
    if (!confirm(`¿Está seguro de DESACTIVAR al paciente ${nombre}?\n\nEsto lo moverá a la sección de Inactivos.`)) return;
    
    try {
        const response = await fetch(`${API_URL}/pacientes/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });
        
        if (response.ok) {
            showSuccess('Paciente desactivado exitosamente');
            loadPacientes();
        } else {
            showError('Error desactivando paciente');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}

async function reactivarPaciente(id, nombre) {
    if (!confirm(`¿Está seguro de REACTIVAR al paciente ${nombre}?`)) return;
    
    try {
        // Obtener paciente actual
        const getResponse = await fetch(`${API_URL}/pacientes/${id}`, { credentials: 'include' });
        const paciente = await getResponse.json();
        
        // Actualizar estado a activo
        paciente.activo = true;
        
        const response = await fetch(`${API_URL}/pacientes/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(paciente)
        });
        
        if (response.ok) {
            showSuccess('Paciente reactivado exitosamente');
            loadPacientes();
        } else {
            showError('Error reactivando paciente');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}
