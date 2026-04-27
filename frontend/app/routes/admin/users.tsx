import { Container, Card, Button } from "react-bootstrap";
import { Link } from "react-router";

interface UserDTO {
  id: number;
  name: string;
  email: string;
}

export async function clientLoader() {
  const res = await fetch("/api/v1/users/", {
    credentials: "include",
  });

  console.log("STATUS:", res.status);

  if (!res.ok) {
    const text = await res.text();
    console.log("ERROR:", text);

    return { users: [] };
  }

  const users: UserDTO[] = await res.json();
  return { users };
}

export default function UserManagement({
  loaderData,
}: {
  loaderData: Awaited<ReturnType<typeof clientLoader>>;
}) {
  const { users } = loaderData;

  return (
    <Container className="mt-5">
      <div className="row justify-content-center">
        <div className="col-12 col-lg-8">

          <h2 className="text-center mt-5 mb-4">User Management</h2>

          <Card className="pg-card mb-3">
            <Card.Body>
              <label className="form-label fw-bold">Users</label>

              {users.length > 0 ? (
                users.map((user) => (
                  <Card key={user.id} className="pg-card mb-2">
                    <Card.Body className="d-flex justify-content-between align-items-center">
                      
                      <div>
                        <h6 className="mb-1">{user.name}</h6>
                        <p className="mb-0">{user.email}</p>
                      </div>

                      <Link
                        to={`/admin/users/${user.id}`}
                        className="pg-btn btn-primary"
                      >
                        View profile
                      </Link>

                    </Card.Body>
                  </Card>
                ))
              ) : (
                <p className="mb-0">There are no users registered</p>
              )}

            </Card.Body>
          </Card>

        </div>
      </div>
    </Container>
  );
}