package tcc.ebuild_slider;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {

    private static final int INTERNO = 2131558558;
    private static final int EXTERNO = 2131558559;
    EditText nome, data, rua, bairro, cidade;
    Button salvar, cancelar;
    String nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra;
    boolean preenchido = false;
    RadioButton int_ext;
    RadioGroup GrupoRadio;
    String[] interno, externo;
    String item_selecionado;
    ListView lista;
    int item = 0;
    SharedPreferences cod_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        interno = new String[] {"Verificar a necessidade","Elaboração dos estudos técnicos preliminares","Licença ambiental prévia","Elaboração do projeto básico","Elaboração do projeto executivo"};
        externo = new String[] {"Publicação do edital","Licitação","Contrataçao e designação do fiscal da obra","Pagamento seguindo o cronograma físico-financeiro e ordem cronológica","Recebimento da obra","Devolução de garantia","Registros finais"};
        find_IDs();
        fases();
        preenchido = false;
        cod_final = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!cod_final.getString("nome", "").equals("")){
            nome.setText(cod_final.getString("nome", ""));
            data.setText(cod_final.getString("data", ""));
            rua.setText(cod_final.getString("rua", ""));
            bairro.setText(cod_final.getString("bairro", ""));
            cidade.setText(cod_final.getString("cidade", ""));
        }

        salvar = (Button) findViewById(R.id.Bsalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
                if(item == 1 && !nome_obra.equals("") && !data_obra.equals("") && !rua_obra.equals("") && !bairro_obra.equals("") && !cidade_obra.equals("")){
                    cod_final.edit().putString("nome", nome_obra).apply();
                    cod_final.edit().putString("data", data_obra).apply();
                    cod_final.edit().putString("rua", rua_obra).apply();
                    cod_final.edit().putString("bairro", bairro_obra).apply();
                    cod_final.edit().putString("cidade", cidade_obra).apply();
                    cod_final.edit().putString("tipo", int_ext.getText().toString()).apply();
                    cod_final.edit().putString("fase", item_selecionado).apply();
                    preenchido = true;
                    cod_final.edit().putBoolean("preenchido", preenchido).apply();
                    finish();
                } else {
                    toast("Preencha corretamente o formulário");
                }

            }
        });

        cancelar = (Button) findViewById(R.id.Bcancela);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cod_final.edit().putBoolean("preenchido", preenchido).apply();
                finish();
            }
        });
    }

    public void salvar(){
        nome_obra = nome.getText().toString();
        data_obra = data.getText().toString();
        rua_obra = rua.getText().toString();
        bairro_obra = bairro.getText().toString();
        cidade_obra = cidade.getText().toString();
    }

    public void fases(){
        GrupoRadio = (RadioGroup) findViewById(R.id.radio_Ext_Int);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
        lista = (ListView) findViewById(R.id.list_Int_Ext);
        lista.setAdapter(adapter);
        int temId = GrupoRadio.getCheckedRadioButtonId();
        int_ext = (RadioButton) findViewById(temId);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                item_selecionado = (String) arg0.getAdapter().getItem(arg2);
                item = 1;
            }
        });

        GrupoRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = GrupoRadio.getCheckedRadioButtonId();
                int_ext = (RadioButton) findViewById(selectedId);
                if ((int_ext = (RadioButton) findViewById(selectedId)) != null) {
                    if (selectedId == INTERNO) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
                        lista = (ListView) findViewById(R.id.list_Int_Ext);
                        lista.setAdapter(adapter);
                    }
                    if (selectedId == EXTERNO) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, externo);
                        lista = (ListView) findViewById(R.id.list_Int_Ext);
                        lista.setAdapter(adapter);
                    }
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            item_selecionado = (String) arg0.getAdapter().getItem(arg2);
                            item = 1;
                        }
                    });
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void find_IDs(){
        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        rua = (EditText) findViewById(R.id.rua);
        bairro = (EditText) findViewById(R.id.bairro);
        cidade = (EditText) findViewById(R.id.cidade);
    }

}
