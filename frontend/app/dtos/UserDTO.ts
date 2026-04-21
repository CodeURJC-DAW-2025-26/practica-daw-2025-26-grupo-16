import type ImageDTO from "./ImageDTO";

export default interface UserDTO {
  id: number;
  name: string;
  email: string;
  password: string;
  roles: string[];
  image: ImageDTO;
}