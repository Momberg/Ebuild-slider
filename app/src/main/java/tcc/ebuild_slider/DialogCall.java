package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by momberg on 25/02/16.
 */
public class DialogCall{
    EditText user, password;
    Button Blog, Bcancel;
    String temp_list, item_selecionado;
    int ID_Lista;
    boolean selecionado = false;
    Activity str;
    ArrayAdapter<String> adapter;
    ListView lista;
    ArrayList<String> info_lista = new ArrayList<>();
    Obra obra = new Obra();
    List<Obra> obras;
    ObraService service = new ObraService();

    public void callLoginDialog(final Activity activity) {
        str = activity;
        final Dialog myDialog = new Dialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_login);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        user = (EditText) myDialog.findViewById(R.id.username);
        password = (EditText) myDialog.findViewById(R.id.password);
        Blog = (Button) myDialog.findViewById(R.id.btn_box);
        Bcancel = (Button) myDialog.findViewById(R.id.buttonCancel);
        myDialog.show();

        password.setText("admin");
        user.setText("admin");

        Blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((user.getText().toString().length() <= 0) && !(password.getText().toString().length() <= 0)) {
                    toast("Preencha o campo Usuário");
                } else if ((password.getText().toString().length() <= 0) && !(user.getText().toString().length() <= 0)){
                    toast("Preencha o campo password");
                } else if ((user.getText().toString().length() <= 0) && (password.getText().toString().length() <= 0)) {
                    toast("Preencha os campos acima");
                }


                if ((user.getText().toString().length() != 0) && (password.getText().toString().length() != 0)) {
                    if (((user.getText().toString().equals("admin")) && (password.getText().toString().equals("admin")))) {
                        Intent intent = new Intent(view.getContext(), LoggedActivity.class);
                        activity.startActivity(intent);
                        myDialog.dismiss();
                        //activity.finish();
                    }

                    if (!((user.getText().toString().equals("admin")) && (password.getText().toString().equals("admin")))) {
                        toast("Usuario inexistente ou senha incorreta");
                    }
                }

            }
        });

        Bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }


    public void call_Dialog_Lista_Obras_logged(final Activity activity){
        str = activity;
        final Dialog myDialog = new Dialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_list_work_logged);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        //set_list(activity);
        //adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, info_lista);
        try {
            obras = service.getObras(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lista = (ListView) myDialog.findViewById(R.id.lista_edita_obra);
        lista.setAdapter(new ObraAdapter(activity, obras));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //item_selecionado = (String) arg0.getAdapter().getItem(arg2);
                ID_Lista = arg2 + 1;
               //toast(item_selecionado);
                selecionado = true;
            }
        });

        Button remove_obra = (Button) myDialog.findViewById(R.id.exclui);
        remove_obra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ObrasDB db = new ObrasDB(str.getApplicationContext());
                db.delete(ID_Lista);
            }
        });
    }

    /*public void set_list(final Activity activity){
        ObraService service = new ObraService();
        try {
            List<Obra> obras = service.getObras(activity);
            for(Obra obra:obras){
                lista_itens(obra.getNome(), obra.getData(), obra.getRua(), obra.getBairro(), obra.getCidade(), obra.getTipoFase(), obra.getFase());
                info_lista.add(temp_list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lista_itens(String nome, String data, String rua, String bairro, String cidade, String tipo, String fase){
        temp_list = "Nome da obra: " + nome + "\n" +
                    "Data: " + data + "\n" +
                    "Endereço: " + rua + ", " + bairro + " - " + cidade +"\n" +
                    "Fase:" + tipo + "\n" +
                    "Fase do Processo: " + fase;
    }*/

    private void toast(String text){
        Toast.makeText(str.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
