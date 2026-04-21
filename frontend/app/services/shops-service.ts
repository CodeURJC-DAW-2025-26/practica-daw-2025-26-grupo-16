import type ShopBasicDTO from "~/dtos/ShopBasicDTO";

const API_URL = "/api/shops";

export async function getShops(): Promise<ShopBasicDTO[]> {
  const res = await fetch(`${API_URL}/`);
  return await res.json();
}
