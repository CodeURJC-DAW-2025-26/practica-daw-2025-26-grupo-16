import type ImageDTO from "./ImageDTO";
import type ShopBasicDTO from "./ShopBasicDTO";

export default interface BookDTO {
  id: number;
  title: string;
  description: string;
  image: ImageDTO;
  shops: ShopBasicDTO[];
}
