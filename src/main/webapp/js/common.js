// Configuración de la API
const API_URL = '/consultorio-medico/resources';

// Funciones comunes
function checkSession() {
    fetch(`${API_URL}/auth/session`, {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => {
        if (!response.ok) {
            // Redirigir al login desde cualquier ubicación
            window.location.href = window.location.pathname.includes('/admin/') 
                ? '../index.html' 
                : 'index.html';
        }
        return response.json();
    })
    .then(data => {
        if (document.getElementById('userName')) {
            document.getElementById('userName').textContent = data.nombreUsuario;
        }
    })
    .catch(() => {
        // Redirigir al login desde cualquier ubicación
        window.location.href = window.location.pathname.includes('/admin/') 
            ? '../index.html' 
            : 'index.html';
    });
}

function logout() {
    fetch(`${API_URL}/auth/logout`, {
        method: 'POST',
        credentials: 'include'
    })
    .then(() => {
        // Redirigir al login desde cualquier ubicación
        window.location.href = window.location.pathname.includes('/admin/') 
            ? '../index.html' 
            : 'index.html';
    });
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
        setTimeout(() => {
            errorDiv.classList.remove('show');
        }, 5000);
    } else {
        alert(message);
    }
}

function showSuccess(message) {
    alert(message);
}

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES');
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleString('es-ES');
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
}

// Verificar sesión en cada página (excepto login)
if (window.location.pathname.indexOf('index.html') === -1 && 
    window.location.pathname !== '/' &&
    !window.location.pathname.endsWith('/')) {
    checkSession();
}
