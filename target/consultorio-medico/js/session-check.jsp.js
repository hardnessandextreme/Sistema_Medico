// Verificación de sesión para páginas JSP
const API_URL = '/consultorio-medico/resources';

async function checkSessionJSP() {
    try {
        const response = await fetch(`${API_URL}/auth/session`, {
            credentials: 'include'
        });

        if (!response.ok) {
            console.error('No hay sesión activa, redirigiendo al login');
            window.location.href = 'index.html';
            return null;
        }

        const data = await response.json();
        console.log('Sesión activa:', data);
        return data;
    } catch (error) {
        console.error('Error verificando sesión:', error);
        window.location.href = 'index.html';
        return null;
    }
}

async function logoutJSP() {
    try {
        await fetch(`${API_URL}/auth/logout`, {
            method: 'POST',
            credentials: 'include'
        });
        window.location.href = 'index.html';
    } catch (error) {
        console.error('Error al cerrar sesión:', error);
        window.location.href = 'index.html';
    }
}

// Verificar sesión al cargar la página
window.addEventListener('DOMContentLoaded', () => {
    checkSessionJSP();
});
