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

import java.util.List;

public class EditarActivity extends AppCompatActivity {

    private static final int INTERNO = 2131558558;
    private static final int EXTERNO = 2131558559;
    private static final String[] interno =  new String[] {"Verificar a necessidade","Elaboração dos estudos técnicos preliminares","Licença ambiental prévia","Elaboração do projeto básico","Elaboração do projeto executivo"};
    private static final String[] externo = new String[] {"Publicação do edital","Licitação","Contrataçao e designação do fiscal da obra","Pagamento seguindo o cronograma físico-financeiro e ordem cronológica","Recebimento da obra","Devolução de garantia","Registros finais"};
    EditText nome, data, rua, bairro, cidade;
    Button salvar, cancelar;
    String nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra;
    RadioButton int_ext, in, out;
    RadioGroup GrupoRadio;
    String item_selecionado, temp, item_da_base;
    ListView lista;
    int item = 0, ID_obra;
    SharedPreferences cod_obra, edit;
    final ObraService service = new ObraService();
    List<Obra> obra;
    Obra o;
    ArrayAdapter<String> adapter;
    final ObrasDB db = new ObrasDB(this);
    boolean editado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        cod_obra = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        find_IDs();
        getID_para_banco();
        fases();
        salvar = (Button) findViewById(R.id.Bsalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                armazenar_info();
                if(item == 1 && !nome_obra.equals("") && !data_obra.equals("") && !rua_obra.equals("") && !bairro_obra.equals("") && !cidade_obra.equals("")){
                    salvar();
                    editado = true;
                    edit.edit().putBoolean("editado", editado).apply();
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
                finish();
            }
        });
    }

    private void armazenar_info(){
        nome_obra = nome.getText().toString();
        data_obra = data.getText().toString();
        rua_obra = rua.getText().toString();
        bairro_obra = bairro.getText().toString();
        cidade_obra = cidade.getText().toString();
        temp = int_ext.getText().toString();
    }

    public void salvar(){
        o.edit_obra(nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra, temp, item_selecionado);
        db.save(o);
    }

    public void fases(){
        GrupoRadio = (RadioGroup) findViewById(R.id.radio_Ext_Int);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                item_selecionado = (String) arg0.getAdapter().getItem(arg2);
                item = 1;
                toast("index selecionado: " + arg2);
            }
        });
        int_ext = (RadioButton) findViewById(GrupoRadio.getCheckedRadioButtonId());
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
        in = (RadioButton) findViewById(R.id.radioInterno);
        out = (RadioButton) findViewById(R.id.radioExterno);
    }

    private void getID_para_banco(){
        ID_obra = cod_obra.getInt("ID", 0);
        obra = service.getObrabyID(getApplicationContext(), ID_obra);
        lista = (ListView) findViewById(R.id.list_Int_Ext);
        o = obra.get(0);
        nome.setText(o.getNome());
        data.setText(o.getData());
        rua.setText(o.getRua());
        bairro.setText(o.getBairro());
        cidade.setText(o.getCidade());
        item_da_base = o.getFase();
        if(o.getTipoFase().equals("Interna")){
            in.setChecked(true);
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
            int i = 0;
            for(String inte : interno){
                inte = interno[i];
                if(inte.equals(item_da_base)){
                    lista.setItemChecked(i, true);
                    lista.setSelection(i);
                    lista.setSelected(true);
                    lista.getOnItemSelectedListener();
                }
                i += 1;
            }
        } else {
            out.setChecked(true);
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, externo);
        }
    }
}
