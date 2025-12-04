// Función para convertir texto a MAYÚSCULAS
function capitalizeText(text) {
    if (!text) return '';
    return text.toUpperCase();
}

// Cargar datos al iniciar
window.addEventListener('DOMContentLoaded', () => {
    loadMedicos();
    loadEspecialidadesFilter();
    loadEspecialidadesSelect();
    
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
});

// Función para abrir modal y resetear campos de usuario
function openModalNuevoMedico() {
    document.getElementById('medicoForm').reset();
    document.getElementById('medicoId').value = '';
    document.getElementById('usuarioFields').style.display = 'block';
    document.getElementById('nombreUsuario').setAttribute('required', 'required');
    document.getElementById('contrasena').setAttribute('required', 'required');
    document.getElementById('modalTitle').textContent = 'Nuevo Médico';
    openModal('medicoModal');
}

async function loadMedicos() {
    try {
        const especialidadId = document.getElementById('especialidadFilter').value;
        
        // Cargar TODOS los médicos (activos e inactivos)
        let url = `${API_URL}/medicos`;
        if (especialidadId) {
            url += `/especialidad/${especialidadId}`;
        }
        
        const response = await fetch(url, { credentials: 'include' });
        const medicos = await response.json();
        
        // Separar médicos activos e inactivos
        const medicosActivos = medicos.filter(m => m.activo);
        const medicosInactivos = medicos.filter(m => !m.activo);
        
        // Renderizar tabla de ACTIVOS
        const tbodyActivos = document.getElementById('medicosActivosBody');
        if (medicosActivos.length === 0) {
            tbodyActivos.innerHTML = '<tr><td colspan="6">No hay médicos activos</td></tr>';
        } else {
            tbodyActivos.innerHTML = medicosActivos.map(m => `
                <tr>
                    <td>${m.cedula}</td>
                    <td>${m.nombres} ${m.apellidos}</td>
                    <td>${m.especialidad.nombreEspecialidad}</td>
                    <td>${m.telefono || 'N/A'}</td>
                    <td>${m.usuario.email || 'N/A'}</td>
                    <td>
                        <button onclick="editMedico(${m.idMedico})" class="btn btn-secondary"><i class="fas fa-edit"></i> Editar</button>
                        <button onclick="deleteMedico(${m.idMedico}, '${m.nombres} ${m.apellidos}')" class="btn btn-danger"><i class="fas fa-trash"></i> Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
        
        // Renderizar tabla de INACTIVOS
        const tbodyInactivos = document.getElementById('medicosInactivosBody');
        if (medicosInactivos.length === 0) {
            tbodyInactivos.innerHTML = '<tr><td colspan="6">No hay médicos inactivos</td></tr>';
        } else {
            tbodyInactivos.innerHTML = medicosInactivos.map(m => `
                <tr style="background-color: #ffebee;">
                    <td>${m.cedula}</td>
                    <td>${m.nombres} ${m.apellidos}</td>
                    <td>${m.especialidad.nombreEspecialidad}</td>
                    <td>${m.telefono || 'N/A'}</td>
                    <td>${m.usuario.email || 'N/A'}</td>
                    <td>
                        <button onclick="reactivarMedico(${m.idMedico}, '${m.nombres} ${m.apellidos}')" class="btn btn-primary"><i class="fas fa-undo"></i> Reactivar</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error cargando médicos:', error);
        showError('Error cargando médicos');
    }
}

async function loadEspecialidadesFilter() {
    try {
        const response = await fetch(`${API_URL}/especialidades?activas=true`, { credentials: 'include' });
        const especialidades = await response.json();
        
        const select = document.getElementById('especialidadFilter');
        select.innerHTML = '<option value="">Todas las especialidades</option>' +
            especialidades.map(e => `<option value="${e.idEspecialidad}">${e.nombreEspecialidad}</option>`).join('');
    } catch (error) {
        console.error('Error cargando especialidades:', error);
    }
}

async function loadEspecialidadesSelect() {
    try {
        const response = await fetch(`${API_URL}/especialidades?activas=true`, { credentials: 'include' });
        const especialidades = await response.json();
        
        const select = document.getElementById('especialidadId');
        select.innerHTML = '<option value="">Seleccione especialidad</option>' +
            especialidades.map(e => `<option value="${e.idEspecialidad}">${e.nombreEspecialidad}</option>`).join('');
    } catch (error) {
        console.error('Error cargando especialidades:', error);
    }
}

document.getElementById('medicoForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const medicoId = document.getElementById('medicoId').value;
    const especialidadId = document.getElementById('especialidadId').value;
    
    if (!especialidadId) {
        showError('Debe seleccionar una especialidad');
        return;
    }
    
    const medico = {
        cedula: document.getElementById('cedula').value,
        nombres: document.getElementById('nombres').value,
        apellidos: document.getElementById('apellidos').value,
        especialidad: { idEspecialidad: parseInt(especialidadId) },
        fechaNacimiento: document.getElementById('fechaNacimiento').value || null,
        numeroLicencia: document.getElementById('numeroLicencia').value || null,
        telefono: document.getElementById('telefono').value || '',
        direccion: document.getElementById('direccion').value || '',
        fotoUrl: document.getElementById('fotoUrl').value || null,
        fechaContratacion: document.getElementById('fechaContratacion').value || null,
        activo: document.getElementById('activo').checked
    };
    
    // Si es nuevo médico, necesitamos crear el usuario también
    if (!medicoId) {
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
        
        medico.usuario = {
            nombreUsuario: nombreUsuario,
            email: nombreUsuario + '@clinica.local',
            contrasenaHash: contrasena,
            rol: { idRol: 2 },
            activo: true
        };
    }
    
    try {
        let url, method;
        
        if (medicoId) {
            // Editar médico existente
            medico.idMedico = parseInt(medicoId);
            url = `${API_URL}/medicos/${medicoId}`;
            method = 'PUT';
        } else {
            // Crear nuevo médico
            url = `${API_URL}/medicos`;
            method = 'POST';
        }
        
        console.log('Enviando:', method, url, medico);
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(medico)
        });
        
        if (response.ok) {
            showSuccess('Médico guardado exitosamente');
            closeModal('medicoModal');
            loadMedicos();
            document.getElementById('medicoForm').reset();
            document.getElementById('medicoId').value = '';
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            showError('Error guardando médico: ' + errorText);
        }
    } catch (error) {
        console.error('Error completo:', error);
        showError('Error de conexión: ' + error.message);
    }
});

async function editMedico(id) {
    try {
        const response = await fetch(`${API_URL}/medicos/${id}`, { credentials: 'include' });
        const medico = await response.json();
        
        document.getElementById('medicoId').value = medico.idMedico;
        document.getElementById('cedula').value = medico.cedula;
        document.getElementById('nombres').value = medico.nombres;
        document.getElementById('apellidos').value = medico.apellidos;
        document.getElementById('especialidadId').value = medico.especialidad.idEspecialidad;
        document.getElementById('fechaNacimiento').value = medico.fechaNacimiento || '';
        document.getElementById('numeroLicencia').value = medico.numeroLicencia || '';
        document.getElementById('telefono').value = medico.telefono || '';
        document.getElementById('fotoUrl').value = medico.fotoUrl || '';
        document.getElementById('direccion').value = medico.direccion || '';
        document.getElementById('fechaContratacion').value = medico.fechaContratacion || '';
        document.getElementById('activo').checked = medico.activo;
        
        // Ocultar campos de usuario en edición
        document.getElementById('usuarioFields').style.display = 'none';
        document.getElementById('nombreUsuario').removeAttribute('required');
        document.getElementById('contrasena').removeAttribute('required');
        
        document.getElementById('modalTitle').textContent = 'Editar Médico';
        openModal('medicoModal');
    } catch (error) {
        console.error('Error cargando médico:', error);
        showError('Error cargando datos del médico');
    }
}

async function deleteMedico(id, nombre) {
    if (!confirm(`¿Está seguro de DESACTIVAR al médico ${nombre}?\n\nEsto lo moverá a la sección de Inactivos.`)) return;
    
    try {
        const response = await fetch(`${API_URL}/medicos/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });
        
        if (response.ok) {
            showSuccess('Médico desactivado exitosamente');
            loadMedicos();
        } else {
            showError('Error desactivando médico');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}

async function reactivarMedico(id, nombre) {
    if (!confirm(`¿Está seguro de REACTIVAR al médico ${nombre}?`)) return;
    
    try {
        // Obtener médico actual
        const getResponse = await fetch(`${API_URL}/medicos/${id}`, { credentials: 'include' });
        const medico = await getResponse.json();
        
        // Actualizar estado a activo
        medico.activo = true;
        
        const response = await fetch(`${API_URL}/medicos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(medico)
        });
        
        if (response.ok) {
            showSuccess('Médico reactivado exitosamente');
            loadMedicos();
        } else {
            showError('Error reactivando médico');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}
