import { useNavigate, Link } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/training-new";
import { addTraining } from "~/services/trainings-service";
import { Container, Card, Form } from "react-bootstrap";

export default function TrainingNew({}: Route.ComponentProps) {
  const navigate = useNavigate();

  async function saveTrainingAction(
    prevState: {
      success: boolean;
      error: string | null;
    } | null,
    formData: FormData
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
    <Container className="mt-5 mb-5">
      <h2 className="text-center mt-5 mb-4">Create New Training</h2>

      <form action={formAction}>
        <div className="diet-container">

          <div className="diet-image">
            <Card className="pg-card mb-4">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Training Image
                </Form.Label>
                <Form.Control
                  type="file"
                  name="imageField"
                  accept=".jpg, .jpeg, .png"
                />
              </Card.Body>
            </Card>
          </div>

          <div className="diet-info">

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Training Name
                </Form.Label>
                <Form.Control
                  type="text"
                  name="name"
                  placeholder="Name of the training plan"
                  required
                />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Duration (minutes)
                </Form.Label>
                <Form.Control
                  type="number"
                  name="time"
                  min={1}
                  placeholder="Duration in minutes"
                  required
                />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Training Goal
                </Form.Label>
                <Form.Select name="goal" required>
                  <option value="">Select an option…</option>
                  <option value="Increase weight">Increase weight</option>
                  <option value="Maintain weight">Maintain weight</option>
                  <option value="Lose weight">Lose weight</option>
                </Form.Select>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Exercises of the Training
                </Form.Label>
                <Form.Control
                  as="textarea"
                  rows={6}
                  name="description"
                  placeholder="Describe the training exercises"
                  required
                />
              </Card.Body>
            </Card>

            {state?.error && (
              <div className="alert alert-danger">{state.error}</div>
            )}

            <div className="btn-row">
              <button
                type="submit"
                className="pg-btn btn-primary"
                disabled={isPending}
              >
                {isPending ? "Saving..." : "Create"}
              </button>

              <Link to="/trainings" className="pg-btn btn-primary">
                Cancel
              </Link>
            </div>

          </div>
        </div>
      </form>
    </Container>
  );
}