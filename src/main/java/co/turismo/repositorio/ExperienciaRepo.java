package co.turismo.repositorio;

import co.turismo.modelo.Experiencia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExperienciaRepo {

    private static final ExperienciaRepo INSTANCIA = new ExperienciaRepo();
    public static ExperienciaRepo get() { return INSTANCIA; }

    private final ObservableList<Experiencia> experiencias =
            FXCollections.observableArrayList();

    private ExperienciaRepo() {
        experiencias.add(new Experiencia(1,
                "Parapente en Chicamocha",
                "Vuelo en parapente sobre el cañón del Chicamocha",
                180000, 8, "2026-06-15", "Finca El Cóndor"));
        experiencias.add(new Experiencia(2,
                "Recorrido Parque del Café",
                "Tour completo por el Parque Nacional del Café en Montenegro",
                95000, 12, "2026-06-20", "Hacienda Café Verde"));
        experiencias.add(new Experiencia(3,
                "Cosecha de Café en Finca",
                "Recolección manual de café en el Eje Cafetero",
                120000, 10, "2026-06-25", "Finca La Esperanza"));
        experiencias.add(new Experiencia(4,
                "Cabalgata por Salento",
                "Recorrido a caballo por los paisajes cafeteros de Salento",
                150000, 6, "2026-07-01", "Finca Los Arrieros"));
        experiencias.add(new Experiencia(5,
                "Rafting Río La Vieja",
                "Descenso en balsa por el Río La Vieja en el Quindío",
                160000, 4, "2026-07-05", "Aventura Quindío"));
        experiencias.add(new Experiencia(6,
                "Senderismo Cocora",
                "Caminata por el Valle de Cocora entre palmas de cera",
                80000, 15, "2026-07-10", "Guías Cocora"));
        experiencias.add(new Experiencia(7,
                "Ordeño y Queso Artesanal",
                "Aprende a ordeñar y hacer queso en finca campesina",
                70000, 8, "2026-07-12", "Finca El Paraíso"));
        experiencias.add(new Experiencia(8,
                "Pesca Deportiva en Laguna",
                "Pesca deportiva en laguna natural con almuerzo típico incluido",
                110000, 10, "2026-07-15", "Finca La Laguna"));
        experiencias.add(new Experiencia(9,
                "Torrentismo Cañón del Güavio",
                "Descenso por cascadas en el cañón del Güavio",
                200000, 6, "2026-07-18", "Extremo Rural"));
        experiencias.add(new Experiencia(10,
                "Avistamiento de Aves Andinas",
                "Tour ornitológico al amanecer con guía experto en Boyacá",
                90000, 8, "2026-07-20", "Finca Las Aves"));
        experiencias.add(new Experiencia(11,
                "Taller de Cerámica Indígena",
                "Aprende técnicas ancestrales de cerámica con artesanos locales",
                85000, 12, "2026-07-22", "Comunidad Zenú"));
        experiencias.add(new Experiencia(12,
                "Noche de Fogón en Finca",
                "Velada campesina con fogón, sancocho y música llanera",
                130000, 20, "2026-07-25", "Finca El Rancho"));
    }

    public ObservableList<Experiencia> getExperienciasConCupos() {
        ObservableList<Experiencia> conCupos =
                FXCollections.observableArrayList();
        for (Experiencia e : experiencias) {
            if (e.getCuposDisponibles() > 0) conCupos.add(e);
        }
        return conCupos;
    }

    public void descontarCupos(int idExperiencia, int personas) {
        for (Experiencia e : experiencias) {
            if (e.getId() == idExperiencia) {
                e.setCuposDisponibles(e.getCuposDisponibles() - personas);
                break;
            }
        }
    }
}
