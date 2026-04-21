import { Form, Button, Container, Alert, Image } from "react-bootstrap";
import type BookDTO from "~/dtos/BookDTO";
import type ShopBasicDTO from "~/dtos/ShopBasicDTO";

interface BookFormProps {
  book?: Partial<BookDTO>;
  shops?: ShopBasicDTO[];
  actionState: [
    { success: boolean; error: string | null } | null,
    (formData: FormData) => void,
    boolean,
  ];
  onCancel: () => void;
}

export default function BookForm({
  book,
  shops = [],
  actionState: [state, formAction, isPending],
  onCancel,
}: BookFormProps) {
  const isEditing = book?.id;
  const selectedShopIds = book?.shops?.map((s) => s.id) || [];

  return (
    <Container className="mt-4 mb-5">
      <h2 className="mb-4">
        {isEditing ? `Edit Book "${book?.title}"` : "New Book"}
      </h2>

      {state?.error && <Alert variant="danger">{state.error}</Alert>}

      <Form action={formAction}>
        {isEditing && <input type="hidden" name="id" value={book?.id} />}

        <Form.Group className="mb-3" controlId="title">
          <Form.Label>Title</Form.Label>
          <Form.Control
            type="text"
            name="title"
            placeholder="Title"
            defaultValue={book?.title || ""}
            required
            disabled={isPending}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="description">
          <Form.Label>Abstract</Form.Label>
          <Form.Control
            as="textarea"
            name="description"
            placeholder="Description"
            rows={4}
            defaultValue={book?.description || ""}
            required
            disabled={isPending}
          />
        </Form.Group>

        {isEditing && book?.image && (
          <Form.Group className="mb-3">
            <Form.Label>Current Image:</Form.Label>
            <div className="mb-2">
              <Image
                src={`/api/images/${book.image.id}/media`}
                thumbnail
                style={{ maxHeight: "200px" }}
              />
            </div>
            <Form.Check
              type="checkbox"
              id="removeImage"
              name="removeImage"
              label="Remove image"
              disabled={isPending}
            />
          </Form.Group>
        )}

        <Form.Group className="mb-3" controlId="image">
          <Form.Label>
            {isEditing && book?.image ? "Update Image" : "Image"}
          </Form.Label>
          <Form.Control
            type="file"
            name="image"
            accept=".jpg,.jpeg"
            disabled={isPending}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="shops">
          <Form.Label>Shops</Form.Label>
          <Form.Select
            multiple
            name="shops"
            defaultValue={selectedShopIds.map(String)}
            disabled={isPending}
            style={{ height: "150px" }}
          >
            {shops.map((shop) => (
              <option key={shop.id} value={shop.id}>
                {shop.name}
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        <div className="mb-3">
          <Button
            variant="secondary"
            className="me-2"
            type="button"
            onClick={onCancel}
            disabled={isPending}
          >
            Cancel
          </Button>
          <Button type="submit" variant="primary" disabled={isPending}>
            {isPending ? "Saving..." : "Save"}
          </Button>
        </div>
      </Form>
    </Container>
  );
}
