package es.ieslavereda.baseoficios;

import static es.ieslavereda.baseoficios.base.Parameters.URL_IMAGE_BASE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.ieslavereda.baseoficios.activities.model.DatosOficio;
import es.ieslavereda.baseoficios.activities.model.Usuario;
import es.ieslavereda.baseoficios.base.ImageDownloader;


/*
 * Adaptador para mostrar una lista de usuarios en un RecyclerView.
 * Muestra el nombre, apellido, oficio e imagen asociada al oficio.
 */
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder> {

    private List<Usuario> usuarios;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    /*
     * Constructor del adaptador.
     *
     * @param context Contexto de la aplicación o actividad.
     * @param usuarios Lista de usuarios a mostrar.
     * @param onClickListener Listener para manejar clicks en los ítems.
     */
    public AdaptadorRV(Context context, List<Usuario> usuarios, View.OnClickListener onClickListener) {
        this.context = context;
        this.usuarios = usuarios;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;

    }

    //Busca y devuelve el objeto DatosOficio que corresponde con el id proporcionado.
    public static DatosOficio getImagen(int id){

        for (DatosOficio im : DatosOficio.values()){
            if (id == im.getId()){
                return im;
            }
        }
        return null;
    }

    //Crea un nuevo ViewHolder y asigna el layout para cada ítem del RecyclerView.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_layout, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    //Asigna los datos del usuario en la posición dada al ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //OBtiene la posicion del usuario en la lista
        Usuario usuario = usuarios.get(position);

        // Obtenemos el DatosOficio usando getById
        DatosOficio oficio = DatosOficio.getById(usuario.getOficioIdOficio());

        // Construimos la URL completa de la imagen
        String urlImagen = URL_IMAGE_BASE + oficio.getImagen();
        
        // Cargamos la imagen con Picasso
        ImageDownloader.downloadImage(urlImagen,R.drawable.ic_launcher_background,holder.foto);
        
        // Seteamos textos
        holder.oficio.setText(oficio.getNombre());
        holder.nombre.setText(usuario.getNombre());
        holder.apellido.setText(usuario.getApellidos());

        holder.setUsuario(usuario);
        holder.itemView.setOnClickListener(onClickListener);
    }



    //Devuelve el número total de usuarios en la lista.
    @Override
    public int getItemCount() {
        return usuarios != null ? usuarios.size() : 0;
    }

    //Clase interna que representa el ViewHolder para cada ítem del RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, apellido;
        private TextView oficio;
        private Usuario usuario;
        private ImageView foto;

        //Constructor del ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewHolder);
            nombre = itemView.findViewById(R.id.nombreViewHolder);
            apellido = itemView.findViewById(R.id.apellidoViewHolder);
            oficio = itemView.findViewById(R.id.oficioViewHolder);


        }
        //Establece el usuario asociado a este ViewHolder.
        public void setUsuario(Usuario usuario){
            this.usuario = usuario;
        }


    }
}
