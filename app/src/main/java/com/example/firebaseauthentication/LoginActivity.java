package com.example.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPass;

    private String email="";
    private String pass="";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtEmail = findViewById(R.id.editTextEmailAddress);
        edtPass = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view){
        email = edtEmail.getText().toString();
        pass = edtPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Por favor llene las tres casillas", Toast.LENGTH_SHORT).show();
        }
        else{
            loginUser();
        }

    }

    public void loginUser(){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"No se pudo iniciar sesion compruebe los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}