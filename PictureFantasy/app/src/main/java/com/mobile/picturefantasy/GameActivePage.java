package com.mobile.picturefantasy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameActivePage extends AppCompatActivity {

    Button backToHomeFromGame;
    ListView listOfPLayersAndPoints;
    TextView nameOfGame;
    TextView nameOfPlayer;
    TextView currentPicturePicker;
    FirebaseDatabase database;
    DatabaseReference gameRef;
    int gameRefCalled = 0;
    ArrayList<String> playerAndPoints;
    ArrayList<String> players;
    Random random;
    int dialogCalled = 0;
    String currentPicture;
    TextView currentPictureLabel;
    Button takePictureNow;
    public static final int CAMERA_ACTION_CODE = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseStorage storage;
    int currentRound;
    int changingPictureTakenStatus = 0;
    long numberOfPlayersThatTookPicture = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_active_page);

        backToHomeFromGame = findViewById(R.id.backToHomeFromGame);
        listOfPLayersAndPoints = findViewById(R.id.listOfPlayersAndPoints);
        nameOfGame = findViewById(R.id.nameOfGameStart);
        nameOfPlayer = findViewById(R.id.displayNameInGameStart);
        currentPicturePicker = findViewById(R.id.nameOfPicturePicker);
        database = FirebaseDatabase.getInstance();
        gameRef = database.getReference("games");
        playerAndPoints = new ArrayList<>();
        players = new ArrayList<>();
        random = new Random();
        currentPicture = "";
        currentPictureLabel = findViewById(R.id.currentPictureLabel);
        takePictureNow = findViewById(R.id.takePictureNow);
        storage = FirebaseStorage.getInstance();
        final long ONE_MEGABYTE = 1024 * 1024;


        Intent intent = getIntent();
        String gameName = intent.getStringExtra("game");
        String playerName = intent.getStringExtra("name");
        String picturePickerName = intent.getStringExtra("picturePicker");
        nameOfGame.setText(gameName);
        nameOfPlayer.setText(playerName);
        currentPicturePicker.setText("Current Picture Picker: " + picturePickerName);

        StorageReference ref = storage.getReference();
        StorageReference imageRef = ref.child("images/");
        StorageReference gamesRef = imageRef.child(gameName);
        StorageReference roundRef = gamesRef.child("Round " + currentRound);
        StorageReference playerRef = roundRef.child(playerName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O); {
            NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (gameRefCalled == 0) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (snap.child("game").getValue(String.class).equals(gameName)) {
                            if (gameRefCalled == 0) {
                                if (!snap.child("currentPicture").getValue(String.class).equals("")) {
                                    currentRound = snap.child("currentRound").getValue(Integer.class);
                                    currentPicture = snap.child("currentPicture").getValue(String.class);
                                    currentPictureLabel.setText("Current picture: \n" + currentPicture);
                                } else {
                                    takePictureNow.setVisibility(View.GONE);
                                }


                            }
                            snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (gameRefCalled == 0) {
                                        gameRefCalled = 1;
                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            String PaP = "Player: " + snap.child("name").getValue(String.class) + " Points: " + snap.child("points").getValue(Integer.class);
                                            players.add(snap.child("name").getValue(String.class));
                                            playerAndPoints.add(PaP);
                                        }
                                        listOfPLayersAndPoints.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, playerAndPoints));
                                    }


                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            snap.getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String s = snapshot.child("currentPicture").getValue(String.class);
                                    boolean picturePicked = snapshot.child("picturePickedForRound").getValue(boolean.class);
                                    if (s!= null && !s.equals("") && !picturePicked) {
                                        snapshot.getRef().child("picturePickedForRound").setValue(true);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(GameActivePage.this, "My notification");
                                        builder.setContentTitle("Round started!");
                                        builder.setContentText(snapshot.child("currentPicture").getValue(String.class));
                                        builder.setSmallIcon(R.drawable.ic_launcher_background);
                                        builder.setAutoCancel(true);

                                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(GameActivePage.this);
                                        managerCompat.notify(1, builder.build());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //check
        if (playerName.equals(picturePickerName)) {
            //put a dialog box here that says you're the picker picture.
            //the dialog also includes an editText for them to write what to take a picture of.
            //i will add boolean later that shows if picture has been picked or not yet

            takePictureNow.setVisibility(View.GONE);

            final Dialog dialog = new Dialog(GameActivePage.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.picture_picker_dialolg);
            dialog.setCancelable(false);

            EditText pictureName = dialog.findViewById(R.id.pictureName);
            Button pick = dialog.findViewById(R.id.pickButton);

            Intent intent2 = getIntent();
            String GM = intent2.getStringExtra("game");
            dialogCalled = 1;
            gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (dialogCalled == 1) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if (snap.child("game").getValue(String.class).equals(GM)) {
                                if (!snap.child("currentPicture").getValue(String.class).equals("")) {
                                    dialog.cancel();
                                    snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (dialogCalled == 1) {
                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    if (!snap.child("name").getValue(String.class).equals(playerName)) {
                                                        if (snap.child("pictureTakenForRound").getValue(boolean.class)) {
                                                            numberOfPlayersThatTookPicture++;
                                                        }
                                                    }
                                                }
                                                if (numberOfPlayersThatTookPicture != (snapshot.getChildrenCount() - 1)) {
                                                    new AlertDialog.Builder(GameActivePage.this)
                                                            .setTitle("Waiting for all players to take a picture.")
                                                            .show();
                                                } else {
                                                    Intent intent5 = new Intent(getApplicationContext(), LookThroughPicturesPage.class);
                                                    intent5.putExtra("game", GM);
                                                    startActivity(intent5);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            dialog.show();
            pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pictureName.getText().toString().equals("")) {
                        Toast.makeText(GameActivePage.this, "Please enter a valid picture idea.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent2 = getIntent();
                        String GM = intent2.getStringExtra("game");
                        dialogCalled = 1;
                        gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (dialogCalled == 1) {
                                    dialogCalled = 0;
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        if (snap.child("game").getValue(String.class).equals(GM)) {
                                            if (snap.child("currentPicture").getValue(String.class).equals("")) {
                                                snap.getRef().child("currentPicture").setValue(pictureName.getText().toString());
                                                currentPictureLabel.setText("Current picture: \n" + pictureName.getText().toString());
                                                dialog.cancel();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
        }


        backToHomeFromGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.putExtra("name", playerName);
                startActivity(intent);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap finalPhoto = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    finalPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    playerRef.putBytes(data).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GameActivePage.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            changingPictureTakenStatus = 1;
                            gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (changingPictureTakenStatus == 1) {
                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            if (snap.child("game").getValue(String.class).equals(gameName)) {
                                                if (changingPictureTakenStatus == 1) {
                                                    snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (changingPictureTakenStatus == 1) {
                                                                changingPictureTakenStatus = 0;
                                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                                    if (snap.child("name").getValue(String.class).equals(playerName)) {
                                                                        snap.getRef().child("pictureTakenForRound").setValue(true);
                                                                    }
                                                                }
                                                            }


                                                        }


                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }
        });

        takePictureNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(GameActivePage.this, "There is no app that can do this!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}