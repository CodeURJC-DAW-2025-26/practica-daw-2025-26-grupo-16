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

export async function addTraining(formData: FormData): Promise<any> {
  const data = {
    name: formData.get("name"),
    description: formData.get("description"),
    goal: formData.get("goal"),
    time: Number(formData.get("time")),
  };

  const res = await fetch(`${API_URL}/`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    throw new Error(await res.text());
  }

  return res.json();
}

export async function updateTraining(id: number, formData: FormData): Promise<void> {
  const existing = await getTraining(String(id));
  
    const data = {
      id,
      name: String(formData.get("name")),
      description: String(formData.get("description")),
      goal: String(formData.get("goal")),
      time: Number(formData.get("time")),
      subscribed: existing.subscribed,
    };

  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Update error:", text);
    throw new Error("Error updating training");
  }
}

export async function updateTrainingImage(id: number, file: File): Promise<void> {
  const formData = new FormData();
  formData.append("imageFile", file);

  const res = await fetch(`${API_URL}/${id}/images`, {
    method: "PUT",
    credentials: "include",
    body: formData,
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
}

export async function deleteTraining(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Delete error:", text);
    throw new Error("Error deleting training");
  }
}

export async function subscribeTraining(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}/subscribe`, {
    method: "POST",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Subscribe error:", text);
    throw new Error("Error subscribing training");
  }
}

export async function unsubscribeTraining(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}/subscribe`, {
    method: "DELETE",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Unsubscribe error:", text);
    throw new Error("Error unsubscribing training");
  }
}

export async function downloadTrainingPdf(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}/pdf`, {
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Download error:", text);
    throw new Error("Error downloading PDF");
  }

  const blob = await res.blob();

  const url = window.URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = `training-${id}.pdf`;

  document.body.appendChild(a);
  a.click();

  a.remove();
  window.URL.revokeObjectURL(url);
}