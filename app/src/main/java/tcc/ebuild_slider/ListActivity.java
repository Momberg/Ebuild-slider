package tcc.ebuild_slider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    boolean editado = false;
    Button remove_obra, edita_obra;
    SearchView searchView;

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
        try {
            obras = service.getObrasAll(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new ObraAdapter(getApplicationContext(), obras);
        lista.setAdapter(adapter);
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
        remove_obra = (Button) findViewById(R.id.exclui);
        remove_obra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID_Lista != -1) {
                    ObrasDB db = new ObrasDB(getApplicationContext());
                    db.delete(ID_Lista);
                    update_list();
                    searchView.setQuery("",true);
                    ID_Lista = -1;
                } else {
                    toast("Selecione alguma obra");
                }
            }
        });

        edita_obra = (Button) findViewById(R.id.edita);
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
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_name);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Busca");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                obras = service.searchObrasName(getApplicationContext(), searchView.getQuery().toString().trim());
                adapter = new ObraAdapter(getApplicationContext(), obras);
                lista.setAdapter(adapter);
                if(searchView.getQuery().toString().trim().equals("")){
                    update_list();
                }
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.search_name:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
