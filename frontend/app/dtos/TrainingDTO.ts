import type ImageDTO from "./ImageDTO";

export interface TrainingDTO {
  id: number;
  name: string;
  description: string;
  goal: string;
  time: number;
  image: ImageDTO;
  userId: number;
}