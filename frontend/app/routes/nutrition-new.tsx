import { useNavigate, Link } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/nutrition-new";
import { addNutrition } from "~/services/nutritions-service";
import { Container, Row, Col, Card, Form } from "react-bootstrap";

export default function NutritionNew({}: Route.ComponentProps) {
  const navigate = useNavigate();

  async function saveNutritionAction(
    prevState: {
      success: boolean;
      error: string | null;
    } | null,
    formData: FormData
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
    <Container className="mt-5 nutrition-page">
      <h2 className="text-center mt-5 mb-4">Create New Nutrition</h2>

      <form action={formAction}>
        <Row>
          <Col xs={12} lg={5} className="mb-4">
            <Card className="pg-card">
              <Card.Body>
                <Form.Label className="fw-bold">Nutrition Image</Form.Label>
                <Form.Control
                  type="file"
                  name="imageField"
                  accept="image/*"
                />
              </Card.Body>
            </Card>
          </Col>

          <Col xs={12} lg={7}>
            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Nutrition Name
                </Form.Label>
                <Form.Control
                  type="text"
                  name="name"
                  placeholder="Name of the nutrition plan"
                  required
                />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Calories (kcal)
                </Form.Label>
                <Form.Control
                  type="number"
                  name="calories"
                  placeholder="Calories of the nutrition in kcal"
                  required
                />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">
                  Nutrition Goal
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
                  Meals of the Nutrition
                </Form.Label>
                <Form.Control
                  as="textarea"
                  rows={6}
                  name="description"
                  placeholder={`Examples:
Breakfast: 2 scrambled eggs + 50 g oats
Lunch: 150 g chicken + salad with oil
Snack: Plain Greek yoghurt with almonds
Dinner: 120 g salmon + sautéed vegetables`}
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

              <Link to="/nutritions" className="pg-btn btn-primary">
                Cancel
              </Link>
            </div>
          </Col>
        </Row>
      </form>
    </Container>
  );
}