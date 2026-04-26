import { Link } from "react-router";
import type { Route } from "./+types/training-list";
import { getTrainings } from "~/services/trainings-service";
import type TrainingDTO from "~/dtos/TrainingDTO";
import { useUserStore } from "~/stores/user-store";
import { useState } from "react";

const PAGE_SIZE = 10;

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getTrainings();
}

export default function TrainingList({ loaderData }: Route.ComponentProps) {
  const [trainings, setTrainings] = useState<TrainingDTO[]>(loaderData);
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
      const nextTrainings = await getTrainings(nextPage, PAGE_SIZE);

      if (nextTrainings.length === 0) {
        setHasMore(false);
        return;
      }

      setTrainings((prev) => [...prev, ...nextTrainings]);
      setPage(nextPage);
      setHasMore(nextTrainings.length >= PAGE_SIZE);
    } catch (error) {
      console.error("Error loading more trainings", error);
    } finally {
      setIsLoadingMore(false);
    }
  }

  return (
    <main className="pg-container nutrition-page">

      <h2 className="text-center mt-4 mb-3">
        Training Plans
      </h2>

      <div className="grid grid-3">

        {trainings.map((training: TrainingDTO) => {

          return (
            <div className="pg-card" key={training.id}>

              <div className="card-header">
                {training.name}
              </div>

              {training.image?.id ? (
                <img
                  src={`/api/v1/images/${training.image.id}/media`}
                  className="card-img"
                  alt={training.name}
                />
              ) : (
                <img
                  src="/no_image.png"
                  className="card-img"
                  alt="No image"
                />
              )}

              <div className="card-header">

                Exercises:
                <br />

                {training.description}

                <div className="mt-3 text-center">

                  <Link
                    to={`/trainings/${training.id}`}
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
          <Link to="/training-new" className="pg-btn btn-primary">
            Add training
          </Link>
        </div>
      )}

    </main>
  );
}