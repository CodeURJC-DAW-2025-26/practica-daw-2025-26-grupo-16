import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
	layout("routes/home.tsx", [
		index("routes/book-list.tsx"),
		route("book/:id", "routes/book-detail.tsx"),
		route("book/:id/edit", "routes/book-edit.tsx"),
		route("book-new", "routes/book-new.tsx"),
		route("*", "routes/not-found.tsx")
	]),
] satisfies RouteConfig;