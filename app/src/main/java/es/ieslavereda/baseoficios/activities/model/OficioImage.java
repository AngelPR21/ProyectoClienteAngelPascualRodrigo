package es.ieslavereda.baseoficios.activities.model;

public enum OficioImage {
    ACTOR(1, "ACTOR", "actor.png"),
    ALBANIL(2, "ALBAÑIL", "albañil.png"),
    BANQUERO(3, "BANQUERO", "banquero.png"),
    COCINERO(4, "COCINERO", "cocinero.png"),
    ESTUDIANTE(5, "ESTUDIANTE", "estudiante.png"),
    INSTAGRAMER(6, "INSTAGRAMER", "instagramer.png"),
    PINTOR(7, "PINTOR", "pintor.png"),
    POLICIA(8, "POLICÍA", "policia.png"),
    POLITICO_ACTIVO(9, "POLÍTICO ACTIVO", "politico_activo.png"),
    POLITICO_RETIRADO(10, "POLÍTICO RETIRADO", "politico_retirado.png"),
    VENDEDOR(11, "VENDEDOR", "vendedor.png"),
    YOUTUBER(12, "YOUTUBER", "youtuber.png");

    private final int id;
    private final String nombre;
    private final String imagen;

    OficioImage(int id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}