package com.example.firebaseauthentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtEmail;
    private EditText edtPass;

    private RadioButton radioAdmin, radioUser;

    //Datos a registrar

    private String name="";
    private String email="";
    private String password="";

    private String role ="";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtNombre = findViewById(R.id.editTextNombre);
        edtEmail = findViewById(R.id.editTextEmailAddress);
        edtPass = findViewById(R.id.editTextPassword);
        radioAdmin = findViewById(R.id.radioAdmin);
        radioUser = findViewById(R.id.radioUser);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void Registrar(View view){
        name = edtNombre.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPass.getText().toString();

        if (radioAdmin.isChecked()) {
            role = "admin";
        } else if (radioUser.isChecked()) {
            role = "user";
        } else {
            Toast.makeText(this, "Porfavor seleccione un rol", Toast.LENGTH_SHORT).show();
            return;
        }


        if (name.isEmpty() || email.isEmpty() ||  password.isEmpty()) {
            Toast.makeText(this, "Por favor llene las tres casillas", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(password.length() >= 6){
                registerUser();
            }
            else{
                Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name",name);
                    map.put("email",email);
                    map.put("password",password);
                    map.put("role", role);


                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                //validacion de roles si es admin lanza un activity si es user lanza otro
                                if ("admin".equals(role)) {
                                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(MainActivity.this, UserActivity.class);
                                    startActivity(i);
                                }
                                finish();

                            }else {
                                Toast.makeText(MainActivity.this,"No se pudo registrar correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(MainActivity.this,"No se pudo registrar el Usuario",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }//finish RegisterUser


    public void iniciarSesion(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
        }
    }
}