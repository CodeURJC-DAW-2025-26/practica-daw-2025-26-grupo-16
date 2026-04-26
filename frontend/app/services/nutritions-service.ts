import type NutritionDTO from "~/dtos/NutritionDTO";

const API_URL = "/api/v1/nutritions";
const DEFAULT_PAGE_SIZE = 10;

export async function getNutritions(page = 0, size = DEFAULT_PAGE_SIZE): Promise<NutritionDTO[]> {
  const res = await fetch(`${API_URL}/?page=${page}&size=${size}`);

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

export async function updateNutrition(id: number, formData: FormData): Promise<void> {
  const existing = await getNutrition(String(id));

  const data = {
    id,
    name: String(formData.get("name")),
    description: String(formData.get("description")),
    goal: String(formData.get("goal")),
    calories: Number(formData.get("calories")),
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
    throw new Error("Error updating nutrition");
  }
}

export async function updateNutritionImage(id: number, file: File): Promise<void> {
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

export async function deleteNutrition(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Delete error:", text);
    throw new Error("Error deleting nutrition");
  }
}

export async function subscribeNutrition(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}/subscribe`, {
    method: "POST",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Subscribe error:", text);
    throw new Error("Error subscribing nutrition");
  }
}

export async function unsubscribeNutrition(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}/subscribe`, {
    method: "DELETE",
    credentials: "include",
  });

  if (!res.ok) {
    const text = await res.text();
    console.error("Unsubscribe error:", text);
    throw new Error("Error unsubscribing nutrition");
  }
}

export async function downloadNutritionPdf(id: number): Promise<void> {
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
  a.download = `nutrition-${id}.pdf`;

  document.body.appendChild(a);
  a.click();

  a.remove();
  window.URL.revokeObjectURL(url);
}