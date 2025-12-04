document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('errorMessage');
    
    console.log('Intentando login con usuario:', username);
    
    try {
        const response = await fetch('/consultorio-medico/resources/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({
                nombreUsuario: username,
                contrasena: password
            })
        });
        
        console.log('Response status:', response.status);
        
        if (response.ok) {
            const data = await response.json();
            console.log('Login exitoso, datos:', data);
            
            // Redirigir según el rol del usuario
            let redirectUrl = 'admin/dashboard.html'; // Por defecto (Administrador)
            
            if (data.rol === 'Médico' || data.rol === 'Medico') {
                redirectUrl = 'medico.html';
            } else if (data.rol === 'Recepcionista') {
                redirectUrl = 'recepcionista.html';
            } else if (data.rol === 'Paciente') {
                redirectUrl = 'paciente.html';
            } else if (data.rol === 'Administrador') {
                redirectUrl = 'admin/dashboard.html';
            }
            
            console.log('Redirigiendo a:', redirectUrl, '(Rol:', data.rol + ')');
            window.location.href = redirectUrl;
        } else {
            const error = await response.json();
            console.error('Error de login:', error);
            errorDiv.textContent = error.error || 'Usuario o contraseña incorrectos';
            errorDiv.style.display = 'block';
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        errorDiv.textContent = 'Error de conexión con el servidor: ' + error.message;
        errorDiv.style.display = 'block';
    }
});

// ============================================
// FUNCIONALIDAD CAMBIAR CONTRASEÑA
// ============================================
document.getElementById('forgotPasswordLink').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('modalCambiarPassword').style.display = 'block';
});

function cerrarModalPassword() {
    document.getElementById('modalCambiarPassword').style.display = 'none';
    document.getElementById('formCambiarPassword').reset();
    document.getElementById('errorMessageModal').textContent = '';
    document.getElementById('successMessageModal').style.display = 'none';
}

document.getElementById('formCambiarPassword').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const cedula = document.getElementById('recuperarCedula').value.trim();
    const nombreUsuario = document.getElementById('recuperarUsuario').value.trim();
    const nuevaPassword = document.getElementById('nuevaPassword').value;
    const confirmarPassword = document.getElementById('confirmarPassword').value;
    const errorDiv = document.getElementById('errorMessageModal');
    const successDiv = document.getElementById('successMessageModal');
    
    // Limpiar mensajes
    errorDiv.textContent = '';
    successDiv.style.display = 'none';
    
    // Validaciones
    if (!/^\d{10}$/.test(cedula)) {
        errorDiv.textContent = 'La cédula debe tener 10 dígitos';
        return;
    }
    
    if (nuevaPassword.length < 6) {
        errorDiv.textContent = 'La contraseña debe tener al menos 6 caracteres';
        return;
    }
    
    if (nuevaPassword !== confirmarPassword) {
        errorDiv.textContent = 'Las contraseñas no coinciden';
        return;
    }
    
    try {
        // Buscar paciente por cédula
        const response = await fetch(`/consultorio-medico/resources/pacientes/cedula/${cedula}`, {
            credentials: 'include'
        });
        
        if (!response.ok) {
            errorDiv.textContent = 'No se encontró un paciente con esa cédula';
            return;
        }
        
        const paciente = await response.json();
        
        // Verificar que el paciente tenga usuario
        if (!paciente.usuario) {
            errorDiv.textContent = 'Este paciente no tiene usuario registrado';
            return;
        }
        
        // Verificar que el nombre de usuario coincida
        if (paciente.usuario.nombreUsuario !== nombreUsuario) {
            errorDiv.textContent = 'El nombre de usuario no coincide con la cédula proporcionada';
            return;
        }
        
        // Actualizar la contraseña
        const updateResponse = await fetch(`/consultorio-medico/resources/usuarios/${paciente.usuario.idUsuario}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({
                ...paciente.usuario,
                contrasenaHash: nuevaPassword // El backend la encriptará
            })
        });
        
        if (updateResponse.ok) {
            successDiv.textContent = '✓ Contraseña actualizada exitosamente. Ahora puedes iniciar sesión con tu nueva contraseña.';
            successDiv.style.display = 'block';
            
            // Limpiar formulario
            document.getElementById('formCambiarPassword').reset();
            
            // Cerrar modal después de 3 segundos
            setTimeout(() => {
                cerrarModalPassword();
            }, 3000);
        } else {
            errorDiv.textContent = 'Error al actualizar la contraseña';
        }
        
    } catch (error) {
        console.error('Error:', error);
        errorDiv.textContent = 'Error de conexión: ' + error.message;
    }
});

// Cerrar modal al hacer clic fuera
window.onclick = function(event) {
    const modal = document.getElementById('modalCambiarPassword');
    if (event.target === modal) {
        cerrarModalPassword();
    }
};
