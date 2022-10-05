package it.mirea.fieldofmiracleswithyakudorovich;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.mirea.fieldofmiracleswithyakudorovich.Models.User;

public class MainActivity extends AppCompatActivity {

    Button loginButton, registerButton;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_view, null);
        dialog.setView(registerWindow);

        final EditText editNickName = registerWindow.findViewById(R.id.editNickName);
        final EditText editEmail = registerWindow.findViewById(R.id.editEmail);
        final EditText editPassword = registerWindow.findViewById(R.id.editPassword);
        final EditText editPasswordAgain = registerWindow.findViewById(R.id.editPasswordAgain);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(editEmail.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Ты тупой? где почта", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                if(TextUtils.isEmpty(editNickName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Ты тупой? где ник", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                if(TextUtils.isEmpty(editPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Ты тупой? где пароль", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                if((editPassword.getText().toString()).length() < 8) {
                    Toast.makeText(getApplicationContext(), "Ты тупой? тебя взломают", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                if(!editPassword.getText().toString().equals(editPasswordAgain.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Ты тупой? пароли не совпадают", Toast.LENGTH_LONG).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User();
                        user.setEmail(editEmail.getText().toString());
                        user.setName(editNickName.getText().toString());
                        user.setPassword(editPassword.getText().toString());

                        users.child(user.getEmail()).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Наконец-то зарегался", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
            }
        });
        dialog.show();
    }
}