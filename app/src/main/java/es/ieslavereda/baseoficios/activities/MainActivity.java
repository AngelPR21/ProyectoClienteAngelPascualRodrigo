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

public class MainActivity extends BaseActivity implements CallInterface<List<Usuario>>, View.OnClickListener {


    private RecyclerView recycled;

    private FloatingActionButton anyadir;

    private List<Usuario> usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        anyadir = findViewById(R.id.anyadir);
        recycled = findViewById(R.id.recyclerView);

        ItemTouchHelper ith = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        Usuario usuario = usuarios.get(viewHolder.getAdapterPosition());

                        int position = viewHolder.getAdapterPosition();

                        executeCall(new CallInterface<Usuario>() {
                            @Override
                            public Usuario doInBackground() throws Exception {
                                return Connector.getConector().delete(Usuario.class,"/apiProyecto/usuarios/"+usuarios.get(position).getIdUsuario());
                            }

                            @Override
                            public void doInUI(Usuario data) {
                                usuarios.remove(position);

                                recycled.getAdapter().notifyItemRemoved(position);

                                Snackbar.make(recycled, "Usuario eliminado: " + usuario.getNombre()+" "+usuario.getApellidos(), Snackbar.LENGTH_LONG)
                                        .setAction("Deshacer", v -> {
                                            executeCall(new CallInterface<Usuario>() {
                                                @Override
                                                public Usuario doInBackground() throws Exception {
                                                    return Connector.getConector().post(Usuario.class,usuario,"/apiProyecto/usuarios");
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
        ith.attachToRecyclerView(recycled);

        anyadir.setOnClickListener(o -> {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            activityResultLauncher.launch(intent);
        });

        // Ejecutamos una llamada para obtener datos de la API
        executeCall(this);
    }



    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();


                    String nombre = data.getStringExtra("nombre");
                    String apellido = data.getStringExtra("apellido");
                    int oficio = data.getIntExtra("oficio", 0);

                    if (data.hasExtra("id")){
                        int userId = data.getIntExtra("id", -1);
                        for (int i = 0; i < usuarios.size(); i++) {
                            if (usuarios.get(i).getIdUsuario() == userId) {
                                usuarios.set(i, new Usuario(userId, nombre, apellido, oficio));
                                recycled.getAdapter().notifyItemChanged(i); // Notificamos el cambio
                                break;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();

                    } else {
                        Usuario nuevoUsuario = new Usuario(nombre, apellido, oficio);
                        usuarios.add(nuevoUsuario);
                        recycled.getAdapter().notifyItemInserted(usuarios.size() - 1);
                        Toast.makeText(MainActivity.this, "Usuario añadido: " + nombre+ " "+ apellido, Toast.LENGTH_SHORT).show();
                    }


                } else if (result.getResultCode()==RESULT_CANCELED){
                    Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                }

            });



    // Realizamos la llamada en segundo plano y devolvemos el objeto obtenido
    @Override
    public List<Usuario> doInBackground() throws Exception {
        return Connector.getConector().getAsList(Usuario.class, "/apiProyecto/usuarios/");
    }

    @Override
    public void doInUI(List<Usuario> data) {
        recycled.setLayoutManager(new LinearLayoutManager(this));
        usuarios = data;
        recycled.setAdapter(new AdaptadorRV(this, usuarios, this));
    }

    @Override
    public void onClick(View view) {

        int position = recycled.getChildAdapterPosition(view);
        Usuario usuario = usuarios.get(position);

        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);

        intent.putExtra("userId",usuario.getIdUsuario());
        intent.putExtra("nombre",usuario.getNombre());
        intent.putExtra("apellido",usuario.getApellidos());
        intent.putExtra("oficioId",usuario.getOficioIdOficio());

        activityResultLauncher.launch(intent);

    }
}
