import { useNavigate } from "react-router";
import type { Route } from "./+types/book-detail";
import { getBook, removeBook } from "~/services/books-service";
import {
  Alert,
  Button,
  ButtonGroup,
  Container,
  Image,
  ListGroup,
  Modal,
} from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";
import { useState } from "react";

export async function clientLoader({ params }: Route.ClientLoaderArgs) {
  return await getBook(params.id!);
}

export default function BookDetail({ loaderData }: Route.ComponentProps) {
  let { user } = useUserStore();
  const book = loaderData;
  const navigate = useNavigate();

  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [isPendingDelete, setPendingDelete] = useState(false);
  const [isDeleteDialogOpen, setDeleteDialogOpen] = useState(false);

  function handleOpenDeleteDialog() {
    setDeleteDialogOpen(true);
  }

  function handleCloseDeleteDialog() {
    if (isPendingDelete) {
      return;
    }
    setDeleteDialogOpen(false);
    setDeleteError(null);
  }

  async function handleDelete() {
    setPendingDelete(true);
    setDeleteError(null);
    try {
      await removeBook(book.id);
      navigate("/");
    } catch (err) {
      console.error(err);
      setDeleteError("Error deleting book");
      setPendingDelete(false);
    }
  }

  return (
    <>
      <Container className="mt-4 mb-5">
        <h2>Book "{book.title}"</h2>

        <Image
          src={book.image ? `/api/images/${book.image.id}/media` : `/no_image.png`}
          className="mb-4"
          alt={book.image ? "Book Image" : "No Image Available"}
          fluid
        />

        <p>{book.description}</p>

        <p>
          <b>Shops where book is available:</b>
        </p>

        <ListGroup>
          {book.shops.map((shop, index) => (
            <ListGroup.Item key={index}>{shop.name}</ListGroup.Item>
          ))}
        </ListGroup>

        {user && (
          <ButtonGroup className="mt-4">
            {user.roles.includes("ADMIN") && (
              <Button variant="danger" onClick={handleOpenDeleteDialog}>
                Remove
              </Button>
            )}
            {user.roles.includes("USER") && (
              <Button variant="warning" onClick={() => navigate(`/book/${book.id}/edit`)}>
                Edit
              </Button>
            )}
          </ButtonGroup>
        )}

        <br />
        <Button
          variant="secondary"
          className="mt-3"
          onClick={() => navigate("/")}
        >
          Back to all books
        </Button>
      </Container>

      <Modal show={isDeleteDialogOpen} onHide={handleCloseDeleteDialog}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Book</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Are you sure you want to delete <b>"{book.title}"</b>?
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
