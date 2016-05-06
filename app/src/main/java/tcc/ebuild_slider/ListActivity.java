package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    ListView lista;
    List<Obra> obras;
    ObraService service = new ObraService();
    ObraAdapter adapter;
    SharedPreferences cod_obra, edit;
    String nome_busca, search;
    boolean editado = false, busca_done = false;
    Button Bsearch;
    EditText busca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        cod_obra = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        botoes();
        listadeObras();
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void update_list(){
        if(!busca_done){
            try {
                obras = service.getObrasAll(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter = new ObraAdapter(getApplicationContext(), obras);
            lista.setAdapter(adapter);
        } else {
            obras = service.searchObrasName(getApplicationContext(), nome_busca);
            adapter = new ObraAdapter(getApplicationContext(), obras);
            lista.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        editado = edit.getBoolean("editado", false);
        if(editado){
            update_list();
            toast("Atualizado");
            editado = false;
            edit.edit().putBoolean("editado", editado).apply();
        }
    }

    private void botoes(){
        Button remove_obra = (Button) findViewById(R.id.exclui);
        remove_obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID_Lista != -1) {
                    ObrasDB db = new ObrasDB(getApplicationContext());
                    db.delete(ID_Lista);
                    update_list();
                    ID_Lista = -1;
                } else {
                    toast("Selecione alguma obra");
                }
            }
        });

        Button edita_obra = (Button) findViewById(R.id.edita);
        edita_obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_Lista != -1){
                    cod_obra.edit().putInt("ID", ID_Lista).apply();
                    Intent intent = new Intent(getApplicationContext(), EditarActivity.class);
                    startActivity(intent);
                } else {
                    toast("Selecione alguma obra");
                }
            }
        });
    }

    private void listadeObras(){
        try {
            obras = service.getObrasAll(this);
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (busca_done) {
            try {
                obras = service.getObrasAll(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter = new ObraAdapter(this, obras);
            lista.setAdapter(adapter);
            busca_done = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.search_dialog:
                callDialogSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Método de criação de Dialog para fazer busca na base por obras.

    public void callDialogSearch(){
        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_search);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        busca = (EditText) myDialog.findViewById(R.id.busca_text);
        Bsearch = (Button) myDialog.findViewById(R.id.busca_btn);
        myDialog.show();
        Bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome_busca = busca.getText().toString();
                if(!nome_busca.equals("")){
                    obras = service.searchObrasName(getApplicationContext(), nome_busca);
                    adapter = new ObraAdapter(getApplicationContext(), obras);
                    lista.setAdapter(adapter);
                    busca_done = true;
                }
                myDialog.dismiss();
            }
        });
    }
}
