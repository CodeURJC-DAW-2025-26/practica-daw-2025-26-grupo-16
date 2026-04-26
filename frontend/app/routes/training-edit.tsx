import { useNavigate } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/training-edit";
import { getTraining, updateTraining, updateTrainingImage } from "~/services/trainings-service";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  const training = await getTraining(params.id!);
  return training;
}

export default function TrainingEdit({ loaderData }: Route.ComponentProps) {
  const training = loaderData;
  const navigate = useNavigate();

  async function saveTrainingAction(prevState: { success: boolean; error: string | null } | null, formData: FormData) {
    try {
      await updateTraining(training.id, formData);

      const file = formData.get("imageField");
  
      if (file && file instanceof File && file.size > 0) {
        await updateTrainingImage(training.id, file);
      }

      navigate(`/trainings/${training.id}`);

      return { success: true, error: null };
    } catch (error) {
      console.error(error);
      return {
        success: false,
        error: "Failed to update training. Please try again.",
      };
    }
  }

  const [state, formAction, isPending] = useActionState(
    saveTrainingAction,
    null
  );

  return (
    <div className="container mt-4">
      <h2>Edit Training</h2>

      <form action={formAction}>
        <input type="hidden" name="id" defaultValue={training.id} />

        <div className="mb-3">
          <label>Name</label>
          <input
            name="name"
            className="form-control"
            defaultValue={training.name}
            required
          />
        </div>

        <div className="mb-3">
          <label>Description</label>
          <textarea
            name="description"
            className="form-control"
            defaultValue={training.description}
            required
          />
        </div>

        <div className="mb-3">
          <label>Time</label>
          <input
            type="number"
            name="time"
            className="form-control"
            defaultValue={training.time}
            required
          />
        </div>

        <div className="mb-3">
          <label>Goal</label>
          <select
            name="goal"
            className="form-control"
            defaultValue={training.goal}
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
          onClick={() => navigate(`/trainings/${training.id}`)}
        >
          Cancel
        </button>
      </form>
    </div>
  );
}