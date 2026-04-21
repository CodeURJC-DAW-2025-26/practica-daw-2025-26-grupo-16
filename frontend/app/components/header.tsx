import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import { Form, Nav, Navbar, Modal } from "react-bootstrap";
import { useActionState, useEffect, useState } from "react";
import { useUserStore } from "~/stores/user-store";

export default function Header() {
  const [isErrorLoginDialogOpen, setErrorLoginDialogOpen] = useState(false);

  function handleShowErrorLoginDialog() {
    setErrorLoginDialogOpen(true);
  }

  function handleCloseErrorLoginDialog() {
    setErrorLoginDialogOpen(false);
  }

  let { user, loginError, loadLoggedUser, loginUser, logoutUser } =
    useUserStore();

  async function loginUserAction(_prevState: void | null, formData: FormData) {
    const username = formData.get("username") as string;
    const password = formData.get("password") as string;

    await loginUser(username, password);

    const error = useUserStore.getState().loginError;

    if (error) {
      handleShowErrorLoginDialog();
    }
  }

  const [, loginFormAction, isPending] = useActionState(loginUserAction, null);

  async function logoutUserAction() {
    await logoutUser();
  }

  const [, logoutFormAction, isLoggingOut] = useActionState(
    logoutUserAction,
    null,
  );

  useEffect(() => {
    loadLoggedUser();
  }, [loadLoggedUser]);

  return (
    <>
      <Navbar expand="lg" bg="dark" data-bs-theme="dark" className="px-3">
        <Container fluid>
          <Navbar.Brand href="/">Library</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarContent" />
          <Navbar.Collapse id="navbarContent" className="justify-content-end">
            {!user && (
              <Form
                action={loginFormAction}
                className="d-flex align-items-center p-2"
              >
                <Form.Control
                  type="text"
                  name="username"
                  placeholder="Username"
                  className="me-3"
                  disabled={isPending}
                />
                <Form.Control
                  type="password"
                  name="password"
                  placeholder="Password"
                  className="me-3"
                  disabled={isPending}
                />
                <Button
                  type="submit"
                  variant="primary"
                  className="btn-nowrap w-50"
                  disabled={isPending}
                >
                  {isPending ? "Logging in..." : "Log In"}
                </Button>
              </Form>
            )}

            {user && (
              <Nav className="d-flex align-items-center">
                <Navbar.Text className="fs-3 text-white mx-3">
                  {user.name}
                </Navbar.Text>
                <Form action={logoutFormAction} className="d-inline">
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={isLoggingOut}
                  >
                    {isLoggingOut ? "Logging out..." : "Log Out"}
                  </Button>
                </Form>
              </Nav>
            )}
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <Modal show={isErrorLoginDialogOpen} onHide={handleCloseErrorLoginDialog}>
        <Modal.Header className="bg-danger text-white" closeButton>
          <Modal.Title>Login Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{loginError}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseErrorLoginDialog}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
