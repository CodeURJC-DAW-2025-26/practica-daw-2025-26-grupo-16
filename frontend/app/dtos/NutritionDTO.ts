import type ImageDTO from "./ImageDTO";

export default interface NutritionDTO {
  id: number;
  name: string;
  description: string;
  goal: string;
  calories: number;
  image: ImageDTO;
  userId: number;
};