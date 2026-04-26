import { Link } from "react-router";
import type { Route } from "./+types/progress";
import { Alert, Card, Col, Container, Row } from "react-bootstrap";
import { getProgress } from "~/services/progress-service";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  return await getProgress();
}

function getLevel(consistency: number) {
  if (consistency < 35) {
    return "Beginner";
  }

  if (consistency < 70) {
    return "Intermediate";
  }

  return "Advanced";
}

export default function ProgressPage({ loaderData }: Route.ComponentProps) {
  const progress = loaderData;
  const trainingsCount = progress.summary.trainingsCount;
  const nutritionsCount = progress.summary.nutritionsCount;
  const consistency = progress.summary.consistency;
  const level = progress.level || getLevel(consistency);

  const sections = [
    {
      label: "Trainings",
      value: trainingsCount,
      toneClass: "progress-tone-red",
      description: "Subscribed training plans",
    },
    {
      label: "Nutritions",
      value: nutritionsCount,
      toneClass: "progress-tone-orange",
      description: "Subscribed nutrition plans",
    },
    {
      label: "Avg Training",
      value: `${progress.summary.averageTrainingMinutes} min`,
      toneClass: "progress-tone-amber",
      description: "Average training duration",
    },
    {
      label: "Avg Calories",
      value: `${progress.summary.averageCalories} kcal`,
      toneClass: "progress-tone-gold",
      description: "Average nutrition calories",
    },
  ];

  if (!progress.authenticated) {
    return (
      <main className="pg-container mt-5 mb-5">
        <Container className="py-4">
          <Alert variant="warning" className="mb-0">
            Log in to see your subscriptions, progress chart, and personalized stats.
            <div className="mt-3">
              <Link to="/login" className="pg-btn btn-primary">
                Go to Log In
              </Link>
            </div>
          </Alert>
        </Container>
      </main>
    );
  }

  return (
    <main className="pg-container mt-5 mb-5">
      <Container fluid className="px-0">
        <div className="progress-hero mb-4">
          <div>
            <p className="progress-eyebrow mb-2">Personal dashboard</p>
            <h2 className="mb-2">My Progress</h2>
            <p className="mb-0">
              Track the trainings and nutritions you are subscribed to in one place.
            </p>
          </div>

          <div className="progress-level-pill">
            <span className="small text-uppercase d-block">Current level</span>
            <strong>{level}</strong>
          </div>
        </div>

        <Row className="g-3 mb-4">
          {sections.map((section) => (
            <Col xs={12} md={6} lg={3} key={section.label}>
              <Card className="pg-card h-100 progress-stat-card">
                <Card.Body>
                  <div className={`progress-stat-dot ${section.toneClass}`} />
                  <div className="stat-value">{section.value}</div>
                  <div className="stat-label mb-2">{section.label}</div>
                  <p className="small text-muted mb-0">{section.description}</p>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>

        <Card className="pg-card mb-4">
          <Card.Header className="card-header">Consistency</Card.Header>
          <Card.Body>
            <div className="d-flex justify-content-between align-items-center mb-2">
              <span>Current level: {level}</span>
              <span>{consistency}%</span>
            </div>
            <div className="progress">
              <div
                className="progress-bar"
                role="progressbar"
                style={{ width: `${consistency}%` }}
                aria-valuenow={consistency}
                aria-valuemin={0}
                aria-valuemax={100}
              >
                {consistency}%
              </div>
            </div>
          </Card.Body>
        </Card>

        <Row className="g-3">
          <Col xs={12} lg={6}>
            <Card className="pg-card h-100">
              <Card.Header className="card-header">Subscribed Trainings</Card.Header>
              <Card.Body>
                {progress.subscribedTrainings.length > 0 ? (
                  <div className="grid grid-1 gap-3">
                    {progress.subscribedTrainings.map((training) => (
                      <article className="progress-list-item" key={training.id}>
                        <div className="progress-list-media">
                          <img
                            src={
                              training.image?.id
                                ? `/api/v1/images/${training.image.id}/media`
                                : "/no_image.png"
                            }
                            alt={training.name}
                          />
                        </div>
                        <div className="progress-list-content">
                          <div className="d-flex justify-content-between gap-3 align-items-start">
                            <div>
                              <h3 className="progress-item-title mb-1">{training.name}</h3>
                              <p className="mb-1">{training.goal}</p>
                            </div>
                            <span className="progress-item-badge">{training.time} min</span>
                          </div>
                          <p className="progress-item-description mb-0">{training.description}</p>
                        </div>
                      </article>
                    ))}
                  </div>
                ) : (
                  <p className="text-muted mb-0">You have no subscribed trainings yet.</p>
                )}
              </Card.Body>
            </Card>
          </Col>

          <Col xs={12} lg={6}>
            <Card className="pg-card h-100">
              <Card.Header className="card-header">Subscribed Nutritions</Card.Header>
              <Card.Body>
                {progress.subscribedNutritions.length > 0 ? (
                  <div className="grid grid-1 gap-3">
                    {progress.subscribedNutritions.map((nutrition) => (
                      <article className="progress-list-item" key={nutrition.id}>
                        <div className="progress-list-media">
                          <img
                            src={
                              nutrition.image?.id
                                ? `/api/v1/images/${nutrition.image.id}/media`
                                : "/no_image.png"
                            }
                            alt={nutrition.name}
                          />
                        </div>
                        <div className="progress-list-content">
                          <div className="d-flex justify-content-between gap-3 align-items-start">
                            <div>
                              <h3 className="progress-item-title mb-1">{nutrition.name}</h3>
                              <p className="mb-1">{nutrition.goal}</p>
                            </div>
                            <span className="progress-item-badge">{nutrition.calories} kcal</span>
                          </div>
                          <p className="progress-item-description mb-0">{nutrition.description}</p>
                        </div>
                      </article>
                    ))}
                  </div>
                ) : (
                  <p className="text-muted mb-0">You have no subscribed nutritions yet.</p>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </main>
  );
}