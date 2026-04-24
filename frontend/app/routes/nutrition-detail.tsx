import { Link } from "react-router";
import { useNavigate } from "react-router";
import type { Route } from "./+types/nutrition-detail";
import { getNutrition, deleteNutrition } from "~/services/nutritions-service";
import {
  Alert,
  Button,
  Container,
  Image,
  Modal,
  Card,
  Row,
  Col,
  Form,
} from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";
import { useState } from "react";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  return await getNutrition(params.id!);
}

export default function NutritionDetail({ loaderData }: Route.ComponentProps) {
  const { user } = useUserStore();
  const nutrition = loaderData;
  const navigate = useNavigate();

  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [isPendingDelete, setPendingDelete] = useState(false);
  const [isDeleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const [subscribing, setSubscribing] = useState(false);

  const [subscribed, setSubscribed] = useState<boolean>(
    Boolean((nutrition as any).subscribed)
  );

  function handleOpenDeleteDialog() {
    setDeleteDialogOpen(true);
  }

  function handleCloseDeleteDialog() {
    if (isPendingDelete) return;
    setDeleteDialogOpen(false);
    setDeleteError(null);
  }

  async function handleDelete() {
    setPendingDelete(true);
    setDeleteError(null);

    try {
      await deleteNutrition(nutrition.id);
      navigate("/nutritions");
    } catch (err) {
      console.error(err);
      setDeleteError("Error deleting nutrition");
      setPendingDelete(false);
    }
  }

  async function handleSubscribe() {
    try {
      setSubscribing(true);

      await fetch(`/subscribeNutrition/${nutrition.id}`, {
        method: "POST",
        credentials: "include",
      });

      setSubscribed(true);
    } catch (err) {
      console.error(err);
    } finally {
      setSubscribing(false);
    }
  }

  async function handleUnsubscribe() {
    try {
      setSubscribing(true);

      await fetch(`/unsubscribeNutrition/${nutrition.id}`, {
        method: "POST",
        credentials: "include",
      });

      setSubscribed(false);
    } catch (err) {
      console.error(err);
    } finally {
      setSubscribing(false);
    }
  }

  return (
    <>
      <Container className="mt-5">
        <h2 className="text-center mt-5 mb-4">Nutrition Details</h2>

        <Row>
          <Col xs={12} lg={5} className="mb-4">
            <Card className="pg-card">
              <Card.Body>
                <Form.Label className="fw-bold">Nutrition Image</Form.Label>

                <Image
                  src={
                    nutrition.image
                      ? `/api/v1/images/${nutrition.image.id}/media`
                      : `/assets/images/no_image.png`
                  }
                  alt={nutrition.name}
                  className="w-100"
                  fluid
                />
              </Card.Body>
            </Card>
          </Col>

          <Col xs={12} lg={7}>
            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">Name</Form.Label>
                <p className="form-control">{nutrition.name}</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">Number of Calories</Form.Label>
                <p className="form-control">{nutrition.calories} kcal</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">Goal</Form.Label>
                <p className="form-control">{nutrition.goal}</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-4">
              <Card.Body>
                <Form.Label className="fw-bold">Meals of the Day</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={6}
                  readOnly
                  value={nutrition.description}
                />
              </Card.Body>
            </Card>

            <div className="btn-row">
              {/* PDF */}
              <Link
                to={`/nutritions/${nutrition.id}/pdf`}
                target="_blank"
                className="pg-btn btn-primary"
              >
                Download PDF
              </Link>

              {/* EDIT */}
              {user?.roles.includes("USER") && (
                <Link
                  to={`/nutritions/${nutrition.id}/edit`}
                  className="pg-btn btn-primary"
                >
                  Edit
                </Link>
              )}

              {/* SUBSCRIBE / UNSUBSCRIBE */}
              {user && (
                subscribed ? (
                  <button
                    className="pg-btn btn-primary"
                    onClick={handleUnsubscribe}
                    disabled={subscribing}
                  >
                    {subscribing ? "Processing..." : "Unsubscribe"}
                  </button>
                ) : (
                  <button
                    className="pg-btn btn-primary"
                    onClick={handleSubscribe}
                    disabled={subscribing}
                  >
                    {subscribing ? "Processing..." : "Subscribe"}
                  </button>
                )
              )}

              {/* DELETE */}
              {user?.roles.includes("ADMIN") && (
                <button
                  className="pg-btn btn-primary"
                  onClick={handleOpenDeleteDialog}
                >
                  Delete
                </button>
              )}

              {/* RETURN */}
              <Link to="/nutritions" className="pg-btn btn-primary">
                Return
              </Link>
            </div>
          </Col>
        </Row>
      </Container>

      {/* MODAL */}
      <Modal show={isDeleteDialogOpen} onHide={handleCloseDeleteDialog}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Nutrition</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>
            Are you sure you want to delete <b>"{nutrition.name}"</b>?
          </p>
          <p className="text-muted">This action cannot be undone.</p>

          {deleteError && <Alert variant="danger">{deleteError}</Alert>}
        </Modal.Body>

        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={handleCloseDeleteDialog}
            disabled={isPendingDelete}
          >
            Cancel
          </Button>

          <Button
            variant="danger"
            onClick={handleDelete}
            disabled={isPendingDelete}
          >
            {isPendingDelete ? "Deleting..." : "Delete"}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}