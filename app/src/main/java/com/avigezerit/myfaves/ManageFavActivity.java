package com.avigezerit.myfaves;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* * * * * * * * * * * * *  ADD OR EDIT FAV ACTIVITY - ON LONG TAP * * * * * * * * * * * * */

public class ManageFavActivity extends AppCompatActivity implements View.OnClickListener {

    //log
    private static final String TAG = ManageFavActivity.class.getSimpleName() + " : ";
    private final Context context = ManageFavActivity.this;

    //xml ref
    EditText mNameET;
    EditText mDescET;
    TextView mPositionTV;
    Button useLocationBtn;


    //location instance
    Place tempFL = new Place();
    private boolean gotLatiLangi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fav);

        mNameET = (EditText) findViewById(R.id.mNameET);
        mDescET = (EditText) findViewById(R.id.mDescET);
        mPositionTV = (TextView) findViewById(R.id.mPositionTV);

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        useLocationBtn = (Button) findViewById(R.id.useLocationBtn);
        useLocationBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.saveBtn:

                String mName = mNameET.getText().toString();
                String mDesc = mDescET.getText().toString();

                //form verification
                if (gotLatiLangi && mName.length() > 0 && mDesc.length() > 0) {

                    tempFL.setName(mName);
                    //save to db
                    //dbm.addNewFL(tempFL);
                    finish();

                } else
                    Toast.makeText(context, "Make sure all fields are valid!", Toast.LENGTH_SHORT).show();

                // TODO : Get coordinates from address

                break;
            case R.id.useLocationBtn:
            case R.id.getLocationAgainBTN:

                getMyPosition();

                break;
        }
    }

    private void getMyPosition() {

        /*
        getLocationHelper tempPosition = new getLocationHelper();
        tempPosition.setContext(ManageFavActivity.this);
        tempPosition.getPositionOfMyLocation();

        tempFL.setLati(tempPosition.getLati());
        tempFL.setLongi(tempPosition.getLongi());
        gotLatiLangi = true;

        tempPosition.setContext(context);
        String addressBasedOnPosition = tempPosition.getAddressFromLL(tempPosition);

        //bye bye btn, hello link if problem
        useLocationBtn.setVisibility(View.GONE);
        mPositionTV.setText(" " + addressBasedOnPosition);
        Button getLocationAgain = (Button) findViewById(R.id.getLocationAgainBTN);
        getLocationAgain.setVisibility(View.VISIBLE);
        getLocationAgain.setOnClickListener(this);

        //TODO get Address autoComplete\

        */

    }

}


