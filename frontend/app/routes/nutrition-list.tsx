import { Link } from "react-router";
import type { Route } from "./+types/nutrition-list";
import { getNutritions } from "~/services/nutritions-service";
import type NutritionDTO from "~/dtos/NutritionDTO";
import { useUserStore } from "~/stores/user-store";
import { useState } from "react";

const PAGE_SIZE = 10;

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getNutritions();
}

export default function NutritionList({ loaderData }: Route.ComponentProps) {
  const [nutritions, setNutritions] = useState<NutritionDTO[]>(loaderData);
  const [page, setPage] = useState(0);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [hasMore, setHasMore] = useState(loaderData.length >= PAGE_SIZE);
  let { user } = useUserStore();

  async function handleLoadMore() {
    if (isLoadingMore || !hasMore) {
      return;
    }

    setIsLoadingMore(true);

    try {
      const nextPage = page + 1;
      const nextNutritions = await getNutritions(nextPage, PAGE_SIZE);

      if (nextNutritions.length === 0) {
        setHasMore(false);
        return;
      }

      setNutritions((prev) => [...prev, ...nextNutritions]);
      setPage(nextPage);
      setHasMore(nextNutritions.length >= PAGE_SIZE);
    } catch (error) {
      console.error("Error loading more nutritions", error);
    } finally {
      setIsLoadingMore(false);
    }
  }

  return (
    <main className="pg-container nutrition-page">

      <h2 className="text-center mt-4 mb-3">
        Nutrition Plans
      </h2>

      <div className="grid grid-3">

        {nutritions.map((nutrition: NutritionDTO) => {

          return (
            <div className="pg-card" key={nutrition.id}>

              <div className="card-header">
                {nutrition.name}
              </div>

              {nutrition.image?.id ? (
                <img
                  src={`/api/v1/images/${nutrition.image.id}/media`}
                  className="card-img"
                  alt={nutrition.name}
                />
              ) : (
                <img
                  src="/no_image.png"
                  className="card-img"
                  alt="No image"
                />
              )}

              <div className="card-header">

                Meals:
                <br />

                {nutrition.description}

                <div className="mt-3 text-center">

                  <Link
                    to={`/nutritions/${nutrition.id}`}
                    className="pg-btn btn-primary"
                  >
                    More info
                  </Link>

                </div>

              </div>

            </div>
          );
        })}

      </div>

      {hasMore && (
        <div className="btn-row" style={{ justifyContent: "center", marginTop: "1rem" }}>
          <button className="pg-btn btn-primary" onClick={handleLoadMore} disabled={isLoadingMore}>
            {isLoadingMore ? "Loading..." : "Load more"}
          </button>
        </div>
      )}

      {user && (
        <div className="btn-row" style={{ justifyContent: "center", marginTop: "2rem" }}>
          <Link to="/nutrition-new" className="pg-btn btn-primary">
            Add nutrition
          </Link>
        </div>
      )}

    </main>
  );
}