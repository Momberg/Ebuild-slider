package tcc.ebuild_slider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {

    private static final String INTERNO = "Interna";
    private static final String EXTERNO = "Externa";
    private static final String[] interno =  new String[] {"Verificar a necessidade","Elaboração dos estudos técnicos preliminares","Licença ambiental prévia","Elaboração do projeto básico","Elaboração do projeto executivo"};
    private static final String[] externo = new String[] {"Publicação do edital","Licitação","Contrataçao e designação do fiscal da obra","Pagamento seguindo o cronograma físico-financeiro e ordem cronológica","Recebimento da obra","Devolução de garantia","Registros finais"};
    EditText nome, rua, bairro, cidade;
    TextView data;
    Button salvar, cancelar, Bdata;
    String nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra;
    boolean preenchido = false, form_enter = false;
    RadioButton int_ext;
    RadioGroup GrupoRadio;
    String item_selecionado;
    ListView lista;
    int item = 0, month, year, day;
    SharedPreferences cod_final;
    private static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        preenchido = false;
        cod_final = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        find_IDs();
        fases();
        botoes();
        get_info_if_exist();
    }

    public void salvar(){
        nome_obra = nome.getText().toString();
        data_obra = data.getText().toString();
        rua_obra = rua.getText().toString();
        bairro_obra = bairro.getText().toString();
        cidade_obra = cidade.getText().toString();
    }

    public void fases(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
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
                    if (int_ext.getText().equals(INTERNO)) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, interno);
                        lista.setAdapter(adapter);
                    }
                    if (int_ext.getText().equals(EXTERNO)) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, externo);
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
        lista = (ListView) findViewById(R.id.list_Int_Ext);
        GrupoRadio = (RadioGroup) findViewById(R.id.radio_Ext_Int);
        Bdata = (Button) findViewById(R.id.change_date);
        setdateontext();
    }

    private void botoes(){
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
                    form_enter = true;
                    cod_final.edit().putBoolean("entrou", form_enter).apply();
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
                form_enter = true;
                cod_final.edit().putBoolean("entrou", form_enter).apply();
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

    private void get_info_if_exist(){
        if(!cod_final.getString("nome", "").equals("")){
            nome.setText(cod_final.getString("nome", ""));
            data.setText(cod_final.getString("data", ""));
            rua.setText(cod_final.getString("rua", ""));
            bairro.setText(cod_final.getString("bairro", ""));
            cidade.setText(cod_final.getString("cidade", ""));
        }
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
}
