import { useNavigate } from "react-router";
import { useActionState } from "react";
import type { Route } from "./+types/book-new";
import BookForm from "~/components/book-form";
import { addBook, uploadBookImage } from "~/services/books-service";
import { getShops } from "~/services/shops-service";

export async function clientLoader({}: Route.ClientLoaderArgs) {
  const shops = await getShops();
  return shops;
}

export default function BookNew({ loaderData }: Route.ComponentProps) {
  const shops = loaderData;
  const navigate = useNavigate();

  async function saveBookAction(
    prevState: {
      success: boolean;
      error: string | null;
    } | null,
    formData: FormData,
  ) {
    const title = formData.get("title") as string;
    const description = formData.get("description") as string;
    const shops = formData.getAll("shops").map((id) => ({ id: Number(id) }));
    const imageFile = formData.get("image") as File | null;

    try {
      const newBook = await addBook(title, description, shops);

      if (imageFile) {
        await uploadBookImage(newBook.id, imageFile);
      }

      navigate(`/book/${newBook.id}`);
      return { success: true, error: null };
    } catch (error) {
      console.error(error);
      return {
        success: false,
        error: "Failed to save book. Please try again.",
      };
    }
  }

  const [state, formAction, isPending] = useActionState(saveBookAction, null);

  return (
    <BookForm
      shops={shops}
      actionState={[state, formAction, isPending]}
      onCancel={() => navigate("/")}
    />
  );
}
