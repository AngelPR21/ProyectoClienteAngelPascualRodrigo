package es.ieslavereda.baseoficios;

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
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder> {

    private List<Usuario> usuarios;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    public AdaptadorRV(Context context, List<Usuario> usuarios, View.OnClickListener onClickListener) {
        this.context = context;
        this.usuarios = usuarios;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;

    }


    public static DatosOficio getImagen(int id){

        for (DatosOficio im : DatosOficio.values()){
            if (id == im.getId()){
                return im;
            }
        }
        return null;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_layout, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        // Obtenemos el DatosOficio usando getById que no devuelve null
        DatosOficio oficio = DatosOficio.getById(usuario.getOficioIdOficio());

        // Construimos la URL completa de la imagen
        String urlImagen = "http://my-web.joaalsai.com/images/" + oficio.getImagen();

        // Cargamos la imagen con Picasso, poniendo una imagen por defecto en caso de error o placeholder
        Picasso.get()
                .load(urlImagen)
                .into(holder.foto);

        // Seteamos texto
        holder.oficio.setText(oficio.getNombre());
        holder.nombre.setText(usuario.getNombre());
        holder.apellido.setText(usuario.getApellidos());

        holder.setUsuario(usuario);
        holder.itemView.setOnClickListener(onClickListener);
    }




    @Override
    public int getItemCount() {
        return usuarios != null ? usuarios.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, apellido;
        private TextView oficio;
        private Usuario usuario;
        private ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewHolder);
            nombre = itemView.findViewById(R.id.nombreViewHolder);
            apellido = itemView.findViewById(R.id.apellidoViewHolder);
            oficio = itemView.findViewById(R.id.oficioViewHolder);


        }

        public void setUsuario(Usuario usuario){
            this.usuario = usuario;
        }


    }
}