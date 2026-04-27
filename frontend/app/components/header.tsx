import { Link, useNavigate } from "react-router";
import { useUserGym } from "~/gyms/user-gym";

export function Header() {
  const { user, logoutUser } = useUserGym();
  const navigate = useNavigate();

  async function handleLogout() {
    await logoutUser();
    navigate("/");
  }

  return (
    <header className="header">
      <nav className="navbar navbar-expand-lg">
        <div className="container-fluid">

          <span className="logo">⚡ PowerGym</span>

          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#powergymNav"
            aria-controls="powergymNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="powergymNav">

            <div className="ms-auto d-flex flex-column flex-lg-row gap-2 mt-3 mt-lg-0">

              <Link to="/" className="pg-btn btn-primary">Home</Link>
              <Link to="/nutritions" className="pg-btn btn-primary">Nutritions</Link>
              <Link to="/trainings" className="pg-btn btn-primary">Trainings</Link>

              {!user && (
                <>
                  <Link to="/login" className="pg-btn btn-primary">Log In</Link>
                  <Link to="/register" className="pg-btn btn-primary">Register</Link>
                </>
              )}

              {user && (
                <>
                  {user.roles.includes("ADMIN") && (
                    <Link to="/admin/users" className="pg-btn btn-primary">
                      Users
                    </Link>
                  )}

                  <Link to="/progress" className="pg-btn btn-primary">Progress</Link>
                  <Link to="/profileUser" className="pg-btn btn-primary">Profile</Link>

                  <button
                    className="pg-btn btn-primary"
                    onClick={handleLogout}
                  >
                    Log Out
                  </button>
                </>
              )}

            </div>

          </div>
        </div>
      </nav>
    </header>
  );
}