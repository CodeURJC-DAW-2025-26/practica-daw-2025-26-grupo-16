import { useNavigate } from "react-router";
import type { Route } from "./+types/nutrition-detail";
import { getNutrition, deleteNutrition } from "~/services/nutritions-service";
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
  return await getNutrition(params.id!);
}

export default function NutritionDetail({ loaderData }: Route.ComponentProps) {
  const { user } = useUserStore();
  const nutrition = loaderData;
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
      await deleteNutrition(nutrition.id);
      navigate("/nutritions");
    } catch (err) {
      console.error(err);
      setDeleteError("Error deleting nutrition");
      setPendingDelete(false);
    }
  }

  return (
    <>
      <Container className="mt-4 mb-5">
        <h2>Nutrition "{nutrition.name}"</h2>

        <Image
          src={
            nutrition.image
              ? `/api/v1/images/${nutrition.image.id}/media`
              : `/no_image.png`
          }
          className="mb-4"
          alt={nutrition.image ? "Nutrition Image" : "No Image Available"}
          fluid
        />

        <p>{nutrition.description}</p>

        <p>
          <b>Calories:</b> {nutrition.calories}
        </p>

        <p>
          <b>Goal:</b> {nutrition.goal}
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
                onClick={() => navigate(`/nutritions/${nutrition.id}/edit`)}
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
          onClick={() => navigate("/nutritions")}
        >
          Back to all nutritions
        </Button>
      </Container>

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