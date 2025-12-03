// Cargar estadísticas al iniciar
window.addEventListener('DOMContentLoaded', () => {
    loadStats();
    loadProximasCitas();
    loadPacientesRecientes();
});

async function loadStats() {
    try {
        // Total pacientes
        const pacientesRes = await fetch(`${API_URL}/pacientes`, { credentials: 'include' });
        const pacientes = await pacientesRes.json();
        document.getElementById('totalPacientes').textContent = pacientes.length;
        
        // Total médicos activos
        const medicosRes = await fetch(`${API_URL}/medicos?activos=true`, { credentials: 'include' });
        const medicos = await medicosRes.json();
        document.getElementById('totalMedicos').textContent = medicos.length;
        
        // Total citas
        const citasRes = await fetch(`${API_URL}/citas`, { credentials: 'include' });
        const citas = await citasRes.json();
        document.getElementById('totalCitas').textContent = citas.length;
        
        // Total tratamientos activos
        const tratamientosRes = await fetch(`${API_URL}/tratamientos?activos=true`, { credentials: 'include' });
        const tratamientos = await tratamientosRes.json();
        document.getElementById('totalTratamientos').textContent = tratamientos.length;
    } catch (error) {
        console.error('Error cargando estadísticas:', error);
    }
}

async function loadProximasCitas() {
    try {
        const response = await fetch(`${API_URL}/citas`, { credentials: 'include' });
        const citas = await response.json();
        
        // Filtrar y ordenar próximas citas
        const hoy = new Date();
        const proximasCitas = citas
            .filter(c => new Date(c.fechaCita) >= hoy)
            .sort((a, b) => new Date(a.fechaCita) - new Date(b.fechaCita))
            .slice(0, 5);
        
        const container = document.getElementById('proximasCitas');
        if (proximasCitas.length === 0) {
            container.innerHTML = '<p>No hay citas próximas</p>';
            return;
        }
        
        const html = `
            <table>
                <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Hora</th>
                        <th>Paciente</th>
                        <th>Médico</th>
                    </tr>
                </thead>
                <tbody>
                    ${proximasCitas.map(cita => `
                        <tr>
                            <td>${formatDate(cita.fechaCita)}</td>
                            <td>${cita.horaCita}</td>
                            <td>${cita.paciente.nombres} ${cita.paciente.apellidos}</td>
                            <td>${cita.medico.nombres} ${cita.medico.apellidos}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
        container.innerHTML = html;
    } catch (error) {
        console.error('Error cargando citas:', error);
        document.getElementById('proximasCitas').innerHTML = '<p>Error cargando citas</p>';
    }
}

async function loadPacientesRecientes() {
    try {
        const response = await fetch(`${API_URL}/pacientes`, { credentials: 'include' });
        const pacientes = await response.json();
        
        // Ordenar por fecha de registro y tomar los 5 más recientes
        const pacientesRecientes = pacientes
            .sort((a, b) => new Date(b.fechaRegistro) - new Date(a.fechaRegistro))
            .slice(0, 5);
        
        const container = document.getElementById('pacientesRecientes');
        if (pacientesRecientes.length === 0) {
            container.innerHTML = '<p>No hay pacientes registrados</p>';
            return;
        }
        
        const html = `
            <table>
                <thead>
                    <tr>
                        <th>Cédula</th>
                        <th>Nombre</th>
                        <th>Teléfono</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    ${pacientesRecientes.map(p => `
                        <tr>
                            <td>${p.cedula}</td>
                            <td>${p.nombres} ${p.apellidos}</td>
                            <td>${p.telefono || 'N/A'}</td>
                            <td>${p.email || 'N/A'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
        container.innerHTML = html;
    } catch (error) {
        console.error('Error cargando pacientes:', error);
        document.getElementById('pacientesRecientes').innerHTML = '<p>Error cargando pacientes</p>';
    }
}
