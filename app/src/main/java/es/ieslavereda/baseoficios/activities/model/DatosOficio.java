package es.ieslavereda.baseoficios.activities.model;

//Enum con los datos de los oficios, su id, nombre y url de la imagen

public enum DatosOficio {
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


    private final int id; //Los pongo en final para que no se puedan modificar
    private final String nombre;
    private final String imagen;

    //Constructor del Enum
    DatosOficio(int id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    //Setters y Getters de DatosOficio

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    /*
     * Busca un DatosOficio por ID.
     * @param id ID numérico del oficio.
     * @return Devuelve el Datos oficio que corresponde con el enum o null si no se encuentra
     */
    public static DatosOficio getById(int id) {
        for (DatosOficio o : values()) {
            if (o.getId() == id) return o;
        }
        // Devuelve un oficio por defecto, que tengas definido
        return null;  // O el que uses para "desconocido"
    }

}
