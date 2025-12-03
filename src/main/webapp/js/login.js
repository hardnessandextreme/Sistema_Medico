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
