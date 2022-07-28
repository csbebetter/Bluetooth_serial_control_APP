package com.litecdows.androidbluetoothserial.demoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeSelectionActivity extends AppCompatActivity {

    private TextView connectionText, messagesView, Text_Device;
    private Button connectButton, mode1_auto_follow, mode2_remote_control, mode3_recall;
    private CommunicateViewModel viewModel;
    private RockerViewModel rocker;
    private LinearLayout mode_buttons ,debug_windows;
    private ListView listView;
    private String string_rocker_data;


    private final String[] keys = new String[]{
            "婴儿车型号：",
            "婴儿车MAC：",
            "控制模式：",
            "转向控制：",
            "人车距离：",
            "人车角度：",
    };
    private String[] values = new String[]{
            "获取中...",
            "获取中...",
            "请选择···",
            "停止",
            "获取中...",
            "获取中...",
    };
    private final int[] images_ic = new int[]{
            R.drawable.ic_directions_car_black_24dp,
            R.drawable.ic_swap_calls_black_24dp,
            R.drawable.ic_alarm_black_24dp,
            R.drawable.ic_alarm_on_black_24dp,
            R.drawable.ic_chrome_reader_mode_black_24dp,
            R.drawable.ic_directions_car_black_24dp,
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);
        // Enable the back button in the action bar if possible
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(CommunicateViewModel.class);

        // This method return false if there is an error, so if it does, we should close.
        if (!viewModel.setupViewModel(getIntent().getStringExtra("device_name"), getIntent().getStringExtra("device_mac"))) {
            finish();
            return;
        }

        // Setup our Views
        connectionText = findViewById(R.id.mode_selection_connection_text);
        messagesView = findViewById(R.id.mode_selection_messages);
        connectButton = findViewById(R.id.mode_selection_connect);
        Text_Device = findViewById(R.id.mode_selection_toolbar_title);
        mode1_auto_follow = findViewById(R.id.mode_button1);
        mode2_remote_control = findViewById(R.id.mode_button2);
        mode3_recall = findViewById(R.id.mode_button3);
        rocker = findViewById(R.id.rockerViewModel);
        mode_buttons = findViewById(R.id.linearLayout4);
        debug_windows = findViewById(R.id.linearLayout);
        ImageView return_Image = findViewById(R.id.mode_selection_toolbar_return);
        ImageView debug_Image = findViewById(R.id.mode_selection_toolbar_debug);
        ScrollView scroll = findViewById(R.id.scrollView3);
        listView = findViewById(R.id.ListView);
        messagesView.setMovementMethod(ScrollingMovementMethod.getInstance());


        initListView();
        //top left icon; Back to main page
        return_Image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
        {
            ModeSelectionActivity.this.finish();
        }
        });

        debug_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug_windows.getVisibility()==View.GONE){
                    listView.setVisibility(View.GONE);
                    debug_windows.setVisibility(View.VISIBLE);
                }
                else{
                    listView.setVisibility(View.VISIBLE);
                    debug_windows.setVisibility(View.GONE);
                }
            }
        });


        //rocker
        if (rocker != null) {
            rocker.setOnDownActionListener((x, y) -> {
                string_rocker_data = rockerSentData(x,y);
                viewModel.sendMessage(string_rocker_data);
                initListView();
            });
            rocker.setOnMoveActionListener((x, y) -> {
                string_rocker_data = rockerSentData(x,y);
                viewModel.sendMessage(string_rocker_data);
                initListView();
            });
            rocker.setOnUpActionListener((x, y) -> {
                for(int i = 0; i<10; i++){
                    viewModel.sendMessage("S");
                    values[3] = "停止";
                    initListView();
                }
            });
        }

        // Start observing the data sent to us by the ViewModel
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> {
            if (!TextUtils.isEmpty(name)) {
                Text_Device.setText(name);
                values[0] = name;
                initListView();
            }
        });
        viewModel.getDeviceMac().observe(this, mac -> {
            if (!TextUtils.isEmpty(mac)) {
                values[1] = mac;
                initListView();
            }
        });
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.no_mode_select);
            }
            messagesView.setText(message);
            int offset = messagesView.getLineCount() * messagesView.getLineHeight();
            if (offset > scroll.getHeight()) {
                scroll.scrollTo( 0, offset - scroll.getHeight() );
            }
        });



        // Setup the send button click action
        mode1_auto_follow.setOnClickListener(v -> {
            viewModel.sendMessage("1");
            values[2] = "追踪模式";
            initListView();
        });
        mode2_remote_control.setOnClickListener(v -> {
            viewModel.sendMessage("2");
            values[2] = "遥控模式";
            initListView();
            mode_buttons.setVisibility(View.GONE);
            rocker.setVisibility(View.VISIBLE);
        });
        mode3_recall.setOnClickListener(v -> {
            viewModel.sendMessage("3");
            values[2] = "一键召回";
            initListView();
        });
    }

    // Called when the ViewModel updates us of our connectivity status
    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                connectionText.setText(R.string.status_connected);
                mode1_auto_follow.setEnabled(true);
                mode2_remote_control.setEnabled(true);
                mode3_recall.setEnabled(true);
                connectButton.setEnabled(true);
                connectButton.setText(R.string.disconnect);
                connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
                connectionText.setText(R.string.status_connecting);
                mode1_auto_follow.setEnabled(false);
                mode2_remote_control.setEnabled(false);
                mode3_recall.setEnabled(false);
                connectButton.setEnabled(false);
                connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:
                connectionText.setText(R.string.status_disconnected);
                mode1_auto_follow.setEnabled(false);
                mode2_remote_control.setEnabled(false);
                mode3_recall.setEnabled(false);
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
        if(rocker.getVisibility()==View.VISIBLE || debug_windows.getVisibility()==View.VISIBLE){
            if(rocker.getVisibility()==View.VISIBLE){
                rocker.setVisibility(View.GONE);
                mode_buttons.setVisibility(View.VISIBLE);
                values[2] = "请选择···";
                initListView();
            }
            debug_windows.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else {
            finish();
        }
    }

    public String rockerSentData(double x, double y){
        if(x>=y && x>=-y){
            values[3] = "右转";
            return "R";
        }
        else if(x<y && x>=-y){
            values[3] = "倒车";
            return "X";
        }
        else if(x>=y && x<-y){
            double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            if(distance > 0 && distance<=0.4){
                values[3] = "前进1";
                return "F";
            }
            else if(distance > 0.4 && distance<=0.7){
                values[3] = "前进2";
                return "E";
            }
            else{
                values[3] = "前进3";
                return "D";
            }
        }
        else if(x<y && x<-y){
            values[3] = "左转";
            return "L";
        }
        else{
            values[3] = "停止";
            return "S";
        }

    }

    public void initListView(){
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("key", keys[i]);
            objectMap.put("value", values[i]);
            objectMap.put("img", images_ic[i]);
            list.add(objectMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item_babycar_state, new String[]{"key", "value", "img"}, new int[]{R.id.key, R.id.value, R.id.img});
        listView.setAdapter(adapter);
    }

}
