import { Link } from "react-router";
import type { Route } from "./+types/nutrition-list";
import { getNutritions } from "~/services/nutritions-service";
import type NutritionDTO from "~/dtos/NutritionDTO";
import { Card, Container, Row, Col, Button } from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getNutritions();
}

export default function NutritionList({ loaderData }: Route.ComponentProps) {
  const nutritions = loaderData;
  let { user } = useUserStore();

  return (
    <Container className="mt-4 mb-5">
      <h2 className="mt-4 mb-4">Nutritions</h2>

      <Row xs={1} md={3} className="g-4">
        {nutritions.map((nutrition: NutritionDTO) => (
          <Col key={nutrition.id}>
            <Card className="h-100">

              {nutrition.image && (
                <Card.Img
                  variant="top"
                  src={`/images/${nutrition.image.id}`}
                />
              )}

              <Card.Body>
                <Card.Title>
                  <Link
                    to={`/nutritions/${nutrition.id}`}
                    className="text-decoration-none text-dark"
                  >
                    {nutrition.name}
                  </Link>
                </Card.Title>

                <Card.Text>
                  {nutrition.calories} kcal
                </Card.Text>

              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {user && (
        <div className="mt-4">
          <Button as={Link as any} to="/nutrition-new" variant="primary">
            New Nutrition
          </Button>
        </div>
      )}
    </Container>
  );
}