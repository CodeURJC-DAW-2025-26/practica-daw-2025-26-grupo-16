import { useUserStore } from "~/stores/user-store";

export function Header() {
  const { user } = useUserStore();

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

              <a href="/" className="pg-btn btn-primary">Home</a>
              <a href="/nutritions" className="pg-btn btn-primary">Nutritions</a>
              <a href="/trainings" className="pg-btn btn-primary">Trainings</a>

              {!user && (
                <>
                  <a href="/login" className="pg-btn btn-primary">Log In</a>
                  <a href="/register" className="pg-btn btn-primary">Register</a>
                </>
              )}

              {user && (
                <>
                  {user.roles.includes("ADMIN") && (
                    <a href="/admin/users" className="pg-btn btn-primary">Users</a>
                  )}

                  <a href="/progress" className="pg-btn btn-primary">Progress</a>
                  <a href="/profileUser" className="pg-btn btn-primary">Profile</a>

                  <form action="/logout" method="post" className="d-inline">
                    <button type="submit" className="pg-btn btn-primary">
                      Log Out
                    </button>
                  </form>
                </>
              )}

            </div>

          </div>

        </div>

      </nav>

    </header>
  );
}