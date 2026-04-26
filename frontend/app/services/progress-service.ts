import type NutritionDTO from "~/dtos/NutritionDTO";
import type TrainingDTO from "~/dtos/TrainingDTO";

export interface ProgressSummaryDTO {
  trainingsCount: number;
  nutritionsCount: number;
  averageTrainingMinutes: number;
  averageCalories: number;
  consistency: number;
}

export interface ProgressResponseDTO {
  chartType: string;
  labels: string[];
  values: number[];
  summary: ProgressSummaryDTO;
  level: string;
  subscribedTrainings: TrainingDTO[];
  subscribedNutritions: NutritionDTO[];
  authenticated: boolean;
}

const EMPTY_PROGRESS: ProgressResponseDTO = {
  chartType: "bar",
  labels: ["Trainings", "Nutritions", "Avg Training Minutes", "Avg Calories", "Consistency"],
  values: [0, 0, 0, 0, 0],
  summary: {
    trainingsCount: 0,
    nutritionsCount: 0,
    averageTrainingMinutes: 0,
    averageCalories: 0,
    consistency: 0,
  },
  level: "Beginner",
  subscribedTrainings: [],
  subscribedNutritions: [],
  authenticated: false,
};

export async function getProgress(): Promise<ProgressResponseDTO> {
  const res = await fetch("/api/v1/progress/chart", {
    credentials: "include",
  });

  if (res.status === 401) {
    return EMPTY_PROGRESS;
  }

  if (!res.ok) {
    throw new Error("Error fetching progress");
  }

  const progress = (await res.json()) as Omit<ProgressResponseDTO, "authenticated">;

  return {
    ...progress,
    authenticated: true,
  };
}