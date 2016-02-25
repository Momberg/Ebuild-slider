package tcc.ebuild_slider;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by momberg on 25/02/16.
 */
public class DialogCall{
    EditText user, password;
    Button Blog, Bcancel;

    public void callLoginDialog(final Activity activity) {
        final Dialog myDialog = new Dialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_login);
        myDialog.setCanceledOnTouchOutside(false);

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
                if (user.getText().toString().length() <= 0) {
                    Toast.makeText(activity.getApplicationContext(), "Preencha o campo Usuário", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().length() <= 0) {
                    Toast.makeText(activity.getApplicationContext(), "Preencha o campo password", Toast.LENGTH_LONG).show();
                }


                if ((user.getText().toString().length() != 0) && (password.getText().toString().length() != 0)) {
                    if (((user.getText().toString().equals("admin")) && (password.getText().toString().equals("admin")))) {
                        //Intent intent = new Intent(view.getContext(), MapsUserActivity.class);
                        //startActivity(intent);
                        myDialog.dismiss();
                        //finish();
                    }

                    if (!((user.getText().toString().equals("admin")) && (password.getText().toString().equals("admin")))) {
                        Toast.makeText(activity.getApplicationContext(), "Usuario inexistente ou senha incorreta", Toast.LENGTH_LONG).show();
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
}
