package com.example.a6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_NUMBER = "number";
    private Boolean isLogin = false, isPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        final TextInputLayout logi = findViewById(R.id.log);
        final TextInputLayout password = findViewById(R.id.password);
        final Button loginbtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.register);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

//Jeżeli chcemy aby po wejsciu raz loginem zapamietalo
        String loginremember = sharedPreferences.getString(KEY_LOGIN, null);

        if (loginremember != null){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String logiText = logi.getEditText().getText().toString();
                final String passwordTxt = password.getEditText().getText().toString();

                if(logiText.isEmpty() || passwordTxt.isEmpty()){
                    if(logiText.isEmpty()){
                        logi.setError("Pole nie może być puste");
                    }
                    if(passwordTxt.isEmpty()){
                        password.setError("Pole nie może być puste");
                    }


//                    Toast.makeText(Login.this, "Proszę wprowadzić dane", Toast.LENGTH_SHORT).show();
                }else {

                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(logiText)){
                                final String getPassword = snapshot.child(logiText).child("password").getValue(String.class);
                                if(getPassword.equals(passwordTxt)){
                                    final String getNumber = snapshot.child(logiText).child("phone").getValue(String.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_LOGIN, logiText);
                                    editor.putString(KEY_NUMBER, getNumber);
                                    editor.putString(KEY_LOGED, "admin");
                                    editor.apply();

                                    Toast.makeText(Login.this, "Witamy ponownie", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    isLogin = false;
                                    isPassword = false;
                                    finish();
                                }else {
                                    isPassword = true;
//                                    password.setError("Niepoprawne hasło");
//                                    Toast.makeText(Login.this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                isLogin = true;
//                                logi.setError("Niepoprawny login");
//                                Toast.makeText(Login.this, "Niepoprawny login", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(logiText)){
                                final String getPassword = snapshot.child(logiText).child("password").getValue(String.class);
                                if(getPassword.equals(passwordTxt)){
                                    final String getNumber = snapshot.child(logiText).child("phone").getValue(String.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_LOGIN, logiText);
                                    editor.putString(KEY_NUMBER, getNumber);
                                    editor.putString(KEY_LOGED, "user");
                                    editor.apply();

                                    Toast.makeText(Login.this, "Witamy ponownie", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    isLogin = false;
                                    isPassword = false;
                                    finish();
                                }else {
                                    isPassword = true;
//                                    password.setError("Niepoprawne hasło");
//                                    Toast.makeText(Login.this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                isLogin = true;
//                                logi.setError("Niepoprawny login");
//                                Toast.makeText(Login.this, "Niepoprawny login", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if(isPassword){
                        password.setError("Niepoprawne hasło");
                    }
                    if (isLogin){
                        logi.setError("Niepoprawny login");
                    }
                }
            }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}