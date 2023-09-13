package com.mobile.picturefantasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.QuoteSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SignUpPage extends AppCompatActivity {

    EditText name;
    EditText username;
    EditText password;
    Button done;
    private FirebaseAuth Auth;
    DatabaseReference database;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        name = findViewById(R.id.nameField);
        username = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        done = findViewById(R.id.Done);
        db = FirebaseDatabase.getInstance();
        database = db.getReference("users");
        Auth = FirebaseAuth.getInstance();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream is = getApplicationContext().getAssets().open("innapropiateWords.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while (reader.readLine() != null) {
                        String a = reader.readLine();
                        if (name.getText().toString().equals(reader.readLine())) {
                            Toast.makeText(SignUpPage.this, "Display name is inappropriate!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (username.getText().toString().length() > 320) {
                    Toast.makeText(getApplicationContext(), "Email is too long!", Toast.LENGTH_SHORT).show();
                } else if (!username.getText().toString().contains("@")) {
                    Toast.makeText(getApplicationContext(), "Not a valid email!", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().length() > 15) {
                    Toast.makeText(getApplicationContext(), "Your name is too long!", Toast.LENGTH_SHORT).show();
                } else if ((password.getText().toString().length() < 8)) {
                    Toast.makeText(getApplicationContext(), "Please make a stronger password!", Toast.LENGTH_SHORT).show();
                } else if (username.getText().toString().equals("") || name.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(SignUpPage.this, "Please make an input for all!", Toast.LENGTH_SHORT).show();
                } else {
                    onSignUp(view);
                }
            }
        });

    }

    public void onSignUp(View view) {

        EditText editText = findViewById(R.id.emailField);
        final String username = editText.getText().toString();

        editText = findViewById(R.id.passwordField);
        String password = editText.getText().toString();

        editText = findViewById(R.id.nameField);
        final String name = editText.getText().toString();

        final SignUpPage thisAct = this;

        Auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, save new user in Freebase
                            String uid = database.push().getKey();
                            User user = new User();
                            user.setEmail(username);
                            user.setName(name);
                            Toast.makeText(SignUpPage.this, uid,
                                    Toast.LENGTH_SHORT).show();
                            database.child(uid).setValue(user);
                            Intent intent = new Intent(thisAct, LoginPage.class);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            FirebaseUser user2 = Auth.getCurrentUser();
                            user2.updateProfile(profileUpdates);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(SignUpPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}