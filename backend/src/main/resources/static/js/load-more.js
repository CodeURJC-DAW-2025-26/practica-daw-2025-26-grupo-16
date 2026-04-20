const initLoadMore = ({ buttonId, gridId, errorLabel }) => {
	const button = document.getElementById(buttonId);
	const grid = document.getElementById(gridId);
	const spinner = document.getElementById(`${buttonId}Spinner`);

	if (!button || !grid) return;
	if (button.dataset.initialized === 'true') return;

	button.dataset.initialized = 'true';

	const endpoint = button.dataset.endpoint;
	const pageSize = Number(button.dataset.pageSize || 10);

	const setLoading = (isLoading) => {
		button.disabled = isLoading;
		button.setAttribute('aria-busy', String(isLoading));

		if (spinner) {
			spinner.style.display = isLoading ? 'inline-block' : 'none';
		}
	};

	button.addEventListener('click', async () => {
		const nextPage = Number(button.dataset.nextPage || 1);
		setLoading(true);

		try {
			const response = await fetch(
				`${endpoint}?page=${nextPage}&size=${pageSize}`,
				{
					headers: { Accept: 'text/html' },
				},
			);

			if (!response.ok) {
				throw new Error(`Request failed with status ${response.status}`);
			}

			const html = await response.text();
			grid.insertAdjacentHTML('beforeend', html);

			const hasMore = response.headers.get('X-Has-More') === 'true';
			const nextResponsePage = Number(response.headers.get('X-Next-Page'));

			if (hasMore) {
				button.dataset.nextPage = Number.isFinite(nextResponsePage)
					? String(nextResponsePage)
					: String(nextPage + 1);
				setLoading(false);
			} else {
				setLoading(false);
				button.style.display = 'none';
			}
		} catch (error) {
			console.error(`Error loading ${errorLabel} page`, error);
			setLoading(false);
		}
	});
};

const initLoadMorePages = () => {
	if (document.getElementById('nutritionsGrid')) {
		initLoadMore({
			buttonId: 'loadMoreNutritionsBtn',
			gridId: 'nutritionsGrid',
			errorLabel: 'nutritions',
		});
	}

	if (document.getElementById('trainingsGrid')) {
		initLoadMore({
			buttonId: 'loadMoreTrainingsBtn',
			gridId: 'trainingsGrid',
			errorLabel: 'trainings',
		});
	}
};

if (document.readyState === 'loading') {
	document.addEventListener('DOMContentLoaded', initLoadMorePages);
} else {
	initLoadMorePages();
}
