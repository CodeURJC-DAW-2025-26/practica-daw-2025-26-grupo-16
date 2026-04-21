import { Link, useNavigate } from "react-router";
import type { Route } from "./+types/book-list";
import { getBooks } from "~/services/books-service";
import type BookDTO from "~/dtos/BookDTO";
import { Card, Container, Row, Col, Button } from "react-bootstrap";
import { useUserStore } from "~/stores/user-store";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getBooks();
}

export default function BooksList({ loaderData }: Route.ComponentProps) {
  const books = loaderData;
  let { user } = useUserStore();

  return (
    <Container className="mt-4 mb-5">
      <h2 className="mt-4 mb-4">Books</h2>

      <Row xs={1} md={3} className="g-4">
        {books.map((book: BookDTO) => (
          <Col key={book.id}>
            <Card className="h-100">
              <Card.Body>
                <Card.Title>
                  <Link
                    to={`/book/${book.id}`}
                    className="text-decoration-none text-dark"
                  >
                    {book.title}
                  </Link>
                </Card.Title>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {user && (
        <div className="mt-4">
          <Button as={Link as any} to="/book-new" variant="primary">
            New Book
          </Button>
        </div>
      )}
    </Container>
  );
}
