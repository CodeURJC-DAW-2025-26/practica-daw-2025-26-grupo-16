import { Link } from "react-router";
import type { Route } from "./+types/training-list";
import { getTrainings } from "~/services/trainings-service";
import type TrainingDTO from "~/dtos/TrainingDTO";
import { Card, Container, Row, Col, Button } from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getTrainings();
}

export default function TrainingList({ loaderData }: Route.ComponentProps) {
  const trainings = loaderData;
  let { user } = useUserStore();

  return (
    <Container className="mt-4 mb-5">
      <h2 className="mt-4 mb-4">Trainings</h2>

      <Row xs={1} md={3} className="g-4">
        {trainings.map((training: TrainingDTO) => (
          <Col key={training.id}>
            <Card className="h-100">

              {training.image && (
                <Card.Img
                  variant="top"
                  src={`/images/${training.image.id}`}
                />
              )}

              <Card.Body>
                <Card.Title>
                  <Link
                    to={`/trainings/${training.id}`}
                    className="text-decoration-none text-dark"
                  >
                    {training.name}
                  </Link>
                </Card.Title>

                <Card.Text>
                  {training.time} min
                </Card.Text>

              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {user && (
        <div className="mt-4">
          <Button as={Link as any} to="/training-new" variant="primary">
            New Training
          </Button>
        </div>
      )}
    </Container>
  );
}