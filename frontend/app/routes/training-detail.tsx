import { Link, useNavigate } from "react-router";
import type { Route } from "./+types/training-detail";
import { getTraining, deleteTraining } from "~/services/trainings-service";
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
import { useRevalidator } from "react-router";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  return await getTraining(params.id!);
}

export default function TrainingDetail({ loaderData }: Route.ComponentProps) {
  const { user } = useUserStore();
  const training = loaderData;

  const navigate = useNavigate();
  const revalidator = useRevalidator();

  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [isPendingDelete, setPendingDelete] = useState(false);
  const [isDeleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [subscribing, setSubscribing] = useState(false);

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
      await deleteTraining(training.id);
      navigate("/trainings");
    } catch (err) {
      console.error(err);
      setDeleteError("Error deleting training");
      setPendingDelete(false);
    }
  }

  async function handleSubscribe() {
    try {
      setSubscribing(true);

      await fetch(`/api/v1/trainings/${training.id}/subscribe`, {
        method: "POST",
        credentials: "include",
      });

      revalidator.revalidate();
    } catch (err) {
      console.error(err);
    } finally {
      setSubscribing(false);
    }
  }

  async function handleUnsubscribe() {
    try {
      setSubscribing(true);

      await fetch(`/api/v1/trainings/${training.id}/subscribe`, {
        method: "DELETE",
        credentials: "include",
      });

      revalidator.revalidate();
    } catch (err) {
      console.error(err);
    } finally {
      setSubscribing(false);
    }
  }

  return (
    <>
      <Container className="mt-5">
        <h2 className="text-center mt-5 mb-4">Training Details</h2>

        <Row>
          <Col xs={12} lg={5} className="mb-4">
            <Card className="pg-card">
              <Card.Body>
                <Form.Label className="fw-bold">Training Image</Form.Label>

                <Image
                  src={
                    training.image
                      ? `/api/v1/images/${training.image.id}/media`
                      : `/assets/images/no_image.png`
                  }
                  alt={training.name}
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
                <p className="form-control">{training.name}</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">Duration</Form.Label>
                <p className="form-control">{training.time} minutes</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label className="fw-bold">Goal</Form.Label>
                <p className="form-control">{training.goal}</p>
              </Card.Body>
            </Card>

            <Card className="pg-card mb-4">
              <Card.Body>
                <Form.Label className="fw-bold">Exercises</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={6}
                  readOnly
                  value={training.description}
                />
              </Card.Body>
            </Card>

            <div className="btn-row">
              <Link
                to={`/trainings/${training.id}/pdf`}
                target="_blank"
                className="pg-btn btn-primary"
              >
                Download PDF
              </Link>

              {(user?.roles.includes("ROLE_USER") || user?.roles.includes("ROLE_ADMIN")) && (
                <Link
                  to={`/trainings/${training.id}/edit`}
                  className="pg-btn btn-primary"
                >
                  Edit
                </Link>
              )}

              {user &&
                (training.subscribed ? (
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
                ))}

              {user?.roles.includes("ROLE_ADMIN") && (
                <button
                  className="pg-btn btn-primary"
                  onClick={handleOpenDeleteDialog}
                >
                  Delete
                </button>
              )}

              <Link to="/trainings" className="pg-btn btn-primary">
                Return
              </Link>
            </div>
          </Col>
        </Row>
      </Container>

      <Modal show={isDeleteDialogOpen} onHide={handleCloseDeleteDialog}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Training</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>
            Are you sure you want to delete <b>"{training.name}"</b>?
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