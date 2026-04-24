import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
  layout("routes/home.tsx", [

    index("routes/index.tsx"),

    route("nutritions", "routes/nutrition-list.tsx"),
    route("nutritions/:id", "routes/nutrition-detail.tsx"),
    route("nutritions/:id/edit", "routes/nutrition-edit.tsx"),
    route("nutrition-new", "routes/nutrition-new.tsx"),

    route("trainings", "routes/training-list.tsx"),
    route("trainings/:id", "routes/training-detail.tsx"),
    route("trainings/:id/edit", "routes/training-edit.tsx"),
    route("training-new", "routes/training-new.tsx"),

    route("*", "routes/not-found.tsx"),
  ]),
] satisfies RouteConfig;