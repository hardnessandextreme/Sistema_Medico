// Cargar datos al iniciar
window.addEventListener('DOMContentLoaded', () => {
    loadCitas();
    loadPacientesSelect();
    loadMedicosSelect();
    loadEstadosSelect();
});

async function loadCitas() {
    try {
        const response = await fetch(`${API_URL}/citas`, { credentials: 'include' });
        const citas = await response.json();
        
        // Separar citas por estado
        const pendientes = citas.filter(c => c.estadoCita?.nombreEstado === 'Pendiente');
        const confirmadas = citas.filter(c => c.estadoCita?.nombreEstado === 'Confirmada');
        const atendidas = citas.filter(c => c.estadoCita?.nombreEstado === 'Atendida');
        const canceladas = citas.filter(c => c.estadoCita?.nombreEstado === 'Cancelada');
        const reprogramadas = citas.filter(c => c.estadoCita?.nombreEstado === 'Reprogramada');
        const noAsistio = citas.filter(c => c.estadoCita?.nombreEstado === 'No asistió');
        
        // Renderizar Pendientes
        renderCitasTable('citasPendientesBody', pendientes, '#FFF9C4');
        
        // Renderizar Confirmadas
        renderCitasTable('citasConfirmadasBody', confirmadas, '#C8E6C9');
        
        // Renderizar Atendidas
        renderCitasTable('citasAtendidasBody', atendidas, '#BBDEFB');
        
        // Renderizar Canceladas (mostrar motivo)
        renderCitasCanceladas('citasCanceladasBody', canceladas);
        
        // Renderizar Reprogramadas
        renderCitasTable('citasReprogramadasBody', reprogramadas, '#FFE0B2');
        
        // Renderizar No Asistió
        renderCitasTable('citasNoAsistioBody', noAsistio, '#F5F5F5');
        
    } catch (error) {
        console.error('Error cargando citas:', error);
        showError('Error cargando citas');
    }
}

function renderCitasTable(tbodyId, citas, bgColor) {
    const tbody = document.getElementById(tbodyId);
    if (citas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No hay citas en este estado</td></tr>';
        return;
    }
    
    tbody.innerHTML = citas.map(c => {
        const estadoNombre = c.estadoCita ? c.estadoCita.nombreEstado : 'Pendiente';
        return `
        <tr style="background-color: ${bgColor};">
            <td>${formatDate(c.fechaCita)}</td>
            <td>${c.horaCita}</td>
            <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
            <td>${c.medico.nombres} ${c.medico.apellidos}</td>
            <td>${c.medico.especialidad.nombreEspecialidad}</td>
            <td>
                <button onclick="editCita(${c.idCita})" class="btn btn-secondary">✏️ Editar</button>
                <button onclick="cambiarEstadoCita(${c.idCita}, '${estadoNombre}')" class="btn btn-primary">🔄 Estado</button>
                <button onclick="deleteCita(${c.idCita})" class="btn btn-danger">🗑️ Eliminar</button>
            </td>
        </tr>
        `;
    }).join('');
}

function renderCitasCanceladas(tbodyId, citas) {
    const tbody = document.getElementById(tbodyId);
    if (citas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No hay citas canceladas</td></tr>';
        return;
    }
    
    tbody.innerHTML = citas.map(c => `
        <tr style="background-color: #FFCDD2;">
            <td>${formatDate(c.fechaCita)}</td>
            <td>${c.horaCita}</td>
            <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
            <td>${c.medico.nombres} ${c.medico.apellidos}</td>
            <td>${c.motivoCancelacion || 'N/A'}</td>
            <td>
                <button onclick="editCita(${c.idCita})" class="btn btn-secondary">✏️ Ver Detalles</button>
                <button onclick="deleteCita(${c.idCita})" class="btn btn-danger">🗑️ Eliminar</button>
            </td>
        </tr>
    `).join('');
}

async function loadPacientesSelect() {
    try {
        const response = await fetch(`${API_URL}/pacientes`, { credentials: 'include' });
        const allPacientes = await response.json();
        
        // Filtrar solo pacientes activos
        const pacientes = allPacientes.filter(p => p.activo);
        
        const select = document.getElementById('pacienteId');
        select.innerHTML = '<option value="">Seleccione un paciente</option>' +
            pacientes.map(p => `<option value="${p.idPaciente}">${p.nombres} ${p.apellidos} - ${p.cedula}</option>`).join('');
    } catch (error) {
        console.error('Error cargando pacientes:', error);
    }
}

async function loadMedicosSelect() {
    try {
        const response = await fetch(`${API_URL}/medicos`, { credentials: 'include' });
        const allMedicos = await response.json();
        
        // Filtrar solo médicos activos
        const medicos = allMedicos.filter(m => m.activo);
        
        const select = document.getElementById('medicoId');
        select.innerHTML = '<option value="">Seleccione un médico</option>' +
            medicos.map(m => `<option value="${m.idMedico}">${m.nombres} ${m.apellidos} - ${m.especialidad.nombreEspecialidad}</option>`).join('');
    } catch (error) {
        console.error('Error cargando médicos:', error);
    }
}

async function loadEstadosSelect() {
    try {
        const response = await fetch(`${API_URL}/estados-cita`, { credentials: 'include' });
        const estados = await response.json();
        
        const select = document.getElementById('estadoCitaId');
        select.innerHTML = '<option value="">Seleccione un estado</option>' +
            estados.map(e => `<option value="${e.idEstadoCita}">${e.nombreEstado}</option>`).join('');
    } catch (error) {
        console.error('Error cargando estados:', error);
    }
}

document.getElementById('citaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const citaId = document.getElementById('citaId').value;
    const estadoValue = document.getElementById('estadoCitaId').value;
    const estadoCitaId = estadoValue ? parseInt(estadoValue) : 7; // 7 = Pendiente por defecto
    
    // Convertir hora de HH:MM a HH:MM:SS para LocalTime
    let horaCita = document.getElementById('horaCita').value;
    if (horaCita && horaCita.length === 5) {
        horaCita += ':00'; // Agregar segundos
    }
    
    const cita = {
        paciente: { idPaciente: parseInt(document.getElementById('pacienteId').value) },
        medico: { idMedico: parseInt(document.getElementById('medicoId').value) },
        fechaCita: document.getElementById('fechaCita').value,
        horaCita: horaCita,
        estadoCita: { idEstadoCita: estadoCitaId },
        motivoConsulta: document.getElementById('motivo').value
    };
    
    try {
        const url = citaId ? `${API_URL}/citas/${citaId}` : `${API_URL}/citas`;
        const method = citaId ? 'PUT' : 'POST';
        
        if (citaId) {
            cita.idCita = parseInt(citaId);
        }
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(cita)
        });
        
        if (response.ok) {
            showSuccess('Cita guardada exitosamente');
            closeModal('citaModal');
            loadCitas();
            document.getElementById('citaForm').reset();
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            showError('Error guardando cita: ' + errorText);
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión: ' + error.message);
    }
});

async function editCita(id) {
    try {
        const response = await fetch(`${API_URL}/citas/${id}`, { credentials: 'include' });
        const cita = await response.json();
        
        document.getElementById('citaId').value = cita.idCita;
        document.getElementById('pacienteId').value = cita.paciente.idPaciente;
        document.getElementById('medicoId').value = cita.medico.idMedico;
        document.getElementById('fechaCita').value = cita.fechaCita;
        document.getElementById('horaCita').value = cita.horaCita;
        document.getElementById('estadoCitaId').value = cita.estadoCita ? cita.estadoCita.idEstadoCita : 1;
        document.getElementById('motivo').value = cita.motivoConsulta || '';
        
        document.getElementById('modalTitle').textContent = 'Editar Cita';
        openModal('citaModal');
    } catch (error) {
        console.error('Error cargando cita:', error);
        showError('Error cargando datos de la cita');
    }
}

async function deleteCita(id) {
    if (!confirm('¿Está seguro de eliminar esta cita?')) return;
    
    try {
        const response = await fetch(`${API_URL}/citas/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });
        
        if (response.ok) {
            showSuccess('Cita eliminada exitosamente');
            loadCitas();
        } else {
            showError('Error eliminando cita');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}

function clearFilters() {
    document.getElementById('fechaInicio').value = '';
    document.getElementById('fechaFin').value = '';
    loadCitas();
}

async function cambiarEstadoCita(idCita, estadoActual) {
    try {
        // Obtener todos los estados disponibles
        const responseEstados = await fetch(`${API_URL}/estados-cita`, { credentials: 'include' });
        const estados = await responseEstados.json();
        
        // Crear opciones para el prompt
        let opciones = 'Seleccione el nuevo estado:\n\n';
        estados.forEach((e, index) => {
            opciones += `${index + 1}. ${e.nombreEstado}\n`;
        });
        
        const seleccion = prompt(opciones + '\nIngrese el número del estado:');
        if (!seleccion) return;
        
        const indice = parseInt(seleccion) - 1;
        if (indice < 0 || indice >= estados.length) {
            alert('Selección inválida');
            return;
        }
        
        const nuevoEstado = estados[indice];
        
        // Obtener la cita actual
        const responseCita = await fetch(`${API_URL}/citas/${idCita}`, { credentials: 'include' });
        const cita = await responseCita.json();
        
        // Si el nuevo estado es Cancelada, pedir motivo
        let motivoCancelacion = null;
        if (nuevoEstado.nombreEstado === 'Cancelada') {
            motivoCancelacion = prompt('Ingrese el motivo de cancelación:');
            if (!motivoCancelacion) return;
        }
        
        // Actualizar la cita
        cita.estadoCita = { idEstadoCita: nuevoEstado.idEstadoCita };
        if (motivoCancelacion) {
            cita.motivoCancelacion = motivoCancelacion;
        }
        
        const response = await fetch(`${API_URL}/citas/${idCita}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(cita)
        });
        
        if (response.ok) {
            showSuccess(`Estado cambiado a: ${nuevoEstado.nombreEstado}`);
            loadCitas();
        } else {
            showError('Error cambiando estado de la cita');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
}
