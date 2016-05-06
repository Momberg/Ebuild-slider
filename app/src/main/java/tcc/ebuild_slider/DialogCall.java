package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogCall extends AppCompatActivity {
    EditText user, password;
    Button Blog, Bcancel;
    Activity str;
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
