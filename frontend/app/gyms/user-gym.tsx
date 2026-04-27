import { create } from "zustand";
import { persist } from "zustand/middleware";

import type UserDTO from "~/dtos/UserDTO";
import { HttpError, logIn, logOut, reqIsLogged } from "~/services/login-service";

interface UserState {
  user: UserDTO | null;
  loginError: string | null;
  loadLoggedUser: () => Promise<void>;
  loginUser: (username: string, password: string) => Promise<void>;
  logoutUser: () => Promise<void>;
}

export const useUserGym = create<UserState>()(
  persist(
    (set, get) => ({
      user: null,
      loginError: null,
      loadLoggedUser: async () => {
        set({ user: null, loginError: null });

        try {
          const user = await reqIsLogged();
          set({ user });
        } catch (error) {
          if (error instanceof HttpError && error.status === 401) {
            set({ user: null, loginError: null });
            return;
          }

          console.log(error);
          set({ loginError: "Failed to load logged-in user" });
        }
      },

      loginUser: async (username, password) => {
        set({ user: null, loginError: null });

        try {
          await logIn(username, password);
          await get().loadLoggedUser();
        } catch (error) {
          console.log(error);
          set({ loginError: "Incorrect username or password. Please try again." });
        }
      },

      logoutUser: async () => {
        set({ user: null, loginError: null });

        try {
          await logOut();
        } catch (error) {
          console.log(error);
          set({ loginError: "Logout failed. Please try again." });
        }
      },
    }),
    {
      name: "powergym-user",
    }
  )
);