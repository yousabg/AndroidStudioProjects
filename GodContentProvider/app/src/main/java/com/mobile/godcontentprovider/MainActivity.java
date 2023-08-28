package com.mobile.godcontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannelGroup;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button insertButton;
    Button queryButton;
    Button updateButton;
    Button deleteButton;
    Button nextButton;
    Button previousButton;
    EditText gname;
    EditText a1;
    EditText a2;
    EditText a3;
    EditText a4;
    EditText ul;
    EditText baseDmg;
    EditText specialDmg;
    TextView idTv;
    TextView gNameTv;
    TextView a1Tv;
    TextView a2Tv;
    TextView a3Tv;
    TextView a4Tv;
    TextView ulTv;
    TextView baseDmgTv;
    TextView specialDmgTv;

    Cursor mCursor;

    View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues mUpdateValues = new ContentValues();

            mUpdateValues.put(GodProvider.COLUMN_GOD_NAME, gname.getText().toString().trim());
            mUpdateValues.put(GodProvider.COLUMN_ABILITYONE, a1.getText().toString().trim());
            mUpdateValues.put(GodProvider.COLUMN_ABILITYTWO, a2.getText().toString().trim());
            mUpdateValues.put(GodProvider.COLUMN_ABILITYTHREE, a3.getText().toString().trim());
            mUpdateValues.put(GodProvider.COLUMN_ABILITYFOUR, a4.getText().toString().trim());
            mUpdateValues.put(GodProvider.COLUMN_ULTIMATE, ul.getText().toString().trim());
            mUpdateValues.put(GodProvider.BASE_DMG, baseDmg.getText().toString().trim());
            mUpdateValues.put(GodProvider.SPECIAL_DMG, specialDmg.getText().toString().trim());

            String mSelectionClause = GodProvider.COLUMN_GOD_NAME + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYONE + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYTWO + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYTHREE + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYFOUR + " = ? "
                    + " AND " + GodProvider.COLUMN_ULTIMATE + " = ? "
                    + " AND " + GodProvider.BASE_DMG + " = ? "
                    + " AND " + GodProvider.SPECIAL_DMG + " = ? ";

            String[] mSelectionArgs = { gNameTv.getText().toString().trim()
                    , a1Tv.getText().toString().trim()
                    , a2Tv.getText().toString().trim()
                    , a3Tv.getText().toString().trim()
                    , a4Tv.getText().toString().trim()
                    , ulTv.getText().toString().trim()
                    , baseDmgTv.getText().toString().trim()
                    , specialDmgTv.getText().toString().trim()};

            int numRowsUpdates= getContentResolver().update(GodProvider.CONTENT_URI, mUpdateValues,
                    mSelectionClause, mSelectionArgs);

            clear();

        }
    };

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mSelectionClause = GodProvider.COLUMN_GOD_NAME + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYONE + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYTWO + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYTHREE + " = ? "
                    + " AND " + GodProvider.COLUMN_ABILITYFOUR + " = ? "
                    + " AND " + GodProvider.COLUMN_ULTIMATE + " = ? "
                    + " AND " + GodProvider.BASE_DMG + " = ? "
                    + " AND " + GodProvider.SPECIAL_DMG + " = ? ";

            String[] mSelectionArgs = { gNameTv.getText().toString().trim()
                    , a1Tv.getText().toString().trim()
                    , a2Tv.getText().toString().trim()
                    , a3Tv.getText().toString().trim()
                    , a4Tv.getText().toString().trim()
                    , ulTv.getText().toString().trim()
                    , baseDmgTv.getText().toString().trim()
                    , specialDmgTv.getText().toString().trim()};

            int mRowsDeleted = getContentResolver().delete(GodProvider.CONTENT_URI, mSelectionClause,
                    mSelectionArgs);

            clear();
        }
    };

    View.OnClickListener insertListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ContentValues mNewValues = new ContentValues();

            mNewValues.put(GodProvider.COLUMN_GOD_NAME, gname.getText().toString().trim());
            mNewValues.put(GodProvider.COLUMN_ABILITYONE, a1.getText().toString().trim());
            mNewValues.put(GodProvider.COLUMN_ABILITYTWO, a2.getText().toString().trim());
            mNewValues.put(GodProvider.COLUMN_ABILITYTHREE, a3.getText().toString().trim());
            mNewValues.put(GodProvider.COLUMN_ABILITYFOUR, a4.getText().toString().trim());
            mNewValues.put(GodProvider.COLUMN_ULTIMATE, ul.getText().toString().trim());
            mNewValues.put(GodProvider.BASE_DMG, baseDmg.getText().toString().trim());
            mNewValues.put(GodProvider.SPECIAL_DMG, specialDmg.getText().toString().trim());

            getContentResolver().insert(GodProvider.CONTENT_URI, mNewValues);

            clear();
        }
    };

    View.OnClickListener queryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCursor = getContentResolver().query(GodProvider.CONTENT_URI, null, null, null, null);

            if (mCursor != null) {
                if (mCursor.getCount() > 0) {
                    mCursor.moveToNext();
                    setViews();
                }
            }
        }
    };

    View.OnClickListener previousListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCursor != null) {
                if (!mCursor.moveToPrevious()) {
                    mCursor.moveToLast();
                }

                setViews();
            }
        }
    };

    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCursor != null) {
                if (!mCursor.moveToNext()) {
                    mCursor.moveToFirst();
                }
                setViews();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gname = findViewById(R.id.gname);
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        ul = findViewById(R.id.ul);
        baseDmg = findViewById(R.id.baseDmg);
        specialDmg = findViewById(R.id.specialDmg);

        insertButton = findViewById(R.id.insertButton);
        queryButton = findViewById(R.id.queryButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        idTv = findViewById(R.id.unique_id);
        gNameTv = findViewById(R.id.gnameTextView);
        a1Tv = findViewById(R.id.a1TextView);
        a2Tv = findViewById(R.id.a2TextView);
        a3Tv = findViewById(R.id.a3TextView);
        a4Tv = findViewById(R.id.a4TextView);
        ulTv = findViewById(R.id.ulTextView);
        baseDmgTv = findViewById(R.id.baseDmgTextView);
        specialDmgTv = findViewById(R.id.specialDmgTextView);

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);

        insertButton.setOnClickListener(insertListener);

        updateButton.setOnClickListener(updateListener);

        deleteButton.setOnClickListener(deleteListener);

        queryButton.setOnClickListener(queryListener);

        previousButton.setOnClickListener(previousListener);

        nextButton.setOnClickListener(nextListener);
    }

    private void setViews() {
        String text1 = mCursor.getString(0) + ": ";
        idTv.setText(text1);
        String text2 = "God Name: " + mCursor.getString(1);
        gNameTv.setText(text2);
        String text3 = "A1: " + mCursor.getString(2);
        a1Tv.setText(text3);
        String text4 = "A2: " + mCursor.getString(3);
        a2Tv.setText(text4);
        String text5 = "A3: " + mCursor.getString(4);
        a3Tv.setText(text5);
        String text6 = "A4: " + mCursor.getString(5);
        a4Tv.setText(text6);
        String text7 = "ULT: " + mCursor.getString(6);
        ulTv.setText(text7);
        String text8 = "BASE DMG: " + mCursor.getString(7);
        baseDmgTv.setText(text8);
        String text9 = "SPECIAL DMG: " + mCursor.getString(8);
        specialDmgTv.setText(text9);
    }

    private void clear() {
        gname.setText("");
        a1.setText("");
        a2.setText("");
        a3.setText("");
        a4.setText("");
        ul.setText("");
        baseDmg.setText("");
        specialDmg.setText("");

        idTv.setText("");
        gNameTv.setText("");
        a1Tv.setText("");
        a2Tv.setText("");
        a3Tv.setText("");
        a4Tv.setText("");
        ulTv.setText("");
        baseDmgTv.setText("");
        specialDmgTv.setText("");

        mCursor = null;
    }
}