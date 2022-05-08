package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, email, telephone, address, password, confirmPassword;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstname);
        lastName =  findViewById(R.id.lastname);
        email =  findViewById(R.id.email);
        telephone =  findViewById(R.id.telephone);
        address =  findViewById(R.id.address);
        password =  findViewById(R.id.password);
        confirmPassword =  findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.signupButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // taking data from the input fields to variables
                final String firstNameTxt = firstName.getText().toString();
                final String lastNameTxt = lastName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String telephoneTxt = telephone.getText().toString();
                final String addressTxt = address.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                // checking whether the fields are empty
                if(firstNameTxt.isEmpty() || lastNameTxt.isEmpty() || emailTxt.isEmpty() || telephoneTxt.isEmpty() || addressTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()){
                    Toast.makeText(Register.this, "Fill out all the fields and retry", Toast.LENGTH_SHORT).show();
                }
                // checking whether the passwords are matching or not
                else if(!(passwordTxt.equals(confirmPasswordTxt))){
                    Toast.makeText(Register.this, "Passwords are not Matching", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!(Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches())){ // validating email address
                        Toast.makeText(Register.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(telephoneTxt.length() == 10)){ // validating telephone number
                        Toast.makeText(Register.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    } else if(passwordTxt.length() < 6){
                        Toast.makeText(Register.this, "Enter a password with minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        String replaceEmail = emailTxt.replace(".", ",");

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("firstname", firstNameTxt);
                        data.put("lastname", lastNameTxt);
                        data.put("telephone", telephoneTxt);
                        data.put("address", addressTxt);
                        data.put("password", passwordTxt);

                        // adding the data to the database
                        FirebaseDatabase.getInstance().getReference().child("fashionHubDB").child("Users").child(replaceEmail).push().setValue(data);
                        Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();

                        // changing the view to login after inserting data
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    // changing the intent to login View
    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}