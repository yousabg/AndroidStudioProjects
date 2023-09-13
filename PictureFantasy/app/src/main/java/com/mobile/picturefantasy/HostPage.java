package com.mobile.picturefantasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HostPage extends AppCompatActivity {

    EditText gameName;
    EditText gameID;
    Button createGame;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ArrayList<Player> players;
    FirebaseAuth Auth;
    Button backToHomeFromHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_page);

        gameName = findViewById(R.id.hostNameField);
        gameID = findViewById(R.id.gameIDField);
        createGame = findViewById(R.id.createButton);
        database = FirebaseDatabase.getInstance();
        Auth = FirebaseAuth.getInstance();
        myRef = database.getReference("games");
        players = new ArrayList<>();
        Player player = new Player(Auth.getCurrentUser().getDisplayName(), false, 0, false);
        players.add(player);
        backToHomeFromHost = findViewById(R.id.backToHomeFromHost);

        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = myRef.push().getKey();
                assert uid != null;
                Game game = new Game(gameName.getText().toString(), gameID.getText().toString(), players, false, Auth.getCurrentUser().getDisplayName(), "", 0, 0, "", false);
                myRef.child(uid).setValue(game);
                Toast.makeText(HostPage.this, "Good", Toast.LENGTH_SHORT).show();

//int i = 0;
//int num = 0;
//Random rand = new Random();
//String gameID = "";
//while (i < 10) {
//    num = rand.nextInt(9);
//    gameID = gameID + num;
//    i++;
//        }

            }
        });

        backToHomeFromHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.putExtra("name", Auth.getCurrentUser().getDisplayName());
                startActivity(intent);
            }
        });



    }
}