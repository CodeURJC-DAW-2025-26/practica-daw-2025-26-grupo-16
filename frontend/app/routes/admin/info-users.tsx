import { Link } from "react-router";

type ImageDTO = {
  id: number;
};

type Profile = {
  name: string;
  email?: string;
  roles?: string[];
};

type Training = {
  id: number;
  name: string;
  image?: ImageDTO | null;
};

type Nutrition = {
  id: number;
  name: string;
  image?: ImageDTO | null;
};

export async function clientLoader({ params }: { params: { id: string } }) {
  const userId = params.id;

  // Fetch user data
  const userRes = await fetch(`/api/v1/users/${userId}`, {
    credentials: "include",
  });

  if (!userRes.ok) {
    throw new Error("Failed to load user");
  }

  const userData = await userRes.json();

  // Fetch user's subscribed trainings
  const trainingsRes = await fetch(`/api/v1/users/${userId}/trainings`, {
    credentials: "include",
  });
  const subscribedTrainings = trainingsRes.ok ? await trainingsRes.json() : [];

  // Fetch user's subscribed nutritions
  const nutritionsRes = await fetch(`/api/v1/users/${userId}/nutritions`, {
    credentials: "include",
  });
  const subscribedNutritions = nutritionsRes.ok ? await nutritionsRes.json() : [];

  return {
    profile: userData,
    subscribedTrainings,
    subscribedNutritions,
  };
}

export default function UsersInfo({
  loaderData,
}: {
  loaderData: Awaited<ReturnType<typeof clientLoader>>;
}) {
  const { profile, subscribedTrainings, subscribedNutritions } = loaderData;
  return (
    <main className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-12 col-md-8 col-lg-6">

          <h2 className="text-center mt-5 mb-4">User Profile</h2>

          <div className="pg-card mb-3">
            <div className="card-body">
              <label className="form-label fw-bold">Name</label>
              <p className="mb-0">
                {profile.name}
              </p>
            </div>
          </div>

          <div className="pg-card mb-3">
            <div className="card-body">
              <label className="form-label fw-bold">Username</label>
              <p className="mb-0">{profile.name}</p>
            </div>
          </div>

          <div className="pg-card mb-3">
            <div className="card-body">
              <label className="form-label fw-bold">Email</label>
              <p className="mb-0">
                {profile.email ?? "No email"}
              </p>
            </div>
          </div>

          <div className="pg-card mb-3">
            <div className="card-body">
              <label className="form-label fw-bold">Roles</label>
              <div>
                {(profile.roles ?? []).map((role: string) => (
                  <span key={role} className="badge text-bg-primary me-1">
                    {role}
                  </span>
                ))}
              </div>
            </div>
          </div>

          <div className="pg-card mb-3 mt-4">
            <div className="card-body">
              <label className="form-label fw-bold">
                Trainings Subscribed
              </label>

              {subscribedTrainings.length > 0 ? (
                subscribedTrainings.map((t: Training) => (
                  <div className="card mb-2" key={t.id}>
                    <div className="row g-0 align-items-center">

                      <div className="col-auto">
                        <img
                          src={
                            t.image
                              ? `/images/${t.image.id}`
                              : "/assets/images/no_image.png"
                          }
                          className="img-thumbnail"
                          style={{ width: 100, height: 100 }}
                          alt={t.name}
                        />
                      </div>

                      <div className="col">
                        <div className="card-body py-2">
                          <h6 className="card-title mb-1 text-white">
                            {t.name}
                          </h6>

                          <Link
                            to={`/trainings/${t.id}`}
                            className="btn btn-sm btn-primary"
                          >
                            More info
                          </Link>
                        </div>
                      </div>

                    </div>
                  </div>
                ))
              ) : (
                <p className="mb-0 text-white">
                  There are no training plans added to the profile
                </p>
              )}
            </div>
          </div>

          <div className="pg-card mb-3 mt-4">
            <div className="card-body">
              <label className="form-label fw-bold">
                Nutrition Subscribed
              </label>

              {subscribedNutritions.length > 0 ? (
                subscribedNutritions.map((n: Nutrition) => (
                  <div className="card mb-2" key={n.id}>
                    <div className="row g-0 align-items-center">

                      <div className="col-auto">
                        <img
                          src={
                            n.image
                              ? `/images/${n.image.id}`
                              : "/assets/images/no_image.png"
                          }
                          className="img-thumbnail"
                          style={{ width: 100, height: 100 }}
                          alt={n.name}
                        />
                      </div>

                      <div className="col">
                        <div className="card-body py-2">
                          <h6 className="card-title mb-1 text-white">
                            {n.name}
                          </h6>

                          <Link
                            to={`/nutritions/${n.id}`}
                            className="btn btn-sm btn-primary"
                          >
                            More info
                          </Link>
                        </div>
                      </div>

                    </div>
                  </div>
                ))
              ) : (
                <p className="mb-0 text-white">
                  There are no nutrition plans added to the profile
                </p>
              )}
            </div>
          </div>

          <div className="text-center mt-3 mb-4">
            <Link to="/admin/users" className="btn btn-primary">
              Back to users
            </Link>
          </div>

        </div>
      </div>
    </main>
  );
}