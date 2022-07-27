package com.litecdows.androidbluetoothserial.demoapp;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ImageView toolbar_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // This method return false if there is an error, so if it does, we should close.
        if (!viewModel.setupViewModel()) {
            finish();
            return;
        }

        toolbar_about = findViewById(R.id.toolbar_about);
        toolbar_about.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        });

        // Setup our Views
        RecyclerView deviceList = findViewById(R.id.main_devices);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        // Setup the RecyclerView
        //在您的界面中显示大量数据，同时最大限度减少内存用量
        deviceList.setLayoutManager(new LinearLayoutManager(this));//LinearLayoutManager将各个项排列在一维列表中
        DeviceAdapter adapter = new DeviceAdapter();  //DeviceAdapter在下面有定义。
        deviceList.setAdapter(adapter);

        // Setup the SwipeRefreshLayout
        //实现下拉刷新的界面模式
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshPairedDevices();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Start observing the data sent to us by the ViewModel
        viewModel.getPairedDeviceList().observe(MainActivity.this, adapter::updateList);

        // Immediately refresh the paired devices list
        viewModel.refreshPairedDevices();

    }

    // Called when clicking on a device entry to start the AutoFollowActivity
    public void openModeSelectionActivity(String deviceName, String macAddress) {
        //显示Intent就是直接以“类名称”来指定要启动哪一个Activity;
        Intent intent = new Intent(this, ModeSelectionActivity.class);
        intent.putExtra("device_name", deviceName);
        intent.putExtra("device_mac", macAddress);
        startActivity(intent);
    }

    // Called when a button in the action bar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// If the back button was pressed, handle it the normal way
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called when the user presses the back button
    @Override
    public void onBackPressed() {
        // Close the activity
        finish();
    }

    // A class to hold the data in the RecyclerView
    //列表中的每个独立元素都由一个 ViewHolder 对象进行定义
    private class DeviceViewHolder extends RecyclerView.ViewHolder {


        private final RelativeLayout layout;
        private final TextView text1;
        private final TextView text2;

        DeviceViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.list_item);
            text1 = view.findViewById(R.id.list_item_text1);
            text2 = view.findViewById(R.id.list_item_text2);
        }

        @SuppressLint("MissingPermission")
        void setupView(BluetoothDevice device) {
                text1.setText(device.getName());
                text2.setText(device.getAddress());
                layout.setOnClickListener(view -> openModeSelectionActivity(device.getName(), device.getAddress()));
        }
    }

    // A class to adapt our list of devices to the RecyclerView
    //定义用于将数据与 ViewHolder 视图相关联的 Adapter

    private class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {
        private BluetoothDevice[] deviceList = new BluetoothDevice[0];

        @NotNull
        @Override
        //onCreateViewHolder()：每当 RecyclerView 需要创建新的 ViewHolder 时，它都会调用此方法。
        //此方法会创建并初始化 ViewHolder 及其关联的 View，但不会填充视图的内容，因为 ViewHolder 此时尚未绑定到具体数据。
        public DeviceViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        }

        @Override
        //onBindViewHolder()：RecyclerView 调用此方法将 ViewHolder 与数据相关联。
        // 此方法会提取适当的数据，并使用该数据填充 ViewHolder 的布局。
        // 例如，如果 RecyclerView 显示的是一个名称列表，该方法可能会在列表中查找适当的名称，并填充 ViewHolder 的 TextView widget。
        @SuppressLint("MissingPermission")
        public void onBindViewHolder(@NotNull DeviceViewHolder holder, int position) {
            holder.setupView(deviceList[position]);
        }

        @Override
        //getItemCount()：RecyclerView 调用此方法来获取数据集的大小。
        //例如，在通讯簿应用中，这可能是地址总数。RecyclerView 使用此方法来确定什么时候没有更多的列表项可以显示。
        public int getItemCount() {
            return deviceList.length;
        }

        @SuppressLint("NotifyDataSetChanged")
        void updateList(Collection<BluetoothDevice> deviceList) {
            this.deviceList = deviceList.toArray(new BluetoothDevice[0]);
            notifyDataSetChanged();
        }
    }
}
