import type ImageDTO from "./ImageDTO";

export interface UserDTO {
  id: number;
  name: string;
  email: string;
  password: string;
  roles: string[];
  image: ImageDTO;
}