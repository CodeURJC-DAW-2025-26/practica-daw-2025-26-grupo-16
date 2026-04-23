import { useNavigate } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/nutrition-new";
import { addNutrition } from "~/services/nutritions-service";

export default function NutritionNew({}: Route.ComponentProps) {
  const navigate = useNavigate();

  async function saveNutritionAction(
    prevState: {
      success: boolean;
      error: string | null;
    } | null,
    formData: FormData,
  ) {
    try {
      await addNutrition(formData);

      navigate("/nutritions");

      return { success: true, error: null };
    } catch (error) {
      console.error(error);
      return {
        success: false,
        error: "Failed to create nutrition. Please try again.",
      };
    }
  }

  const [state, formAction, isPending] = useActionState(
    saveNutritionAction,
    null
  );

  return (
    <div className="container mt-4">
      <h2>Create Nutrition</h2>

      <form action={formAction}>
        <div className="mb-3">
          <label>Name</label>
          <input name="name" className="form-control" required />
        </div>

        <div className="mb-3">
          <label>Description</label>
          <textarea name="description" className="form-control" required />
        </div>

        <div className="mb-3">
          <label>Calories</label>
          <input
            type="number"
            name="calories"
            className="form-control"
            required
          />
        </div>

        <div className="mb-3">
          <label>Goal</label>
          <select name="goal" className="form-control">
            <option value="Increase weight">Increase weight</option>
            <option value="Maintain weight">Maintain weight</option>
            <option value="Lose weight">Lose weight</option>
          </select>
        </div>

        <div className="mb-3">
          <label>Image</label>
          <input type="file" name="imageField" className="form-control" />
        </div>

        {state?.error && (
          <div className="alert alert-danger">{state.error}</div>
        )}

        <button
          type="submit"
          className="btn btn-primary"
          disabled={isPending}
        >
          {isPending ? "Saving..." : "Create"}
        </button>

        <button
          type="button"
          className="btn btn-secondary ms-2"
          onClick={() => navigate("/nutritions")}
        >
          Cancel
        </button>
      </form>
    </div>
  );
}