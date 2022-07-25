package com.harrysoft.androidbluetoothserial.demoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class AutoFollowActivity extends AppCompatActivity {

    private TextView connectionText, messagesView, Text_Device;
    private EditText messageBox;
    private Button sendButton, connectButton, Button_f, Button_b, Button_l, Button_r, Button_s;
    private ImageView return_Image;
    private CommunicateViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_follow);
        // Enable the back button in the action bar if possible
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(CommunicateViewModel.class);

//        // This method return false if there is an error, so if it does, we should close.
//        if (!viewModel.setupViewModel(getIntent().getStringExtra("device_name"), getIntent().getStringExtra("device_mac"))) {
//            finish();
//            return;
//        }

        // Setup our Views
        //connectionText = findViewById(R.id.communicate_connection_text);
        messagesView = findViewById(R.id.communicate_messages);
        messageBox = findViewById(R.id.communicate_message);
        sendButton = findViewById(R.id.communicate_send);
        //connectButton = findViewById(R.id.communicate_connect);
        return_Image = findViewById(R.id.communicate_toolbar_return);
        Button_f = findViewById(R.id.button_f);
        Button_b = findViewById(R.id.button_b);
        Button_l = findViewById(R.id.button_l);
        Button_r = findViewById(R.id.button_r);
        Button_s = findViewById(R.id.button_s);
        Text_Device = findViewById(R.id.communicate_toolbar_title);

        return_Image.setOnClickListener(new View.OnClickListener()
        {@Override
            public void onClick(View v)
            {
                AutoFollowActivity.this.finish();
            }
        });

        // Start observing the data sent to us by the ViewModel
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> {
            if (!TextUtils.isEmpty(name)) {
                Text_Device.setText(name);
            }
        });
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.no_messages);
            }
            messagesView.setText(message);
        });
        viewModel.getMessage().observe(this, message -> {
            // Only update the message if the ViewModel is trying to reset it
            if (TextUtils.isEmpty(message)) {
                messageBox.setText(message);
            }
        });

        //setup the FrameLayout title
        Text_Device.setText(viewModel.getDeviceName().getValue());
        // Setup the send button click action
        sendButton.setOnClickListener(v -> viewModel.sendMessage(messageBox.getText().toString()));
        Button_f.setOnClickListener(v -> viewModel.sendMessage("F"));
        Button_b.setOnClickListener(v -> viewModel.sendMessage("X"));
        Button_l.setOnClickListener(v -> viewModel.sendMessage("L"));
        Button_r.setOnClickListener(v -> viewModel.sendMessage("R"));
        Button_s.setOnClickListener(v -> viewModel.sendMessage("S"));
    }

    // Called when the ViewModel updates us of our connectivity status
    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                connectionText.setText(R.string.status_connected);
                messageBox.setEnabled(true);
                sendButton.setEnabled(true);
                Button_f.setEnabled(true);
                Button_b.setEnabled(true);
                Button_l.setEnabled(true);
                Button_r.setEnabled(true);
                Button_s.setEnabled(true);
                connectButton.setEnabled(true);
                connectButton.setText(R.string.disconnect);
                connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
                connectionText.setText(R.string.status_connecting);
                messageBox.setEnabled(false);
                sendButton.setEnabled(false);
                Button_f.setEnabled(false);
                Button_b.setEnabled(false);
                Button_l.setEnabled(false);
                Button_r.setEnabled(false);
                Button_s.setEnabled(false);
                connectButton.setEnabled(false);
                connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:
                connectionText.setText(R.string.status_disconnected);
                messageBox.setEnabled(false);
                sendButton.setEnabled(false);
                Button_f.setEnabled(false);
                Button_b.setEnabled(false);
                Button_l.setEnabled(false);
                Button_r.setEnabled(false);
                Button_s.setEnabled(false);
                connectButton.setEnabled(true);
                connectButton.setText(R.string.connect);
                connectButton.setOnClickListener(v -> viewModel.connect());
                break;
        }
    }

    // Called when a button in the action bar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If the back button was pressed, handle it the normal way
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when the user presses the back button
    @Override
    public void onBackPressed() {
        // Close the activity
        finish();
    }
}
