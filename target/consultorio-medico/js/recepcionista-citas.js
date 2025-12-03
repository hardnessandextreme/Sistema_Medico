// ============================================
// GESTIÓN DE CITAS - PANEL RECEPCIONISTA
// ============================================

let medicosDisponibles = [];
let pacientesActivos = [];
let especialidades = [];

// ============================================
// CARGAR DATOS INICIALES
// ============================================
async function cargarDatosIniciales() {
    try {
        const [medicosResp, pacientesResp, especialidadesResp] = await Promise.all([
            fetch(`${API_URL}/medicos`, {credentials: 'include'}),
            fetch(`${API_URL}/pacientes`, {credentials: 'include'}),
            fetch(`${API_URL}/especialidades`, {credentials: 'include'})
        ]);
        
        const todosmedicos = await medicosResp.json();
        const todosPacientes = await pacientesResp.json();
        especialidades = await especialidadesResp.json();
        
        // Filtrar solo activos
        medicosDisponibles = todosmedicos.filter(m => m.activo === true);
        pacientesActivos = todosPacientes.filter(p => p.activo === true);
        
        cargarPacientesSelect();
        cargarEspecialidadesSelect();
        
    } catch (error) {
        console.error('Error cargando datos iniciales:', error);
        showError('Error al cargar datos del sistema');
    }
}

// ============================================
// CARGAR PACIENTES EN SELECT
// ============================================
function cargarPacientesSelect() {
    const select = document.getElementById('paciente-cita');
    select.innerHTML = '<option value="">Seleccione un paciente</option>';
    
    pacientesActivos.forEach(p => {
        const option = document.createElement('option');
        option.value = p.idPaciente;
        option.textContent = `${p.nombres} ${p.apellidos} - ${p.cedula}`;
        select.appendChild(option);
    });
}

// ============================================
// CARGAR ESPECIALIDADES EN SELECT
// ============================================
function cargarEspecialidadesSelect() {
    const select = document.getElementById('especialidad-cita');
    select.innerHTML = '<option value="">Seleccione especialidad</option>';
    
    especialidades.filter(e => e.activo === true).forEach(esp => {
        const option = document.createElement('option');
        option.value = esp.idEspecialidad;
        option.textContent = esp.nombreEspecialidad;
        select.appendChild(option);
    });
}

// ============================================
// CARGAR MÉDICOS POR ESPECIALIDAD
// ============================================
function cargarMedicosPorEspecialidad() {
    const especialidadId = parseInt(document.getElementById('especialidad-cita').value);
    const selectMedico = document.getElementById('medico-cita');
    
    selectMedico.innerHTML = '<option value="">Seleccione un médico</option>';
    selectMedico.disabled = true;
    
    if (!especialidadId) return;
    
    const medicosFiltrados = medicosDisponibles.filter(m => 
        m.especialidad && m.especialidad.idEspecialidad === especialidadId
    );
    
    if (medicosFiltrados.length === 0) {
        selectMedico.innerHTML = '<option value="">No hay médicos disponibles</option>';
        return;
    }
    
    medicosFiltrados.forEach(m => {
        const option = document.createElement('option');
        option.value = m.idMedico;
        option.textContent = `Dr. ${m.nombres} ${m.apellidos}`;
        selectMedico.appendChild(option);
    });
    
    selectMedico.disabled = false;
}

// ============================================
// ABRIR MODAL NUEVA CITA
// ============================================
function abrirModalNuevaCita() {
    limpiarFormularioCita();
    document.getElementById('titulo-modal-cita').textContent = '➕ Nueva Cita';
    $('#modalAgendarCita').modal('show');
}

// ============================================
// LIMPIAR FORMULARIO
// ============================================
function limpiarFormularioCita() {
    document.getElementById('paciente-cita').value = '';
    document.getElementById('especialidad-cita').value = '';
    document.getElementById('medico-cita').innerHTML = '<option value="">Seleccione un médico</option>';
    document.getElementById('medico-cita').disabled = true;
    document.getElementById('fecha-cita').value = '';
    document.getElementById('hora-cita').value = '';
    document.getElementById('motivo-cita').value = '';
    document.getElementById('sintomas-cita').value = '';
}

// ============================================
// GUARDAR CITA
// ============================================
async function guardarCita() {
    try {
        const pacienteId = document.getElementById('paciente-cita').value;
        const medicoId = document.getElementById('medico-cita').value;
        const fecha = document.getElementById('fecha-cita').value;
        let hora = document.getElementById('hora-cita').value;
        const motivo = document.getElementById('motivo-cita').value.trim();
        const sintomas = document.getElementById('sintomas-cita').value.trim();
        
        // Validaciones
        if (!pacienteId || !medicoId || !fecha || !hora || !motivo) {
            showError('Por favor complete todos los campos obligatorios');
            return;
        }
        
        // Validar que la fecha no sea anterior a hoy
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);
        const fechaSeleccionada = new Date(fecha);
        if (fechaSeleccionada < hoy) {
            showError('No se puede agendar citas en fechas pasadas');
            return;
        }
        
        // Agregar segundos si solo tiene HH:MM
        if (hora && hora.length === 5) {
            hora += ':00';
        }
        
        const cita = {
            paciente: { idPaciente: parseInt(pacienteId) },
            medico: { idMedico: parseInt(medicoId) },
            fechaCita: fecha,
            horaCita: hora,
            motivoConsulta: motivo,
            sintomas: sintomas || null,
            estadoCita: { idEstadoCita: 7 } // 7 = Pendiente
        };
        
        const response = await fetch(`${API_URL}/citas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(cita)
        });
        
        if (response.ok) {
            showSuccess('Cita agendada exitosamente');
            $('#modalAgendarCita').modal('hide');
            cargarCitasDelDia();
        } else {
            const error = await response.text();
            showError('Error al agendar cita: ' + error);
        }
        
    } catch (error) {
        console.error('Error guardando cita:', error);
        showError('Error al guardar la cita');
    }
}

// ============================================
// CARGAR CITAS DEL DÍA
// ============================================
async function cargarCitasDelDia() {
    try {
        const response = await fetch(`${API_URL}/citas`, {credentials: 'include'});
        const todasCitas = await response.json();
        
        const hoy = new Date().toISOString().split('T')[0];
        const citasHoy = todasCitas.filter(c => 
            c.fechaCita && c.fechaCita.startsWith(hoy) &&
            c.paciente && c.paciente.activo === true &&
            c.medico && c.medico.activo === true
        ).sort((a, b) => a.horaCita.localeCompare(b.horaCita));
        
        const tbody = document.getElementById('tbody-citas-dia');
        tbody.innerHTML = '';
        
        if (citasHoy.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#607d8b;">No hay citas programadas para hoy</td></tr>';
            return;
        }
        
        citasHoy.forEach(cita => {
            const tr = document.createElement('tr');
            const pacienteNombre = `${cita.paciente.nombres} ${cita.paciente.apellidos}`;
            const medicoNombre = `Dr. ${cita.medico.nombres} ${cita.medico.apellidos}`;
            const estado = cita.estadoCita ? cita.estadoCita.nombreEstado : 'Pendiente';
            const estadoClass = estado.toLowerCase().replace(/ /g, '-');
            
            tr.innerHTML = `
                <td>${cita.horaCita || 'N/A'}</td>
                <td>${pacienteNombre}</td>
                <td>${medicoNombre}</td>
                <td>${cita.motivoConsulta || 'Sin motivo'}</td>
                <td><span class="badge-estado badge-${estadoClass}">${estado}</span></td>
                <td>
                    <button class="btn-sm btn-info" onclick="verDetalleCita(${cita.idCita})" title="Ver detalles">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
        
    } catch (error) {
        console.error('Error cargando citas:', error);
    }
}

// ============================================
// VER DETALLE DE CITA
// ============================================
async function verDetalleCita(idCita) {
    try {
        const response = await fetch(`${API_URL}/citas/${idCita}`, {credentials: 'include'});
        const cita = await response.json();
        
        const mensaje = `
            📅 DETALLE DE LA CITA
            
            Paciente: ${cita.paciente.nombres} ${cita.paciente.apellidos}
            Cédula: ${cita.paciente.cedula}
            Teléfono: ${cita.paciente.telefono || 'N/A'}
            
            Médico: Dr. ${cita.medico.nombres} ${cita.medico.apellidos}
            Especialidad: ${cita.medico.especialidad.nombreEspecialidad}
            
            Fecha: ${cita.fechaCita}
            Hora: ${cita.horaCita}
            
            Motivo: ${cita.motivoConsulta || 'No especificado'}
            Síntomas: ${cita.sintomas || 'No especificados'}
            
            Estado: ${cita.estadoCita.nombreEstado}
        `;
        
        alert(mensaje);
        
    } catch (error) {
        console.error('Error cargando detalle:', error);
        showError('Error al cargar detalles de la cita');
    }
}

// ============================================
// FUNCIONES DE UTILIDAD
// ============================================
function showSuccess(message) {
    alert('✅ ' + message);
}

function showError(message) {
    alert('❌ ' + message);
}

// ============================================
// INICIALIZAR
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    cargarDatosIniciales();
    cargarCitasDelDia();
    
    // Configurar fecha mínima (hoy)
    const hoy = new Date().toISOString().split('T')[0];
    const inputFecha = document.getElementById('fecha-cita');
    if (inputFecha) {
        inputFecha.min = hoy;
    }
});
