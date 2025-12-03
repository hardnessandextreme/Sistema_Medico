// Función para convertir texto a MAYÚSCULAS
function capitalizeText(text) {
    if (!text) return '';
    return text.toUpperCase();
}

// Cargar recepcionistas al iniciar
window.addEventListener('DOMContentLoaded', () => {
    loadRecepcionistas();
    
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
function openNewRecepcionistaModal() {
    document.getElementById('recepcionistaForm').reset();
    document.getElementById('recepcionistaId').value = '';
    document.getElementById('usuarioFields').style.display = 'block';
    document.getElementById('nombreUsuario').setAttribute('required', 'required');
    document.getElementById('contrasena').setAttribute('required', 'required');
    document.getElementById('modalTitle').textContent = '➕ Nueva Recepcionista';
    openModal('recepcionistaModal');
}

async function loadRecepcionistas() {
    try {
        const response = await fetch(`${API_URL}/recepcionistas`, { credentials: 'include' });
        const recepcionistas = await response.json();
        
        // Separar activas e inactivas
        const activas = recepcionistas.filter(r => r.activo);
        const inactivas = recepcionistas.filter(r => !r.activo);
        
        // Renderizar tabla de ACTIVAS
        const tbodyActivas = document.getElementById('recepcionistasActivasBody');
        if (activas.length === 0) {
            tbodyActivas.innerHTML = '<tr><td colspan="6">No hay recepcionistas activas</td></tr>';
        } else {
            tbodyActivas.innerHTML = activas.map(r => `
                <tr>
                    <td>${r.cedula}</td>
                    <td>${r.nombres} ${r.apellidos}</td>
                    <td>${r.telefono || 'N/A'}</td>
                    <td>${r.usuario && r.usuario.email ? r.usuario.email : 'N/A'}</td>
                    <td>${r.usuario ? r.usuario.nombreUsuario : 'N/A'}</td>
                    <td>
                        <button onclick="editRecepcionista(${r.idRecepcionista})" class="btn btn-secondary">✏️ Editar</button>
                        <button onclick="deleteRecepcionista(${r.idRecepcionista}, '${r.nombres} ${r.apellidos}')" class="btn btn-danger">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
        
        // Renderizar tabla de INACTIVAS
        const tbodyInactivas = document.getElementById('recepcionistasInactivasBody');
        if (inactivas.length === 0) {
            tbodyInactivas.innerHTML = '<tr><td colspan="6">No hay recepcionistas inactivas</td></tr>';
        } else {
            tbodyInactivas.innerHTML = inactivas.map(r => `
                <tr style="background-color: #ffebee;">
                    <td>${r.cedula}</td>
                    <td>${r.nombres} ${r.apellidos}</td>
                    <td>${r.telefono || 'N/A'}</td>
                    <td>${r.usuario && r.usuario.email ? r.usuario.email : 'N/A'}</td>
                    <td>${r.usuario ? r.usuario.nombreUsuario : 'N/A'}</td>
                    <td>
                        <button onclick="reactivarRecepcionista(${r.idRecepcionista}, '${r.nombres} ${r.apellidos}')" class="btn btn-primary">♻️ Reactivar</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error cargando recepcionistas:', error);
        showError('Error cargando recepcionistas');
    }
}

document.getElementById('recepcionistaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const recepcionistaId = document.getElementById('recepcionistaId').value;
    const emailValue = document.getElementById('nombreUsuario').value.trim() + '@clinica.local';
    
    const recepcionista = {
        cedula: document.getElementById('cedula').value,
        nombres: document.getElementById('nombres').value,
        apellidos: document.getElementById('apellidos').value,
        fechaNacimiento: document.getElementById('fechaNacimiento').value || null,
        telefono: document.getElementById('telefono').value || '',
        direccion: document.getElementById('direccion').value || '',
        fechaContratacion: document.getElementById('fechaContratacion').value || null,
        activo: document.getElementById('activo').checked
    };
    
    // Si es nueva recepcionista, crear usuario
    if (!recepcionistaId) {
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
        
        recepcionista.usuario = {
            nombreUsuario: nombreUsuario,
            email: emailValue || nombreUsuario + '@clinica.local',
            contrasenaHash: contrasena,
            rol: { idRol: 3 },
            activo: true
        };
    }
    
    try {
        let url, method;
        
        if (recepcionistaId) {
            recepcionista.idRecepcionista = parseInt(recepcionistaId);
            url = `${API_URL}/recepcionistas/${recepcionistaId}`;
            method = 'PUT';
        } else {
            url = `${API_URL}/recepcionistas`;
            method = 'POST';
        }
        
        console.log('Enviando:', method, url, recepcionista);
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(recepcionista)
        });
        
        if (response.ok) {
            showSuccess('Recepcionista guardada exitosamente');
            closeModal('recepcionistaModal');
            loadRecepcionistas();
            document.getElementById('recepcionistaForm').reset();
            document.getElementById('recepcionistaId').value = '';
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            showError('Error guardando recepcionista: ' + errorText);
        }
    } catch (error) {
        console.error('Error completo:', error);
        showError('Error de conexión: ' + error.message);
    }
});

async function editRecepcionista(id) {
    try {
        const response = await fetch(`${API_URL}/recepcionistas/${id}`, { credentials: 'include' });
        const recepcionista = await response.json();
        
        document.getElementById('recepcionistaId').value = recepcionista.idRecepcionista;
        document.getElementById('cedula').value = recepcionista.cedula;
        document.getElementById('nombres').value = recepcionista.nombres;
        document.getElementById('apellidos').value = recepcionista.apellidos;
        document.getElementById('fechaNacimiento').value = recepcionista.fechaNacimiento || '';
        document.getElementById('telefono').value = recepcionista.telefono || '';
        document.getElementById('direccion').value = recepcionista.direccion || '';
        document.getElementById('fechaContratacion').value = recepcionista.fechaContratacion || '';
        document.getElementById('activo').checked = recepcionista.activo;
        
        // Ocultar campos de usuario en edición
        document.getElementById('usuarioFields').style.display = 'none';
        document.getElementById('nombreUsuario').removeAttribute('required');
        document.getElementById('contrasena').removeAttribute('required');
        
        document.getElementById('modalTitle').textContent = '✏️ Editar Recepcionista';
        openModal('recepcionistaModal');
    } catch (error) {
        console.error('Error cargando recepcionista:', error);
        showError('Error cargando datos de recepcionista');
    }
}

async function deleteRecepcionista(id, nombre) {
    if (!confirm(`¿Está seguro de DESACTIVAR a la recepcionista ${nombre}?\n\nEsto la moverá a la sección de Inactivas.`)) return;
    
    try {
        const response = await fetch(`${API_URL}/recepcionistas/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });
        
        if (response.ok) {
            showSuccess('Recepcionista desactivada exitosamente');
            loadRecepcionistas();
        } else {
            showError('Error desactivando recepcionista');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}

async function reactivarRecepcionista(id, nombre) {
    if (!confirm(`¿Está seguro de REACTIVAR a la recepcionista ${nombre}?`)) return;
    
    try {
        // Obtener recepcionista actual
        const getResponse = await fetch(`${API_URL}/recepcionistas/${id}`, { credentials: 'include' });
        const recepcionista = await getResponse.json();
        
        // Actualizar estado a activo
        recepcionista.activo = true;
        
        const response = await fetch(`${API_URL}/recepcionistas/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(recepcionista)
        });
        
        if (response.ok) {
            showSuccess('Recepcionista reactivada exitosamente');
            loadRecepcionistas();
        } else {
            showError('Error reactivando recepcionista');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}

// Al abrir modal para nuevo, mostrar campos de usuario
function openModal(modalId) {
    if (modalId === 'recepcionistaModal') {
        document.getElementById('usuarioFields').style.display = 'block';
        document.getElementById('nombreUsuario').setAttribute('required', 'required');
        document.getElementById('contrasena').setAttribute('required', 'required');
        document.getElementById('modalTitle').textContent = 'Nueva Recepcionista';
    }
    document.getElementById(modalId).style.display = 'flex';
}
