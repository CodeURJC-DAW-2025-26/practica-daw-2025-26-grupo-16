import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router";
import { Alert, Button, Container, Form } from "react-bootstrap";
import { useUserGym } from "~/gyms/user-gym";

export default function LoginPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { loginUser, loginError } = useUserGym();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setSubmitting] = useState(false);

  useEffect(() => {
    const emailParam = searchParams.get("email");
    if (emailParam) {
      setEmail(emailParam);
    }
  }, [searchParams]);

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);

    try {
      await loginUser(email, password);
      const { user } = useUserGym.getState();

      if (user) {
        navigate("/");
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <Container className="mt-4 mb-5" style={{ maxWidth: "480px" }}>
      <h2 className="mb-4">Log In</h2>

      {loginError && <Alert variant="danger">{loginError}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="loginEmail">
          <Form.Label>Email</Form.Label>
          <Form.Control
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </Form.Group>

        <Form.Group className="mb-4" controlId="loginPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </Form.Group>

        <Button type="submit" className="pg-btn btn-primary" disabled={isSubmitting}>
          {isSubmitting ? "Logging in..." : "Log In"}
        </Button>
      </Form>
    </Container>
  );
}
