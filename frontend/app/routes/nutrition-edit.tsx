import { useNavigate } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/nutrition-edit";
import { getNutrition, updateNutrition } from "~/services/nutritions-service";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  const nutrition = await getNutrition(params.id!);
  return nutrition;
}

export default function NutritionEdit({ loaderData }: Route.ComponentProps) {
  const nutrition = loaderData;
  const navigate = useNavigate();

  async function saveNutritionAction(
    prevState: { success: boolean; error: string | null } | null,
    formData: FormData
  ) {
    try {
      await updateNutrition(formData);

      navigate(`/nutritions/${nutrition.id}`);

      return { success: true, error: null };
    } catch (error) {
      console.error(error);
      return {
        success: false,
        error: "Failed to update nutrition. Please try again.",
      };
    }
  }

  const [state, formAction, isPending] = useActionState(
    saveNutritionAction,
    null
  );

  return (
    <div className="container mt-4">
      <h2>Edit Nutrition</h2>

      <form action={formAction}>
        {/* ID oculto (IMPORTANTE) */}
        <input type="hidden" name="id" defaultValue={nutrition.id} />

        <div className="mb-3">
          <label>Name</label>
          <input
            name="name"
            className="form-control"
            defaultValue={nutrition.name}
            required
          />
        </div>

        <div className="mb-3">
          <label>Description</label>
          <textarea
            name="description"
            className="form-control"
            defaultValue={nutrition.description}
            required
          />
        </div>

        <div className="mb-3">
          <label>Calories</label>
          <input
            type="number"
            name="calories"
            className="form-control"
            defaultValue={nutrition.calories}
            required
          />
        </div>

        <div className="mb-3">
          <label>Goal</label>
          <select
            name="goal"
            className="form-control"
            defaultValue={nutrition.goal}
          >
            <option value="Increase weight">Increase weight</option>
            <option value="Maintain weight">Maintain weight</option>
            <option value="Lose weight">Lose weight</option>
          </select>
        </div>

        <div className="mb-3">
          <label>Change Image</label>
          <input type="file" name="imageField" className="form-control" />
        </div>

        <div className="mb-3">
          <input type="checkbox" name="removeImage" /> Remove current image
        </div>

        {state?.error && (
          <div className="alert alert-danger">{state.error}</div>
        )}

        <button
          type="submit"
          className="btn btn-primary"
          disabled={isPending}
        >
          {isPending ? "Saving..." : "Save changes"}
        </button>

        <button
          type="button"
          className="btn btn-secondary ms-2"
          onClick={() => navigate(`/nutritions/${nutrition.id}`)}
        >
          Cancel
        </button>
      </form>
    </div>
  );
}