package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserProfileUpdate extends AppCompatActivity {
    Button HomeButton, update;
    String firstname, lastname, email, EMAIL;
    TextView FirstName, LastName, Email, Telephone, Address;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        HomeButton = findViewById(R.id.home);
        FirstName = findViewById(R.id.firstNameU);
        LastName = findViewById(R.id.lastNameU);
        Email = findViewById(R.id.emailU);
        Telephone = findViewById(R.id.telephoneU);
        Address = findViewById(R.id.addressU);
        update = findViewById(R.id.update);

        Bundle extras = getIntent().getExtras();
        String user = extras.getString("email");

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

                    email = UserEmail.replace(".", ",");

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String NewFirstName = FirstName.getText().toString();
                            String NewLastName = LastName.getText().toString();
                            String NewAddress = Address.getText().toString();
                            String NewTelephone = Telephone.getText().toString();

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("firstname", NewFirstName);
                            data.put("lastname", NewLastName);
                            data.put("telephone", NewTelephone);
                            data.put("address", NewAddress);
                            data.put("password", UserPassword);

                            FirebaseDatabase.getInstance().getReference().child("fashionHubDB").child("Users").child(email).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UserProfileUpdate.this, "Update Complete", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                    intent.putExtra("user", email);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                    firstname = UserFirstName;
                    lastname = UserLastName;

                    EMAIL = email;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileUpdate.this, "Database Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}