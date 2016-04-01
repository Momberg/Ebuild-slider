package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    int ID_Lista = -1;
    boolean selecionado = false;
    ListView lista;
    List<Obra> obras;
    ObraService service = new ObraService();
    ObraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        try {
            obras = service.getObras(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new ObraAdapter(this, obras);
        lista = (ListView) findViewById(R.id.lista_edita_obra);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Obra o = obras.get(arg2);
                ID_Lista = o.getID();
                selecionado = true;
            }
        });

        Button remove_obra = (Button) findViewById(R.id.exclui);
        remove_obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID_Lista != -1) {
                    ObrasDB db = new ObrasDB(getApplicationContext());
                    db.delete(ID_Lista);
                    try {
                        obras = service.getObras(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    update_list();
                    ID_Lista = -1;
                } else {
                    toast("Selecione alguma obra");
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void update_list(){
        adapter = new ObraAdapter(getApplicationContext(), obras);
        lista.setAdapter(adapter);
    }
}
