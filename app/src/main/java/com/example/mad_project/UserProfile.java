package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    Button HomeButton, AccountDelete, UpdateDetails;
    String firstname, lastname, email, EMAIL;
    TextView FirstName, LastName, Email, Telephone, Address, Password;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        HomeButton = findViewById(R.id.home);
        FirstName = findViewById(R.id.firstName1);
        LastName = findViewById(R.id.lastName1);
        Email = findViewById(R.id.email1);
        Telephone = findViewById(R.id.telephone1);
        Address = findViewById(R.id.address1);
        Password = findViewById(R.id.password1);
        AccountDelete = findViewById(R.id.delete);
        UpdateDetails = findViewById(R.id.updateDetails);

        Bundle extras = getIntent().getExtras();
        String user = extras.getString("user");

        username = user;

        FirebaseDatabase.getInstance().getReference().child("fashionHubDB").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(username);

                if(snapshot.hasChild(username)){
                    String UserFirstName = snapshot.child(username).child("firstname").getValue().toString();
                    String UserLastName = snapshot.child(username).child("lastname").getValue().toString();
                    String UserTelephone = snapshot.child(username).child("telephone").getValue().toString();
                    String UserAddress = snapshot.child(username).child("address").getValue().toString();
                    String UserPassword = snapshot.child(username).child("password").getValue().toString();

                    String UserEmail = username.replace(",", ".");

                    FirstName.setText(UserFirstName);
                    LastName.setText(UserLastName);
                    Email.setText(UserEmail);
                    Telephone.setText(UserTelephone);
                    Address.setText(UserAddress);
                    Password.setText(UserPassword);

                    firstname = UserFirstName;
                    lastname = UserLastName;
                    email = UserEmail.replace(".", ",");
                    EMAIL = email;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Database Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AccountDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteRecord(EMAIL);
            }
        });

        UpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfileUpdate.class);
                intent.putExtra("email", EMAIL);
                startActivity(intent);
            }
        });

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("firstName", firstname);
                intent.putExtra("lastName", lastname);
                intent.putExtra("userName", email);
                startActivity(intent);
            }
        });

    }
    private void DeleteRecord(String emailAddress){
       FirebaseDatabase.getInstance().getReference().child("fashionHubDB").child("Users").child(emailAddress).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               Toast.makeText(getApplicationContext(), "Account Removed Successfully", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(getApplicationContext(), MainActivity.class));
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(), "Error in Removing the Account" + e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }
}