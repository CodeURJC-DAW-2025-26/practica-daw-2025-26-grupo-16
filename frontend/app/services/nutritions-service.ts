import type NutritionDTO from "~/dtos/NutritionDTO";

const API_URL = "/api/v1/nutritions";
const API_IMAGES_URL = "/api/images";

export async function getNutritions(): Promise<NutritionDTO[]> {
  const res = await fetch(`${API_URL}/`);

  if (!res.ok) {
    throw new Error("Error fetching nutritions");
  }

  return await res.json();
}

export async function getNutrition(id: string): Promise<NutritionDTO> {
  const res = await fetch(`${API_URL}/${id}`);

  if (!res.ok) {
    throw new Error("Nutrition not found");
  }

  return await res.json();
}

export async function addNutrition(formData: FormData): Promise<any> {
  const data = {
    name: formData.get("name"),
    description: formData.get("description"),
    goal: formData.get("goal"),
    calories: Number(formData.get("calories")),
  };

  const res = await fetch("/api/v1/nutritions/", {
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

export async function updateNutrition(formData: FormData): Promise<void> {
  const res = await fetch("/editNutrition", {
    method: "POST",
    body: formData,
  });

  if (!res.ok) {
    throw new Error("Error updating nutrition");
  }
}

export async function deleteNutrition(id: number): Promise<void> {
  const res = await fetch(`/deleteNutrition/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error deleting nutrition");
  }
}

export async function subscribeNutrition(id: number): Promise<void> {
  const res = await fetch(`/subscribeNutrition/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error subscribing nutrition");
  }
}

export async function unsubscribeNutrition(id: number): Promise<void> {
  const res = await fetch(`/unsubscribeNutrition/${id}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw new Error("Error unsubscribing nutrition");
  }
}

export async function downloadNutritionPdf(id: number): Promise<Blob> {
  const res = await fetch(`${API_URL}/${id}/pdf`);

  if (!res.ok) {
    throw new Error("Error downloading PDF");
  }

  return await res.blob();
}