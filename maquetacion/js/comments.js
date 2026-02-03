/**
 * Sistema de Comentarios con AJAX
 * FitLife - Gestión de comentarios en tiempo real
 */

// Configuración
const COMMENTS_API_URL = '/api/comments'; // URL del API (ajustar según backend)
let currentNutritionId = 1; // ID del plan nutricional actual

/**
 * Inicializa el sistema de comentarios cuando el DOM está listo
 */
document.addEventListener('DOMContentLoaded', function () {
	const commentForm = document.getElementById('commentForm');

	if (commentForm) {
		commentForm.addEventListener('submit', handleCommentSubmit);
	}

	// Cargar comentarios existentes (opcional si vienen del servidor)
	// loadComments();
});

/**
 * Maneja el envío del formulario de comentarios
 * @param {Event} e - Evento de submit del formulario
 */
function handleCommentSubmit(e) {
	e.preventDefault();

	const commentTextarea = document.getElementById('commentText');
	const commentText = commentTextarea.value.trim();

	if (!commentText) {
		showNotification('Por favor escribe un comentario', 'error');
		return;
	}

	// Crear objeto de comentario
	const commentData = {
		nutritionId: currentNutritionId,
		text: commentText,
		userId: getCurrentUserId(), // Función para obtener ID del usuario actual
		timestamp: new Date().toISOString(),
	};

	// Enviar comentario mediante AJAX
	submitComment(commentData, commentTextarea);
}

/**
 * Envía el comentario al servidor mediante AJAX
 * @param {Object} commentData - Datos del comentario
 * @param {HTMLElement} textarea - Elemento textarea para limpiar después del envío
 */
function submitComment(commentData, textarea) {
	// Mostrar indicador de carga
	showLoadingIndicator();

	// Simulación de petición AJAX (XMLHttpRequest)
	const xhr = new XMLHttpRequest();
	xhr.open('POST', COMMENTS_API_URL, true);
	xhr.setRequestHeader('Content-Type', 'application/json');

	xhr.onload = function () {
		hideLoadingIndicator();

		if (xhr.status >= 200 && xhr.status < 300) {
			// Éxito
			const response = JSON.parse(xhr.responseText);
			addCommentToDOM(response.comment);
			textarea.value = ''; // Limpiar textarea
			showNotification('Comentario publicado exitosamente', 'success');
		} else {
			// Error
			showNotification('Error al publicar el comentario', 'error');
			console.error('Error:', xhr.statusText);
		}
	};

	xhr.onerror = function () {
		hideLoadingIndicator();
		showNotification('Error de conexión', 'error');
	};

	// Enviar datos
	xhr.send(JSON.stringify(commentData));

	// ===== SIMULACIÓN PARA DEMO (Comentar cuando se tenga backend real) =====
	// Simular respuesta exitosa después de 500ms
	setTimeout(() => {
		hideLoadingIndicator();
		const mockComment = {
			id: Date.now(),
			author: getCurrentUserName(),
			text: commentData.text,
			date: 'Justo ahora',
		};
		addCommentToDOM(mockComment);
		textarea.value = '';
		showNotification('Comentario publicado exitosamente', 'success');
	}, 500);
	// ========================================================================
}

/**
 * Alternativa usando Fetch API (más moderno)
 * @param {Object} commentData - Datos del comentario
 */
function submitCommentWithFetch(commentData) {
	fetch(COMMENTS_API_URL, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			Authorization: `Bearer ${getAuthToken()}`, // Si se usa autenticación
		},
		body: JSON.stringify(commentData),
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('Error en la respuesta del servidor');
			}
			return response.json();
		})
		.then((data) => {
			addCommentToDOM(data.comment);
			showNotification('Comentario publicado exitosamente', 'success');
		})
		.catch((error) => {
			console.error('Error:', error);
			showNotification('Error al publicar el comentario', 'error');
		});
}

/**
 * Añade un comentario al DOM
 * @param {Object} comment - Objeto con datos del comentario
 */
function addCommentToDOM(comment) {
	const commentsList = document.getElementById('commentsList');

	if (!commentsList) {
		console.error('No se encontró el contenedor de comentarios');
		return;
	}

	// Crear elemento del comentario
	const commentElement = document.createElement('div');
	commentElement.className = 'comment';
	commentElement.style.animation = 'slideIn 0.3s ease-out';

	commentElement.innerHTML = `
        <div class="comment-header">
            <span class="comment-author">${escapeHtml(comment.author)}</span>
            <span class="comment-date">${escapeHtml(comment.date)}</span>
        </div>
        <div class="comment-body">
            ${escapeHtml(comment.text)}
        </div>
    `;

	// Insertar al principio de la lista
	commentsList.insertBefore(commentElement, commentsList.firstChild);

	// Animación de entrada
	setTimeout(() => {
		commentElement.style.opacity = '1';
	}, 10);
}

/**
 * Carga los comentarios desde el servidor
 */
function loadComments() {
	const xhr = new XMLHttpRequest();
	xhr.open(
		'GET',
		`${COMMENTS_API_URL}?nutritionId=${currentNutritionId}`,
		true,
	);

	xhr.onload = function () {
		if (xhr.status >= 200 && xhr.status < 300) {
			const response = JSON.parse(xhr.responseText);
			displayComments(response.comments);
		} else {
			console.error('Error al cargar comentarios');
		}
	};

	xhr.send();
}

/**
 * Muestra múltiples comentarios en el DOM
 * @param {Array} comments - Array de comentarios
 */
function displayComments(comments) {
	const commentsList = document.getElementById('commentsList');
	commentsList.innerHTML = ''; // Limpiar lista actual

	comments.forEach((comment) => {
		addCommentToDOM(comment);
	});
}

/**
 * Elimina un comentario (requiere permisos)
 * @param {number} commentId - ID del comentario a eliminar
 */
function deleteComment(commentId) {
	if (!confirm('¿Estás seguro de que quieres eliminar este comentario?')) {
		return;
	}

	fetch(`${COMMENTS_API_URL}/${commentId}`, {
		method: 'DELETE',
		headers: {
			Authorization: `Bearer ${getAuthToken()}`,
		},
	})
		.then((response) => {
			if (!response.ok) throw new Error('Error al eliminar');
			return response.json();
		})
		.then((data) => {
			// Remover del DOM
			const commentElement = document.querySelector(
				`[data-comment-id="${commentId}"]`,
			);
			if (commentElement) {
				commentElement.remove();
			}
			showNotification('Comentario eliminado', 'success');
		})
		.catch((error) => {
			console.error('Error:', error);
			showNotification('Error al eliminar el comentario', 'error');
		});
}

/**
 * Muestra una notificación temporal
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de notificación (success, error, info)
 */
function showNotification(message, type = 'info') {
	// Crear elemento de notificación
	const notification = document.createElement('div');
	notification.className = `alert alert-${type}`;
	notification.style.position = 'fixed';
	notification.style.top = '20px';
	notification.style.right = '20px';
	notification.style.zIndex = '9999';
	notification.style.minWidth = '300px';
	notification.style.animation = 'slideInRight 0.3s ease-out';
	notification.textContent = message;

	document.body.appendChild(notification);

	// Remover después de 3 segundos
	setTimeout(() => {
		notification.style.animation = 'slideOutRight 0.3s ease-out';
		setTimeout(() => {
			notification.remove();
		}, 300);
	}, 3000);
}

/**
 * Muestra indicador de carga
 */
function showLoadingIndicator() {
	const submitBtn = document.querySelector(
		'#commentForm button[type="submit"]',
	);
	if (submitBtn) {
		submitBtn.disabled = true;
		submitBtn.textContent = 'Publicando...';
	}
}

/**
 * Oculta indicador de carga
 */
function hideLoadingIndicator() {
	const submitBtn = document.querySelector(
		'#commentForm button[type="submit"]',
	);
	if (submitBtn) {
		submitBtn.disabled = false;
		submitBtn.textContent = 'Publicar Comentario';
	}
}

/**
 * Obtiene el ID del usuario actual (simulado)
 * @returns {number} ID del usuario
 */
function getCurrentUserId() {
	// En producción, esto vendría de la sesión/localStorage/cookies
	return 1;
}

/**
 * Obtiene el nombre del usuario actual (simulado)
 * @returns {string} Nombre del usuario
 */
function getCurrentUserName() {
	// En producción, esto vendría de la sesión/localStorage
	return 'Juan García';
}

/**
 * Obtiene el token de autenticación
 * @returns {string} Token de autenticación
 */
function getAuthToken() {
	// En producción, obtener de localStorage o cookies
	return localStorage.getItem('authToken') || '';
}

/**
 * Escapa caracteres HTML para prevenir XSS
 * @param {string} text - Texto a escapar
 * @returns {string} Texto escapado
 */
function escapeHtml(text) {
	const map = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		'"': '&quot;',
		"'": '&#039;',
	};
	return text.replace(/[&<>"']/g, (m) => map[m]);
}

// Añadir animaciones CSS mediante JavaScript
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    @keyframes slideInRight {
        from {
            opacity: 0;
            transform: translateX(100%);
        }
        to {
            opacity: 1;
            transform: translateX(0);
        }
    }
    
    @keyframes slideOutRight {
        from {
            opacity: 1;
            transform: translateX(0);
        }
        to {
            opacity: 0;
            transform: translateX(100%);
        }
    }
`;
document.head.appendChild(style);
