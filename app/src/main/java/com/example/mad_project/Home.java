package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home<placeholder> extends AppCompatActivity {

    BottomNavigationItemView placeHolder;
    BottomNavigationView bottomNavViewer;
    TextView username, header;

    ImageView logout;
    String UserName;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private long backTime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment(), "HomeFragment").commit();

        header = findViewById(R.id.GreetingText);
        placeHolder = findViewById(R.id.placeholder);
        bottomNavViewer = findViewById(R.id.bottomNavView);
        username = findViewById(R.id.name);
        logout = findViewById(R.id.logout);

        bottomNavViewer.setBackground(null);
        placeHolder.setClickable(false);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null){
            String Name = account.getDisplayName();

            username.setText("Hello " + Name);
            UserName = Name;
        } else {
            Bundle extras = getIntent().getExtras();
            String user_name = extras.getString("Username");
            username.setText("Hello " + user_name);
            UserName = user_name;
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

        bottomNavViewer.setOnNavigationItemSelectedListener(Lister);
    }

    @Override
    public void onBackPressed() {
        if(backTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        }
        backTime = System.currentTimeMillis();
    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener Lister = new BottomNavigationView.OnNavigationItemSelectedListener() {
        String Fragment;
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.home:
                    header.setText("Welcome...");
                    username.setText("Hello " + UserName);
                    selectedFragment = new HomeFragment();
                    Fragment = "HomeFragment";
                    break;

                case R.id.offer:
                    header.setText("Offers");
                    username.setText("Here is our offers");
                    selectedFragment = new OffersFragment();
                    Fragment = "offerFragment";
                    break;

                case R.id.menu:
                    header.setText("Menu");
                    username.setText("Select Your Category Here");
                    selectedFragment = new MenuFragment();
                    Fragment = "MenuFragment";
                    break;

                case R.id.more:
                    header.setText("More");
                    username.setText("More Options for User");
                    selectedFragment = new MoreFragment();
                    Fragment = "MoreFragment";
                    break;
            }
            
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, selectedFragment, Fragment).commit();

            return true;
        }
    };
}