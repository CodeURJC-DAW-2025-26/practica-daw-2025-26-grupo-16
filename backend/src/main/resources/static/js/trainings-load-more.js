document.addEventListener('DOMContentLoaded', () => {
	const button = document.getElementById('loadMoreBtn');
	const grid = document.getElementById('trainingsGrid');

	if (!button || !grid) {
		return;
	}

	const endpoint = button.dataset.endpoint;
	const pageSize = Number(button.dataset.pageSize || 6);

	const escapeHtml = (value) => {
		const div = document.createElement('div');
		div.textContent = value ?? '';
		return div.innerHTML;
	};

	const renderCard = (item) => {
		const card = document.createElement('div');
		card.className = 'card';
		card.innerHTML = `
			<div class="card-header">${escapeHtml(item.name)}</div>
			<img src="${escapeHtml(item.imageUrl)}" class="card-img" alt="Training image" />
			<div class="card-header">
				Exercises:<br>
				${escapeHtml(item.description)}
				<div class="mt-3 text-center">
					<a href="${escapeHtml(item.detailsUrl)}" class="btn btn-primary">More info</a>
				</div>
			</div>
		`;
		return card;
	};

	const hideButton = () => {
		button.style.display = 'none';
	};

	button.addEventListener('click', async () => {
		const nextPage = Number(button.dataset.nextPage || 1);
		button.disabled = true;

		try {
			const response = await fetch(`${endpoint}?page=${nextPage}&size=${pageSize}`, {
				headers: {
					Accept: 'application/json'
				}
			});

			if (!response.ok) {
				throw new Error(`Request failed with status ${response.status}`);
			}

			const data = await response.json();
			(data.items || []).forEach((item) => {
				grid.appendChild(renderCard(item));
			});

			if (data.hasMore) {
				button.dataset.nextPage = String(data.nextPage);
				button.disabled = false;
			} else {
				hideButton();
			}
		} catch (error) {
			console.error('Error loading trainings page', error);
			button.disabled = false;
		}
	});
});
