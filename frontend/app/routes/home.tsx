import "bootstrap/dist/css/bootstrap.min.css";
import "../app.css";

import { Outlet, useNavigation } from "react-router";

import { Header } from "~/components/header";
import { Footer } from "~/components/footer";

export default function Home() {
  const navigation = useNavigation();
  const isLoading = navigation.state === "loading";

  return (
    <div className="background-image">

      {isLoading && (
        <div className="page-spinner-overlay">
          <div className="dot-spinner" />
        </div>
      )}

      <Header />

      <Outlet />

      <Footer />

    </div>
  );
}