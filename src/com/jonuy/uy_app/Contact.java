package com.jonuy.uy_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Activity for contact form.
 */
public class Contact extends RoboActivity {

    @InjectView(R.id.button_back) private Button mBackButton;
    @InjectView(R.id.button_submit) private Button mSubmitButton;

    @InjectView(R.id.input_donut) private EditText mInputDonut;
    @InjectView(R.id.input_email) private EditText mInputEmail;
    @InjectView(R.id.input_message) private EditText mInputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    /**
     * Send email using values from the activity's input fields.
     */
    private void sendEmail() {
        String strDonut = mInputDonut.getText().toString();
        String strEmail = mInputEmail.getText().toString();
        String strMessage = mInputMessage.getText().toString();
    }
}
