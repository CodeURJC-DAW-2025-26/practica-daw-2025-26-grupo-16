const escapeHtml = (value) => {
    const div = document.createElement('div');
    div.textContent = value ?? '';
    return div.innerHTML;
};

const initLoadMore = ({ buttonId, gridId, renderCard, errorLabel }) => {
    const button = document.getElementById(buttonId);
    const grid = document.getElementById(gridId);
    const spinner = document.getElementById(`${buttonId}Spinner`);

    if (!button || !grid) return;
    if (button.dataset.initialized === 'true') return;

    button.dataset.initialized = 'true';

    const endpoint = button.dataset.endpoint;
    const pageSize = Number(button.dataset.pageSize || 6);

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
            const response = await fetch(`${endpoint}?page=${nextPage}&size=${pageSize}`, {
                headers: { Accept: 'application/json' }
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
            renderCard: (item) => {
                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <div class="card-header">${escapeHtml(item.name)}</div>
                    <img src="${escapeHtml(item.imageUrl)}" class="card-img" alt="Nutrition image" />
                    <div class="card-header">
                        Meals:<br>
                        ${escapeHtml(item.description)}
                        <br><br>
                        <div class="mt-3 text-center">
                            <a href="${escapeHtml(item.detailsUrl)}" class="btn btn-primary">More info</a>
                        </div>
                    </div>
                `;
                return card;
            }
        });
    }

    if (document.getElementById('trainingsGrid')) {
        initLoadMore({
            buttonId: 'loadMoreTrainingsBtn',
            gridId: 'trainingsGrid',
            errorLabel: 'trainings',
            renderCard: (item) => {
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
            }
        });
    }
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initLoadMorePages);
} else {
    initLoadMorePages();
}