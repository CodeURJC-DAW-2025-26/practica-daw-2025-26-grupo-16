import { useEffect, useState } from "react";
import { Container, Card, Form, Button, Image, Alert } from "react-bootstrap";
import { useNavigate } from "react-router";
import type TrainingDTO from "~/dtos/TrainingDTO";
import type NutritionDTO from "~/dtos/NutritionDTO";

interface ProfileData {
  id: number;
  name: string;
  email: string;
  image?: { id: number } | null;
}

export async function clientLoader() {
  const userRes = await fetch("/api/v1/users/me", { credentials: "include" });
  if (!userRes.ok) throw new Error("Failed to load user profile");

  const userData: ProfileData = await userRes.json();

  return {
    user: userData,
    profileImage: userData.image,
  };
}

export default function ProfileUser({
  loaderData,
}: {
  loaderData: Awaited<ReturnType<typeof clientLoader>>;
}) {
  const navigate = useNavigate();
  const [name, setName] = useState(loaderData.user?.name || "");
  const [email, setEmail] = useState(loaderData.user?.email || "");
  const [originalEmail, setOriginalEmail] = useState(loaderData.user?.email || "");
  const [image, setImage] = useState<File | null>(null);
  const [profileImage, setProfileImage] = useState(loaderData.profileImage);
  const [imageVersion, setImageVersion] = useState<number>(Date.now());

  const [subscribedTrainings, setSubscribedTrainings] = useState<TrainingDTO[]>([]);
  const [subscribedNutritions, setSubscribedNutritions] = useState<NutritionDTO[]>([]);

  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

    async function reloadData() {
    try {
        const trainingsRes = await fetch("/api/v1/users/me/trainings", {
        credentials: "include",
        });

        const nutritionsRes = await fetch("/api/v1/users/me/nutritions", {
        credentials: "include",
        });

        const trainings = trainingsRes.ok ? await trainingsRes.json() : [];
        const nutritions = nutritionsRes.ok ? await nutritionsRes.json() : [];


        setSubscribedTrainings(trainings);
        setSubscribedNutritions(nutritions);

    } catch (err) {
        console.error(err);
    }
    }

  useEffect(() => {
    reloadData();
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    setIsSubmitting(true);

    const formData = new FormData();
    formData.append("name", name);
    formData.append("email", email);

    if (image) {
      formData.append("imageFile", image);
    }

    try {
      const res = await fetch("/api/v1/users/me", {
        method: "PUT",
        credentials: "include",
        body: formData,
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Failed to update profile: ${res.status} - ${errorText}`);
      }

      const updatedUser = await res.json();
      console.log("Updated user:", updatedUser);

      if (email !== originalEmail) {
        window.location.href = `/login?email=${encodeURIComponent(email)}`;
        return;
      }

      setName(updatedUser.name || name);
      setEmail(updatedUser.email || email);

      if (updatedUser.image && updatedUser.image.id) {
        setProfileImage(updatedUser.image);
        setImageVersion(Date.now());
      } else if (image) {
        setImageVersion(Date.now());
      }
      
      setImage(null); 
      setSuccess("Profile updated successfully");
    } catch (err) {
      console.error(err);
      setError("Error updating profile");
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleUnsubscribeTraining(id: number) {
    try {
      await fetch(`/api/v1/trainings/${id}/subscribe`, {
        method: "DELETE",
        credentials: "include",
      });

      setSubscribedTrainings((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      console.error(err);
      setError("Error unsubscribing from training");
    }
  }

  async function handleUnsubscribeNutrition(id: number) {
    try {
      await fetch(`/api/v1/nutritions/${id}/subscribe`, {
        method: "DELETE",
        credentials: "include",
      });

      setSubscribedNutritions((prev) => prev.filter((n) => n.id !== id));
    } catch (err) {
      console.error(err);
      setError("Error unsubscribing from nutrition");
    }
  }

  return (
    <Container className="mt-5">
      <div className="row justify-content-center">
        <div className="col-12 col-md-8 col-lg-6">

          <h2 className="text-center mt-5 mb-4">Profile User</h2>

          <form onSubmit={handleSubmit} encType="multipart/form-data">

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            <Card className="pg-card mb-3">
              <Card.Body className="text-center">

                <label className="fw-bold mb-3 d-block">Profile Image</label>

                <div className="mb-3">
                  <Image
                    src={
                      profileImage
                        ? `/images/${profileImage.id}?v=${imageVersion}`
                        : "/assets/images/default_user.png"
                    }
                    style={{ width: 150, height: 150 }}
                    key={imageVersion}
                  />
                </div>

                <Form.Control
                  type="file"
                  onChange={(e: any) =>
                    setImage(e.target.files?.[0] || null)
                  }
                />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label>Name</Form.Label>
                <Form.Control value={name} onChange={(e) => setName(e.target.value)} />
              </Card.Body>
            </Card>

            <Card className="pg-card mb-3">
              <Card.Body>
                <Form.Label>Email</Form.Label>
                <Form.Control value={email} onChange={(e) => setEmail(e.target.value)} />
              </Card.Body>
            </Card>

            <div className="text-center">
              <button className="pg-btn btn-primary" disabled={isSubmitting}>
                {isSubmitting ? "Saving..." : "Save"}
              </button>
            </div>
          </form>

          <Card className="pg-card mt-4">
            <Card.Body>
              <b>Trainings Subscribed</b>

              {subscribedTrainings.length > 0 ? (
                subscribedTrainings.map((t) => (
                  <Card key={t.id} className="mb-2">
                    <Card.Body>
                      {t.name}
                      <Button size="sm" onClick={() => handleUnsubscribeTraining(t.id)}>
                        Unsubscribe
                      </Button>
                    </Card.Body>
                  </Card>
                ))
              ) : (
                <p>No trainings</p>
              )}
            </Card.Body>
          </Card>

          <Card className="pg-card mt-4">
            <Card.Body>
              <b>Nutrition Subscribed</b>

              {subscribedNutritions.length > 0 ? (
                subscribedNutritions.map((n) => (
                  <Card key={n.id} className="mb-2">
                    <Card.Body>
                      {n.name}
                      <Button size="sm" onClick={() => handleUnsubscribeNutrition(n.id)}>
                        Unsubscribe
                      </Button>
                    </Card.Body>
                  </Card>
                ))
              ) : (
                <p>No nutritions</p>
              )}
            </Card.Body>
          </Card>

        </div>
      </div>
    </Container>
  );
}