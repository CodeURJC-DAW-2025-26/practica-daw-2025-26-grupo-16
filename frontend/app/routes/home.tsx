import "bootstrap/dist/css/bootstrap.min.css";
import "../app.css";

import { Outlet, useNavigation } from "react-router";
import { Container } from "react-bootstrap";

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

      <main className="container-center">
        <section className="pg-card index-card">
          <h1 className="background-title">⚡ POWERGYM MÓSTOLES</h1>

          <div className="grid grid-3 index-info">
            <div>
              <p>📍 Av. del Alcalde de Móstoles, Móstoles, Madrid</p>
              <p>📞 91 665 50 60</p>
              <p>📧 mostoles@powergym.es</p>
            </div>

            <div>
              <p>🏋️‍♂️ Gimnasio: 6:00h - 1:00h</p>
              <p>🛎️ Recepción: 8:00 - 22:00h</p>
            </div>
          </div>
        </section>
      </main>

      <Footer />

    </div>
  );
}