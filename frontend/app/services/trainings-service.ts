import type TrainingDTO from "~/dtos/TrainingDTO";

const API_URL = "/api/v1/trainings";
const API_PUBLIC_URL = "/trainings";

export async function getTrainings(): Promise<TrainingDTO[]> {
  const res = await fetch(`${API_URL}/`);

  if (!res.ok) {
    throw new Error("Error fetching trainings");
  }

  return await res.json();
}

export async function getTraining(id: string): Promise<TrainingDTO> {
  const res = await fetch(`${API_URL}/${id}`);

  if (!res.ok) {
    throw new Error("Training not found");
  }

  return await res.json();
}

export async function addTraining(formData: FormData): Promise<void> {
  const res = await fetch("/createTraining", {
    method: "POST",
    body: formData,
  });

  if (!res.ok) {
    throw new Error("Error creating training");
  }
}

export async function updateTraining(formData: FormData): Promise<void> {
  const res = await fetch("/editTraining", {
    method: "POST",
    body: formData,
  });

  if (!res.ok) {
    throw new Error("Error updating training");
  }
}

export async function deleteTraining(id: number): Promise<void> {
  const res = await fetch(`/deleteTraining/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error deleting training");
  }
}

export async function subscribeTraining(id: number): Promise<void> {
  const res = await fetch(`/subscribeTraining/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error subscribing training");
  }
}

export async function unsubscribeTraining(id: number): Promise<void> {
  const res = await fetch(`/unsubscribeTraining/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error unsubscribing training");
  }
}

export async function downloadTrainingPdf(id: number): Promise<Blob> {
  const res = await fetch(`${API_URL}/${id}/pdf`);

  if (!res.ok) {
    throw new Error("Error downloading PDF");
  }

  return await res.blob();
}