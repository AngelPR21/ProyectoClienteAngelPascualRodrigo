package es.ieslavereda.baseoficios.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.ieslavereda.baseoficios.API.Connector;
import es.ieslavereda.baseoficios.AdaptadorRV;
import es.ieslavereda.baseoficios.R;
import es.ieslavereda.baseoficios.activities.model.Usuario;
import es.ieslavereda.baseoficios.base.BaseActivity;
import es.ieslavereda.baseoficios.base.CallInterface;

/*
 * Clase MainActivity que actúa como pantalla principal de la aplicación.
 * Muestra una lista de usuarios obtenida desde una API y permite añadir, editar y eliminar usuarios.
 * Utiliza un RecyclerView para mostrar los datos y una FloatingActionButton para añadir nuevos elementos.
 * Implementa CallInterface para manejar llamadas a la API
 * Hace uso de un ActivityResultLauncher para recibir datos de una actividad
 *
 */
public class MainActivity extends BaseActivity implements CallInterface<List<Usuario>>, View.OnClickListener {

   //Lista de usuarios que se rellenara con datos de la API.
    private List<Usuario> usuarios;


     //RecyclerView que muestra la lista de usuarios.
    private RecyclerView recycled;


    //Botón flotante para añadir un nuevo usuario.
    private FloatingActionButton anyadir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        anyadir = findViewById(R.id.anyadir);
        recycled = findViewById(R.id.recyclerView);



        ItemTouchHelper ith = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

                    //MEtodo que cuando se mueve un viewHolder se elimina de la posicion inicial y se pone en la posicion Final, es decir se mueve de posicion
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    //Metodo que cuando se desliza un viewHolder a la izquierda se elimina
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        Usuario usuario = usuarios.get(viewHolder.getAdapterPosition());
                        int position = viewHolder.getAdapterPosition();

                        executeCall(new CallInterface<Usuario>() {

                            // Eliminar el usuario en segundo plano (BBDD)
                            @Override
                            public Usuario doInBackground() throws Exception {
                                return Connector.getConector().delete(Usuario.class, "/apiProyecto/usuarios/" + usuarios.get(position).getIdUsuario());
                            }

                            // Eliminar el usuario de la lista y notifica al adaptador para que se elimine del RecyclerView
                            @Override
                            public void doInUI(Usuario data) {
                                usuarios.remove(position);
                                recycled.getAdapter().notifyItemRemoved(position);

                                // Mostrar Snackbar con opción de deshacer
                                Snackbar.make(recycled, "Usuario eliminado: " + usuario.getNombre() + " " + usuario.getApellidos(), Snackbar.LENGTH_LONG)
                                        .setAction("Deshacer", v -> {
                                            executeCall(new CallInterface<Usuario>() {
                                                @Override
                                                public Usuario doInBackground() throws Exception {
                                                    return Connector.getConector().post(Usuario.class, usuario, "/apiProyecto/usuarios");
                                                }

                                                @Override
                                                public void doInUI(Usuario data) {
                                                    usuarios.add(position, usuario);
                                                    recycled.getAdapter().notifyItemInserted(position);
                                                }
                                            });
                                        }).show();
                            }
                        });
                    }
                });
        // Se conecta el ItemTouchHelper al RecyclerView
        ith.attachToRecyclerView(recycled);

        /*
         * Establece un onClickListener sobre un boton
         * Al hacer clic, se lanza un intent a la clase Infoactivity para crear un usuario
         */
        anyadir.setOnClickListener(o -> {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            activityResultLauncher.launch(intent);
        });

        // Ejecutamos una llamada para obtener datos de la API
        executeCall(this);
    }

    /*
     * Manejador para recibir resultados desde InfoActivity.
     * Actualiza o añade usuarios dependiendo de los datos devueltos.
     */
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String nombre = data.getStringExtra("nombre");
                    String apellido = data.getStringExtra("apellido");
                    int oficio = data.getIntExtra("oficio", 0);

                    if (data.hasExtra("id")) {
                        int userId = data.getIntExtra("id", -1);
                        for (int i = 0; i < usuarios.size(); i++) {
                            if (usuarios.get(i).getIdUsuario() == userId) {
                                usuarios.set(i, new Usuario(userId, nombre, apellido, oficio));
                                break;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();

                    } else {
                        Usuario nuevoUsuario = new Usuario(nombre, apellido, oficio);
                        usuarios.add(nuevoUsuario);
                        Toast.makeText(MainActivity.this, "Usuario añadido: " + nombre + " " + apellido, Toast.LENGTH_SHORT).show();
                    }

                    // Refrescamos la lista desde el servidor
                    executeCall(this);
                } else if (result.getResultCode() == RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                }
            });

    /*
     * Ejecuta la llamada a la API en segundo plano para obtener la lista de usuarios.
     * @return Lista de usuarios obtenida desde la API.
     * @throws Exception si ocurre un error durante la llamada.
     */
    @Override
    public List<Usuario> doInBackground() throws Exception {
        return Connector.getConector().getAsList(Usuario.class, "/apiProyecto/usuarios/");
    }

    /*
     * Metodo ejecutado en la UI tras recibir los datos de la API.
     * Configura el RecyclerView y su adaptador con los datos obtenidos.
     *
     * @param data Lista de usuarios obtenida desde la API.
     */
    @Override
    public void doInUI(List<Usuario> data) {
        recycled.setLayoutManager(new LinearLayoutManager(this));
        usuarios = data;
        recycled.setAdapter(new AdaptadorRV(this, usuarios, this));
    }

    /*
     * metodo para obtener datos de un viewHolder cuando clicas sobre el
     * Lanza InfoActivity con los datos del usuario para editarlo.
     *
     * @param view Vista que ha sido clicada.
     */
    @Override
    public void onClick(View view) {
        int position = recycled.getChildAdapterPosition(view);
        Usuario usuario = usuarios.get(position);

        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        intent.putExtra("userId", usuario.getIdUsuario());
        intent.putExtra("nombre", usuario.getNombre());
        intent.putExtra("apellido", usuario.getApellidos());
        intent.putExtra("oficioId", usuario.getOficioIdOficio());

        activityResultLauncher.launch(intent);
    }
}
