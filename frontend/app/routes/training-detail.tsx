import { useNavigate } from "react-router";
import type { Route } from "./+types/training-detail";
import { getTraining, deleteTraining } from "~/services/trainings-service";
import {
  Alert,
  Button,
  ButtonGroup,
  Container,
  Image,
  Modal,
} from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";
import { useState } from "react";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  return await getTraining(params.id!);
}

export default function TrainingDetail({ loaderData }: Route.ComponentProps) {
  const { user } = useUserStore();
  const training = loaderData;
  const navigate = useNavigate();

  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [isPendingDelete, setPendingDelete] = useState(false);
  const [isDeleteDialogOpen, setDeleteDialogOpen] = useState(false);

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

  return (
    <>
      <Container className="mt-4 mb-5">
        <h2>Training "{training.name}"</h2>

        <Image
          src={
            training.image
              ? `/api/v1/images/${training.image.id}/media`
              : `/no_image.png`
          }
          className="mb-4"
          alt={training.image ? "Training Image" : "No Image Available"}
          fluid
        />

        <p>{training.description}</p>

        <p>
          <b>Time:</b> {training.time} min
        </p>

        <p>
          <b>Goal:</b> {training.goal}
        </p>

        {user && (
          <ButtonGroup className="mt-4">
            {user.roles.includes("ADMIN") && (
              <Button variant="danger" onClick={handleOpenDeleteDialog}>
                Remove
              </Button>
            )}

            {user.roles.includes("USER") && (
              <Button
                variant="warning"
                onClick={() => navigate(`/trainings/${training.id}/edit`)}
              >
                Edit
              </Button>
            )}
          </ButtonGroup>
        )}

        <br />

        <Button
          variant="secondary"
          className="mt-3"
          onClick={() => navigate("/trainings")}
        >
          Back to all trainings
        </Button>
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