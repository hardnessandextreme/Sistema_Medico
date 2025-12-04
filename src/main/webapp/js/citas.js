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
        
        // Separar citas por estado (3 estados simplificados)
        const pendientes = citas.filter(c => c.estadoCita?.nombreEstado === 'Pendiente');
        const completadas = citas.filter(c => c.estadoCita?.nombreEstado === 'Completada');
        const canceladas = citas.filter(c => c.estadoCita?.nombreEstado === 'Cancelada');
        
        // Cargar citas eliminadas
        loadCitasEliminadas();
        
        // Renderizar Pendientes
        renderCitasTable('citasPendientesBody', pendientes, '#FFF9C4');
        
        // Renderizar Completadas
        renderCitasTable('citasCompletadasBody', completadas, '#C8E6C9');
        
        // Renderizar Canceladas (mostrar motivo)
        renderCitasCanceladas('citasCanceladasBody', canceladas);
        
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
        const esCompletada = estadoNombre === 'Completada';
        
        let botonesExtra = '';
        if (esCompletada) {
            botonesExtra = `<button onclick="verDetalleConsulta(${c.idCita})" class="btn btn-info" style="margin-right: 5px;"><i class="fas fa-eye"></i> Ver Consulta</button>`;
        }
        
        return `
        <tr style="background-color: ${bgColor};">
            <td>${formatDate(c.fechaCita)}</td>
            <td>${c.horaCita}</td>
            <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
            <td>${c.medico.nombres} ${c.medico.apellidos}</td>
            <td>${c.medico.especialidad.nombreEspecialidad}</td>
            <td>
                ${botonesExtra}
                <button onclick="editCita(${c.idCita})" class="btn btn-secondary"><i class="fas fa-edit"></i> Editar</button>
                <button onclick="cambiarEstadoCita(${c.idCita}, '${estadoNombre}')" class="btn btn-primary"><i class="fas fa-sync-alt"></i> Estado</button>
                <button onclick="deleteCita(${c.idCita})" class="btn btn-danger"><i class="fas fa-trash"></i> Eliminar</button>
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
                <button onclick="editCita(${c.idCita})" class="btn btn-secondary"><i class="fas fa-eye"></i> Ver Detalles</button>
                <button onclick="deleteCita(${c.idCita})" class="btn btn-danger"><i class="fas fa-trash"></i> Eliminar</button>
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

// ============================================
// VER DETALLE DE CONSULTA
// ============================================
async function verDetalleConsulta(idCita) {
    try {
        const response = await fetch(`${API_URL}/consultas/cita/${idCita}`, {credentials: 'include'});
        if (!response.ok) {
            showError('Esta cita no tiene consulta registrada');
            return;
        }
        
        const consulta = await response.json();
        cargarModalDetalleConsulta(consulta);
    } catch (error) {
        console.error('Error cargando detalle:', error);
        showError('Error al cargar detalles de la consulta');
    }
}

// ============================================
// CARGAR DATOS EN MODAL DE DETALLE
// ============================================
function cargarModalDetalleConsulta(consulta) {
    // Cargar datos en el modal de detalle (solo lectura)
    document.getElementById('detalle-paciente-nombre').textContent = `${consulta.paciente.nombres} ${consulta.paciente.apellidos}`;
    document.getElementById('detalle-paciente-cedula').textContent = consulta.paciente.cedula;
    document.getElementById('detalle-fecha-consulta').textContent = new Date(consulta.fechaConsulta).toLocaleString('es-EC');
    document.getElementById('detalle-motivo').value = consulta.motivoConsulta || 'N/A';
    document.getElementById('detalle-sintomas').value = consulta.sintomasPresentados || 'N/A';
    
    // Parsear signos vitales
    if (consulta.signosVitales) {
        const signos = typeof consulta.signosVitales === 'string' ? JSON.parse(consulta.signosVitales) : consulta.signosVitales;
        document.getElementById('detalle-temperatura').value = signos.temperatura || 'N/A';
        document.getElementById('detalle-presion').value = signos.presionArterial || 'N/A';
        document.getElementById('detalle-fc').value = signos.frecuenciaCardiaca || 'N/A';
        document.getElementById('detalle-fr').value = signos.frecuenciaRespiratoria || 'N/A';
        document.getElementById('detalle-sat').value = signos.saturacionOxigeno || 'N/A';
        document.getElementById('detalle-peso').value = signos.peso || 'N/A';
        document.getElementById('detalle-altura').value = signos.altura || 'N/A';
    }
    
    document.getElementById('detalle-examen-fisico').value = consulta.examenFisico || 'N/A';
    document.getElementById('detalle-diagnostico').value = consulta.diagnostico || 'N/A';
    document.getElementById('detalle-observaciones').value = consulta.observaciones || 'N/A';
    document.getElementById('detalle-recomendaciones').value = consulta.recomendaciones || 'N/A';
    document.getElementById('detalle-proxima-cita').value = consulta.proximaCita || 'N/A';
    
    // Mostrar modal
    openModal('modalDetalleConsulta');
}

// ============================================
// CARGAR CITAS ELIMINADAS
// ============================================
async function loadCitasEliminadas() {
    try {
        const response = await fetch(`${API_URL}/citas/eliminadas`, { credentials: 'include' });
        const citasEliminadas = await response.json();
        
        const tbody = document.getElementById('citasEliminadasBody');
        if (citasEliminadas.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;">No hay citas eliminadas</td></tr>';
            return;
        }
        
        tbody.innerHTML = citasEliminadas.map(c => `
            <tr style="background-color: #EEEEEE;">
                <td>${formatDate(c.fechaCita)}</td>
                <td>${c.horaCita}</td>
                <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
                <td>${c.medico.nombres} ${c.medico.apellidos}</td>
                <td>${c.medico.especialidad.nombreEspecialidad}</td>
                <td>${c.estadoCita ? c.estadoCita.nombreEstado : 'N/A'}</td>
                <td>
                    <button onclick="restaurarCita(${c.idCita})" class="btn btn-primary"><i class="fas fa-undo"></i> Restaurar</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error cargando citas eliminadas:', error);
    }
}

// ============================================
// RESTAURAR CITA ELIMINADA
// ============================================
async function restaurarCita(idCita) {
    if (!confirm('¿Está seguro de restaurar esta cita?')) return;
    
    try {
        const response = await fetch(`${API_URL}/citas/${idCita}`, { credentials: 'include' });
        const cita = await response.json();
        
        cita.activo = true;
        
        const updateResponse = await fetch(`${API_URL}/citas/${idCita}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(cita)
        });
        
        if (updateResponse.ok) {
            showSuccess('Cita restaurada exitosamente');
            loadCitas();
            loadCitasEliminadas();
        } else {
            showError('Error restaurando cita');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión');
    }
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
