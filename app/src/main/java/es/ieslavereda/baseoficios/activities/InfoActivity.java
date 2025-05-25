package es.ieslavereda.baseoficios.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.squareup.picasso.Picasso;

import es.ieslavereda.baseoficios.API.Connector;
import es.ieslavereda.baseoficios.R;
import es.ieslavereda.baseoficios.AdaptadorRV;
import es.ieslavereda.baseoficios.activities.model.OficioImage;
import es.ieslavereda.baseoficios.activities.model.Usuario;
import es.ieslavereda.baseoficios.base.BaseActivity;
import es.ieslavereda.baseoficios.base.CallInterface;

public class InfoActivity extends BaseActivity{

    private ImageView imageInfo;
    private EditText nombreEdit, apellidoEdit;
    private Button guardar, cancelar;
    private Spinner oficioSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.infousuario_layout);

        guardar = findViewById(R.id.guardar);
        cancelar= findViewById(R.id.cancelar);
        imageInfo = findViewById(R.id.imageInfo);
        nombreEdit = findViewById(R.id.nombreEdit);
        apellidoEdit = findViewById(R.id.apellidoEdit);
        oficioSpinner = findViewById(R.id.oficioSpinner);

        oficioSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,OficioImage.values()));



        if (getIntent().hasExtra("oficioId")){
            guardar.setText("Actualizar");
            int idOficio = getIntent().getExtras().getInt("oficioId");

            OficioImage imagen = AdaptadorRV.getImagen(idOficio);
            String url = "http://my-web.joaalsai.com/images/" + imagen.getImagen();
            Picasso.get().load(url).into(imageInfo);


            nombreEdit.setText(getIntent().getExtras().getString("nombre"));
            apellidoEdit.setText(getIntent().getExtras().getString("apellido"));

            int i = 0;
            for (OficioImage im : OficioImage.values()){
                if (im.getId() == idOficio){
                    oficioSpinner.setSelection(i);
                }
                i++;
            }

            guardar.setOnClickListener(v -> {

                executeCall(new CallInterface<Usuario>(){
                    Usuario u = new Usuario(
                            getIntent().getExtras().getInt("userId"),
                            nombreEdit.getText().toString(),
                            apellidoEdit.getText().toString(),
                            oficioSpinner.getSelectedItemPosition()+1
                    );

                    @Override
                    public Usuario doInBackground() throws Exception {

                        return Connector.getConector().put(Usuario.class,u,"/apiProyecto/usuarios/");
                    }

                    @Override
                    public void doInUI(Usuario data) {
                        if (data != null) {
                            Intent intent = new Intent();
                            intent.putExtra("id", data.getIdUsuario());
                            intent.putExtra("nombre", data.getNombre());
                            intent.putExtra("apellido", data.getApellidos());
                            intent.putExtra("oficio", data.getOficioIdOficio());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            });

        } else {
            guardar.setText("Crear");
            guardar.setOnClickListener(v -> {

                executeCall(new CallInterface<Usuario>() {
                    @Override
                    public Usuario doInBackground() throws Exception {
                        Usuario u = new Usuario(nombreEdit.getText().toString(), apellidoEdit.getText().toString(), oficioSpinner.getSelectedItemPosition()+1);
                        return Connector.getConector().post(Usuario.class,u,"/apiProyecto/usuarios/");
                    }

                    @Override
                    public void doInUI(Usuario data) {
                        if (data != null) {
                            Intent intent = new Intent();
                            intent.putExtra("nombre", data.getNombre());
                            intent.putExtra("apellido", data.getApellidos());
                            intent.putExtra("oficio", data.getOficioIdOficio());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "El usuario no se insertó", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            });
        }


        cancelar.setOnClickListener( o -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        oficioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OficioImage seleccionado = (OficioImage) oficioSpinner.getItemAtPosition(i);
                String url = "http://my-web.joaalsai.com/images/" + seleccionado.getImagen();
                Picasso.get().load(url).into(imageInfo);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}