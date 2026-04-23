import { useNavigate } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/training-new";
import { addTraining } from "~/services/trainings-service";

export default function TrainingNew({}: Route.ComponentProps) {
  const navigate = useNavigate();

  async function saveTrainingAction(
    prevState: {
      success: boolean;
      error: string | null;
    } | null,
    formData: FormData,
  ) {
    try {
      await addTraining(formData);

      navigate("/trainings");

      return { success: true, error: null };
    } catch (error) {
      console.error(error);
      return {
        success: false,
        error: "Failed to create training. Please try again.",
      };
    }
  }

  const [state, formAction, isPending] = useActionState(
    saveTrainingAction,
    null
  );

  return (
    <div className="container mt-4">
      <h2>Create Training</h2>

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
          <label>Time</label>
          <input
            type="number"
            name="time"
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
          onClick={() => navigate("/trainings")}
        >
          Cancel
        </button>
      </form>
    </div>
  );
}