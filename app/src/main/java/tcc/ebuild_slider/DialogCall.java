package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
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
public class DialogCall extends AppCompatActivity {
    EditText user, password;
    Button Blog, Bcancel;
    int ID_Lista = -1;
    boolean selecionado = false;
    Activity str;
    ListView lista;
    List<Obra> obras;
    ObraService service = new ObraService();
    ObraAdapter adapter;
    Dialog myDialog;

    public void callLoginDialog(final Activity activity) {
        str = activity;
        myDialog = new Dialog(activity);
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
                } else if ((password.getText().toString().length() <= 0) && !(user.getText().toString().length() <= 0)) {
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

    private void toast(String text){
        Toast.makeText(str.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}
