// ============================================
// GESTIÓN DE CONSULTAS - PANEL MÉDICO
// ============================================

let currentCita = null;
let currentPaciente = null;
let consultaGuardada = false;

// ============================================
// CARGAR CITAS DEL DÍA
// ============================================
async function loadCitasDelDia() {
    try {
        const response = await fetch(`${API_URL}/citas`, {credentials: 'include'});
        const todasCitas = await response.json();
        
        const hoy = new Date().toISOString().split('T')[0];
        const citasHoy = todasCitas.filter(c => 
            c.fechaCita && c.fechaCita.startsWith(hoy) &&
            c.paciente && c.paciente.activo === true &&
            c.medico && c.medico.activo === true &&
            c.estadoCita && c.estadoCita.idEstadoCita === 7 // Solo PENDIENTES
        ).sort((a, b) => a.horaCita.localeCompare(b.horaCita));
        
        const tbody = document.getElementById('tbody-citas-dia');
        tbody.innerHTML = '';
        
        if (citasHoy.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#607d8b;">No hay citas programadas para hoy</td></tr>';
            return;
        }
        
        // Mostrar solo citas pendientes (sin consulta)
        citasHoy.forEach(cita => {
            const tr = document.createElement('tr');
            const pacienteNombre = `${cita.paciente.nombres} ${cita.paciente.apellidos}`;
            const estado = cita.estadoCita ? cita.estadoCita.nombreEstado : 'Pendiente';
            const estadoClass = estado.toLowerCase().replace(/ /g, '-');
            
            tr.innerHTML = `
                <td>${cita.horaCita || 'N/A'}</td>
                <td>${pacienteNombre}</td>
                <td>${cita.motivoConsulta || 'Sin motivo'}</td>
                <td><span class="badge-estado badge-${estadoClass}">${estado}</span></td>
                <td>
                    <button class="btn-sm btn-primary" onclick="abrirConsulta(${cita.idCita})">
                        <i class="fas fa-notes-medical"></i> Atender
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
// VERIFICAR SI CITA YA TIENE CONSULTA
// ============================================
async function verificarSiTieneConsulta(idCita) {
    try {
        const response = await fetch(`${API_URL}/consultas/cita/${idCita}`, {credentials: 'include'});
        return response.ok;
    } catch (error) {
        return false;
    }
}

// ============================================
// ABRIR MODAL DE CONSULTA
// ============================================
async function abrirConsulta(idCita) {
    try {
        // Cargar datos de la cita
        const response = await fetch(`${API_URL}/citas/${idCita}`, {credentials: 'include'});
        currentCita = await response.json();
        
        if (!currentCita.paciente) {
            showError('No se pudo cargar la información del paciente');
            return;
        }
        
        currentPaciente = currentCita.paciente;
        consultaGuardada = false; // Resetear flag
        
        // Verificar si ya existe una consulta para esta cita
        const tieneConsulta = await verificarSiTieneConsulta(idCita);
        if (tieneConsulta) {
            showError('Esta cita ya tiene una consulta registrada');
            return;
        }
        
        limpiarFormularioConsulta(); // Iniciar con formulario limpio
        
        // Llenar datos del paciente
        document.getElementById('paciente-nombre').textContent = `${currentPaciente.nombres} ${currentPaciente.apellidos}`;
        document.getElementById('paciente-cedula').textContent = currentPaciente.cedula;
        document.getElementById('paciente-edad').textContent = currentPaciente.edad ? `${currentPaciente.edad} años` : 'N/A';
        document.getElementById('paciente-alergias').textContent = currentPaciente.alergias || 'Ninguna registrada';
        document.getElementById('paciente-enfermedades').textContent = currentPaciente.enfermedadesCronicas || 'Ninguna registrada';
        
        // Llenar datos de la cita
        document.getElementById('motivo-consulta-display').textContent = currentCita.motivoConsulta || 'Sin motivo especificado';
        document.getElementById('sintomas-previos').textContent = currentCita.sintomas || 'No especificados';
        
        // Cargar historial de consultas previas
        await cargarHistorialConsultas(currentPaciente.idPaciente);
        
        // Mostrar modal
        $('#modalConsulta').modal('show');
        
    } catch (error) {
        console.error('Error abriendo consulta:', error);
        showError('Error al cargar la consulta');
    }
}

// ============================================
// CARGAR CONSULTA EXISTENTE
// ============================================
function cargarConsultaExistente(consulta) {
    document.getElementById('temperatura').value = '';
    document.getElementById('presion-arterial').value = '';
    document.getElementById('frecuencia-cardiaca').value = '';
    document.getElementById('frecuencia-respiratoria').value = '';
    document.getElementById('saturacion-oxigeno').value = '';
    document.getElementById('peso').value = '';
    document.getElementById('altura').value = '';
    
    // Parsear signos vitales si existen
    if (consulta.signosVitales) {
        const signos = typeof consulta.signosVitales === 'string' ? 
            JSON.parse(consulta.signosVitales) : consulta.signosVitales;
        
        document.getElementById('temperatura').value = signos.temperatura || '';
        document.getElementById('presion-arterial').value = signos.presionArterial || '';
        document.getElementById('frecuencia-cardiaca').value = signos.frecuenciaCardiaca || '';
        document.getElementById('frecuencia-respiratoria').value = signos.frecuenciaRespiratoria || '';
        document.getElementById('saturacion-oxigeno').value = signos.saturacionOxigeno || '';
        document.getElementById('peso').value = signos.peso || '';
        document.getElementById('altura').value = signos.altura || '';
    }
    
    document.getElementById('sintomas-actuales').value = consulta.sintomasPresentados || '';
    document.getElementById('examen-fisico').value = consulta.examenFisico || '';
    document.getElementById('diagnostico').value = consulta.diagnostico || '';
    document.getElementById('observaciones-consulta').value = consulta.observaciones || '';
    document.getElementById('recomendaciones').value = consulta.recomendaciones || '';
    document.getElementById('proxima-cita').value = consulta.proximaCita || '';
}

// ============================================
// LIMPIAR FORMULARIO
// ============================================
function limpiarFormularioConsulta() {
    document.getElementById('temperatura').value = '';
    document.getElementById('presion-arterial').value = '';
    document.getElementById('frecuencia-cardiaca').value = '';
    document.getElementById('frecuencia-respiratoria').value = '';
    document.getElementById('saturacion-oxigeno').value = '';
    document.getElementById('peso').value = '';
    document.getElementById('altura').value = '';
    document.getElementById('sintomas-actuales').value = '';
    document.getElementById('examen-fisico').value = '';
    document.getElementById('diagnostico').value = '';
    document.getElementById('observaciones-consulta').value = '';
    document.getElementById('recomendaciones').value = '';
    document.getElementById('proxima-cita').value = '';
}

// ============================================
// CARGAR HISTORIAL DE CONSULTAS
// ============================================
async function cargarHistorialConsultas(idPaciente) {
    try {
        const response = await fetch(`${API_URL}/consultas/paciente/${idPaciente}`, {credentials: 'include'});
        const consultas = await response.json();
        
        const tbody = document.getElementById('tbody-historial-consultas');
        tbody.innerHTML = '';
        
        if (consultas.length === 0) {
            tbody.innerHTML = '<tr><td colspan="3" style="text-align:center;color:#607d8b;">No hay consultas previas</td></tr>';
            return;
        }
        
        // Mostrar solo las últimas 5 consultas
        consultas.slice(0, 5).forEach(c => {
            const tr = document.createElement('tr');
            const fecha = new Date(c.fechaConsulta).toLocaleDateString('es-EC');
            tr.innerHTML = `
                <td>${fecha}</td>
                <td>${c.diagnostico || 'Sin diagnóstico'}</td>
                <td>${c.observaciones || 'Sin observaciones'}</td>
            `;
            tbody.appendChild(tr);
        });
        
    } catch (error) {
        console.error('Error cargando historial:', error);
    }
}

// ============================================
// GUARDAR CONSULTA
// ============================================
async function guardarConsulta() {
    try {
        // Validar todos los campos obligatorios
        const temperatura = document.getElementById('temperatura').value.trim();
        const presionArterial = document.getElementById('presion-arterial').value.trim();
        const frecuenciaCardiaca = document.getElementById('frecuencia-cardiaca').value.trim();
        const frecuenciaRespiratoria = document.getElementById('frecuencia-respiratoria').value.trim();
        const saturacionOxigeno = document.getElementById('saturacion-oxigeno').value.trim();
        const peso = document.getElementById('peso').value.trim();
        const altura = document.getElementById('altura').value.trim();
        const sintomasActuales = document.getElementById('sintomas-actuales').value.trim();
        const examenFisico = document.getElementById('examen-fisico').value.trim();
        const diagnostico = document.getElementById('diagnostico').value.trim();
        const recomendaciones = document.getElementById('recomendaciones').value.trim();
        
        // Validar que todos los campos estén completos
        if (!temperatura || !presionArterial || !frecuenciaCardiaca || !frecuenciaRespiratoria || 
            !saturacionOxigeno || !peso || !altura || !sintomasActuales || !examenFisico || 
            !diagnostico || !recomendaciones) {
            showError('Todos los campos son obligatorios. Complete toda la información de la consulta.');
            return;
        }
        
        // Validar rangos de signos vitales
        const tempNum = parseFloat(temperatura);
        if (isNaN(tempNum) || tempNum < 35 || tempNum > 42) {
            showError('Temperatura debe estar entre 35 y 42 °C');
            return;
        }
        
        const fcNum = parseInt(frecuenciaCardiaca);
        if (isNaN(fcNum) || fcNum < 40 || fcNum > 200) {
            showError('Frecuencia cardíaca debe estar entre 40 y 200 lpm');
            return;
        }
        
        const frNum = parseInt(frecuenciaRespiratoria);
        if (isNaN(frNum) || frNum < 10 || frNum > 60) {
            showError('Frecuencia respiratoria debe estar entre 10 y 60 rpm');
            return;
        }
        
        const satNum = parseInt(saturacionOxigeno);
        if (isNaN(satNum) || satNum < 70 || satNum > 100) {
            showError('Saturación de oxígeno debe estar entre 70 y 100%');
            return;
        }
        
        const pesoNum = parseFloat(peso);
        if (isNaN(pesoNum) || pesoNum < 1 || pesoNum > 300) {
            showError('Peso debe estar entre 1 y 300 kg');
            return;
        }
        
        const alturaNum = parseFloat(altura);
        if (isNaN(alturaNum) || alturaNum < 0.4 || alturaNum > 2.5) {
            showError('Altura debe estar entre 0.4 y 2.5 metros');
            return;
        }
        
        // Recopilar signos vitales
        const signosVitales = {
            temperatura: document.getElementById('temperatura').value,
            presionArterial: document.getElementById('presion-arterial').value,
            frecuenciaCardiaca: document.getElementById('frecuencia-cardiaca').value,
            frecuenciaRespiratoria: document.getElementById('frecuencia-respiratoria').value,
            saturacionOxigeno: document.getElementById('saturacion-oxigeno').value,
            peso: document.getElementById('peso').value,
            altura: document.getElementById('altura').value
        };
        
        // Formatear fecha para Java LocalDateTime (sin milisegundos ni zona horaria)
        const ahora = new Date();
        const year = ahora.getFullYear();
        const month = String(ahora.getMonth() + 1).padStart(2, '0');
        const day = String(ahora.getDate()).padStart(2, '0');
        const hours = String(ahora.getHours()).padStart(2, '0');
        const minutes = String(ahora.getMinutes()).padStart(2, '0');
        const seconds = String(ahora.getSeconds()).padStart(2, '0');
        const fechaConsulta = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
        
        const consulta = {
            cita: { idCita: currentCita.idCita },
            paciente: { idPaciente: currentPaciente.idPaciente },
            medico: { idMedico: currentCita.medico.idMedico },
            fechaConsulta: fechaConsulta,
            motivoConsulta: currentCita.motivoConsulta || 'Consulta general',
            sintomasPresentados: document.getElementById('sintomas-actuales').value.trim() || null,
            signosVitales: JSON.stringify(signosVitales), // Convertir a String JSON
            examenFisico: document.getElementById('examen-fisico').value.trim() || null,
            diagnostico: diagnostico,
            observaciones: document.getElementById('observaciones-consulta').value.trim() || null,
            recomendaciones: document.getElementById('recomendaciones').value.trim() || null,
            proximaCita: document.getElementById('proxima-cita').value || null
        };
        
        const response = await fetch(`${API_URL}/consultas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(consulta)
        });
        
        if (response.ok) {
            // Obtener el estado de asistencia seleccionado
            const estadoAsistencia = document.getElementById('estadoAsistencia').value;
            
            // Actualizar estado de la cita según asistencia
            // 9 = Atendida (se presentó), 11 = No asistió
            await actualizarEstadoCita(currentCita.idCita, parseInt(estadoAsistencia));
            
            consultaGuardada = true; // Marcar como guardada
            showSuccess('Consulta guardada exitosamente');
            $('#modalConsulta').modal('hide');
            loadCitasDelDia();
            loadHistorialCitasAtendidas(); // Recargar historial
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            try {
                const errorJson = JSON.parse(errorText);
                // Verificar si es error de duplicado
                if (errorJson.error && errorJson.error.includes('llave duplicada')) {
                    showError('Esta cita ya tiene una consulta registrada. No se puede duplicar.');
                } else {
                    showError('Error: ' + (errorJson.error || errorText));
                }
            } catch (e) {
                showError('Error al guardar consulta: ' + errorText);
            }
        }
        
    } catch (error) {
        console.error('Error guardando consulta:', error);
        showError('Error al guardar la consulta');
    }
}

// ============================================
// ACTUALIZAR ESTADO DE CITA
// ============================================
async function actualizarEstadoCita(idCita, idEstado) {
    try {
        const cita = await fetch(`${API_URL}/citas/${idCita}`, {credentials: 'include'}).then(r => r.json());
        cita.estadoCita = { idEstadoCita: idEstado };
        
        await fetch(`${API_URL}/citas/${idCita}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(cita)
        });
    } catch (error) {
        console.error('Error actualizando estado:', error);
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
// PREVENIR CIERRE DEL MODAL SIN GUARDAR
// ============================================
$('#modalConsulta').on('hide.bs.modal', function (e) {
    // Solo prevenir cierre si hay datos y no se ha guardado
    const diagnostico = document.getElementById('diagnostico').value.trim();
    
    if (diagnostico && !consultaGuardada) {
        const confirmar = confirm('⚠️ Tiene cambios sin guardar en la consulta. ¿Está seguro de cerrar sin guardar?');
        if (!confirmar) {
            e.preventDefault();
            return false;
        }
    }
});

// ============================================
// CARGAR HISTORIAL DE CITAS ATENDIDAS
// ============================================
async function loadHistorialCitasAtendidas() {
    try {
        const response = await fetch(`${API_URL}/citas`, {credentials: 'include'});
        const todasCitas = await response.json();
        
        const hoy = new Date().toISOString().split('T')[0];
        
        // Filtrar citas de hoy que NO estén pendientes (ya atendidas o no se presentó)
        const citasAtendidas = todasCitas.filter(c => 
            c.fechaCita && c.fechaCita.startsWith(hoy) &&
            c.estadoCita && c.estadoCita.idEstadoCita !== 7 // Todos menos Pendiente
        ).sort((a, b) => b.horaCita.localeCompare(a.horaCita)); // Más recientes primero
        
        const tbody = document.getElementById('tbody-historial-atendidas');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        
        if (citasAtendidas.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#607d8b;">No hay citas atendidas hoy</td></tr>';
            return;
        }
        
        citasAtendidas.forEach(cita => {
            const tr = document.createElement('tr');
            const pacienteNombre = `${cita.paciente.nombres} ${cita.paciente.apellidos}`;
            const estado = cita.estadoCita.nombreEstado;
            const estadoClass = estado.toLowerCase().replace(/ /g, '-');
            
            tr.innerHTML = `
                <td>${cita.horaCita || 'N/A'}</td>
                <td>${pacienteNombre}</td>
                <td>${cita.motivoConsulta || 'Sin motivo'}</td>
                <td><span class="badge-estado badge-${estadoClass}">${estado}</span></td>
                <td>
                    <button class="btn-sm btn-info" onclick="verDetalleConsulta(${cita.idCita})" title="Ver detalles">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error cargando historial de atendidas:', error);
    }
}

// ============================================
// VER DETALLE DE CONSULTA (por ID de Cita)
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
// VER DETALLE DE CONSULTA (por ID de Consulta)
// ============================================
async function verDetalleConsultaPorId(idConsulta) {
    try {
        const response = await fetch(`${API_URL}/consultas/${idConsulta}`, {credentials: 'include'});
        if (!response.ok) {
            showError('No se pudo cargar la consulta');
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
    $('#modalDetalleConsulta').modal('show');
}

// ============================================
// INICIALIZAR
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    loadCitasDelDia();
    loadHistorialCitasAtendidas();
});
