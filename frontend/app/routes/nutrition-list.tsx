import { Link } from "react-router";
import type { Route } from "./+types/nutrition-list";
import { getNutritions } from "~/services/nutritions-service";
import type NutritionDTO from "~/dtos/NutritionDTO";
import { useUserStore } from "~/stores/user-store";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getNutritions();
}

export default function NutritionList({ loaderData }: Route.ComponentProps) {
  const nutritions = loaderData;
  let { user } = useUserStore();

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