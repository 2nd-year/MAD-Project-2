package com.example.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class activity_add_card extends AppCompatActivity {
    Button add;
    EditText CardNumberInput, MMInput,YYInput, CVVInput, CardNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        add = findViewById(R.id.button2);
        CardNumberInput = findViewById(R.id.CardNumberInput);
        MMInput = findViewById(R.id.MMInput);
        YYInput = findViewById(R.id.YYInput);
        CVVInput = findViewById(R.id.CVVInput);
        CardNameInput = findViewById(R.id.CardNameInput);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CardNumber = CardNumberInput.getText().toString();
                String MM = MMInput.getText().toString();
                String YY = YYInput.getText().toString();
                String CVV = CVVInput.getText().toString();
                String CardName = CardNameInput.getText().toString();

                if (CardNumber.isEmpty() || MM.isEmpty() ||YY.isEmpty() || CVV.isEmpty() || CardName.isEmpty()) {
                    Toast.makeText(activity_add_card.this, "Empty input fields not allowed", Toast.LENGTH_SHORT).show();
                }
                else {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("CardNumber", CardNumber);
                    map.put("MM", MM);
                    map.put("YY", YY);
                    map.put("CVV", CVV);
                    map.put("CardHolderName", CardName);

                    FirebaseDatabase.getInstance().getReference().child("FashionHub").child("Payment").push().updateChildren(map);
                    Toast.makeText(activity_add_card.this, "Payment successful", Toast.LENGTH_SHORT).show();
                }
            }
    });
}}