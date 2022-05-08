package com.example.mad_project;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class MainActivity extends AppCompatActivity{
    ImageView googleLoginButton;
    GoogleSignInOptions gso;
    TextView errorMsg;
    GoogleSignInClient gsc;
    TextInputEditText username, password;
    Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleLoginButton = findViewById(R.id.google_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        LoginButton = findViewById(R.id.signInButton);
        errorMsg = findViewById(R.id.error);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checking whether the fields are empty
                if((username.getText().toString().isEmpty()) && (password.getText().toString().isEmpty())){
                    errorMsg.setText("* Fill Username and Password");
                } else if((username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty())){
                    errorMsg.setText("* Fill Username");
                } else if(!(username.getText().toString().isEmpty()) && (password.getText().toString().isEmpty())){
                    errorMsg.setText("* Fill Password");
                } else {
                    Validate((username.getText().toString()).replace(".", ","), (password.getText().toString()));
                }
            }
        });
    }

    private void Validate(String userName , String userPassword) {
        FirebaseDatabase.getInstance().getReference().child("fashionHubDB").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checking is there a username in the database that u entered
                if(snapshot.hasChild(userName)){
                    String getUserPassword = snapshot.child(userName).child("password").getValue().toString();
                    String UserFirstName = snapshot.child(userName).child("firstname").getValue().toString();
                    String UserLastName = snapshot.child(userName).child("lastname").getValue().toString();

                    // checking the password with the username
                    if(getUserPassword.equals(userPassword)){
//                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("firstName", UserFirstName);
                        intent.putExtra("lastName", UserLastName);
                        intent.putExtra("userName", userName);
                        startActivity(intent);

                    } else {
                        errorMsg.setText("* Invalid Password");
                    }
                }
                else {
                    errorMsg.setText("* Invalid Username");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        if((userName.equals("Admin")) && (userPassword.equals("1234"))){
//            Intent intent = new Intent(getApplicationContext(), Home.class);
//            intent.putExtra("Username", userName);
//            intent.putExtra("Password", userPassword);
//            startActivity(intent);
//        } else if((userName.equals("Admin")) && !(userPassword.equals("1234"))){
//            errorMsg.setText("* Invalid Password");
//        } else if(!(userName.equals("Admin")) && (userPassword.equals("1234"))){
//            errorMsg.setText("* Invalid Username");
//        } else {
//            errorMsg.setText("* Invalid Username or Password");
//        }
    }

    private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                HomeActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Login Error - Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void HomeActivity() {
        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    // changing the intent to register view
    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }
}