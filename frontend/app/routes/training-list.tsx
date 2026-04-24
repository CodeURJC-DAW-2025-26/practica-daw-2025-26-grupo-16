import { Link } from "react-router";
import type { Route } from "./+types/training-list";
import { getTrainings } from "~/services/trainings-service";
import type TrainingDTO from "~/dtos/TrainingDTO";
import { useUserStore } from "~/stores/user-store";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getTrainings();
}

export default function TrainingList({ loaderData }: Route.ComponentProps) {
  const trainings = loaderData;
  let { user } = useUserStore();

  return (
    <main className="pg-container nutrition-page">

      <h2 className="text-center mt-4 mb-3">
        Training Plans
      </h2>

      <div className="grid grid-3">

        {trainings.map((training: TrainingDTO) => {

          console.log("Training:", training);
          console.log("Training image:", training.image);

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

                Meals:
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

      {user && (
        <div className="btn-row" style={{ justifyContent: "center", marginTop: "2rem" }}>
          <Link to="/trainings-new" className="pg-btn btn-primary">
            Add training
          </Link>
        </div>
      )}

    </main>
  );
}