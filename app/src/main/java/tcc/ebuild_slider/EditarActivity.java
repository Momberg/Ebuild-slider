package tcc.ebuild_slider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class EditarActivity extends AppCompatActivity {

    private static final int INTERNO = 2131558560;
    private static final int EXTERNO = 2131558561;
    private static final String[] interno =  new String[] {"Verificar a necessidade","Elaboração dos estudos técnicos preliminares","Licença ambiental prévia","Elaboração do projeto básico","Elaboração do projeto executivo"};
    private static final String[] externo = new String[] {"Publicação do edital","Licitação","Contrataçao e designação do fiscal da obra","Pagamento seguindo o cronograma físico-financeiro e ordem cronológica","Recebimento da obra","Devolução de garantia","Registros finais"};
    EditText nome, rua, bairro, cidade;
    TextView data;
    Button salvar, cancelar, Bdata;
    String nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra;
    RadioButton int_ext, in, out;
    RadioGroup GrupoRadio;
    TextView fase_ant;
    String item_selecionado, temp, item_da_base;
    ListView lista;
    int item = 0, ID_obra, month, year, day;
    SharedPreferences cod_obra, edit;
    final ObraService service = new ObraService();
    List<Obra> obra;
    Obra o;
    ArrayAdapter<String> adapter;
    final ObrasDB db = new ObrasDB(this);
    boolean editado;
    private static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        cod_obra = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        find_IDs();
        getID_para_banco();
        fases();
        botoes();
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
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                item_selecionado = (String) arg0.getAdapter().getItem(arg2);
                item = 1;
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
                        adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
                        lista.setAdapter(adapter);
                    }
                    if (selectedId == EXTERNO) {
                        adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, externo);
                        lista.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void find_IDs(){
        nome = (EditText) findViewById(R.id.nome);
        data = (TextView) findViewById(R.id.data);
        rua = (EditText) findViewById(R.id.rua);
        bairro = (EditText) findViewById(R.id.bairro);
        cidade = (EditText) findViewById(R.id.cidade);
        in = (RadioButton) findViewById(R.id.radioInterno);
        out = (RadioButton) findViewById(R.id.radioExterno);
        lista = (ListView) findViewById(R.id.list_Int_Ext);
        fase_ant = (TextView) findViewById(R.id.text_fase_anterior);
        Bdata = (Button) findViewById(R.id.change_date);
        setdateontext();
    }

    private void getID_para_banco(){
        ID_obra = cod_obra.getInt("ID", 0);
        obra = service.getObrabyID(getApplicationContext(), ID_obra);
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
            lista.setAdapter(adapter);
            int i = 0;
            for(String inte : interno){
                inte = interno[i];
                if(inte.equals(item_da_base)){
                    fase_ant.setText(item_da_base);
                }
                i += 1;
            }
        } else {
            out.setChecked(true);
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, externo);
            lista.setAdapter(adapter);
            int i = 0;
            for(String exte : externo){
                exte = externo[i];
                if(exte.equals(item_da_base)){
                    fase_ant.setText(item_da_base);
                }
                i += 1;
            }
        }
    }

    private void botoes(){
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

        Bdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            if(day < 10 && month < 10){
                data.setText(new StringBuilder().append("0").append(day).append("/").append("0").append(month + 1).append("/").append(year).append(" "));
            } else if(day < 10 && month >= 10){
                data.setText(new StringBuilder().append("0").append(day).append("/").append(month + 1).append("/").append(year).append(" "));
            } else if(day >= 10 && month < 10){
                data.setText(new StringBuilder().append(day).append("/").append("0").append(month + 1).append("/").append(year).append(" "));
            } else if(day >= 10 && month >= 10){
                data.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year).append(" "));
            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private void setdateontext(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }
}
