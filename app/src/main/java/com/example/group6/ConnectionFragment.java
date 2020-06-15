package com.example.group6;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Set;

public class ConnectionFragment extends Fragment {

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    TextView labelSelectedDevice;
    TextView labelCurrentDevice;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BA = BluetoothAdapter.getDefaultAdapter();
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);

        if (BA != null)
            pairedDevices = BA.getBondedDevices();

        ListView pairedDevicesList = (ListView)view.findViewById(R.id.pairedDevicesList);
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName() + " MAC: " + bt.getAddress());
        final ArrayAdapter adapter = new  ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);

        pairedDevicesList.setAdapter(adapter);

        labelSelectedDevice = (TextView)view.findViewById(R.id.textview_selected_device_mac);
        labelCurrentDevice = (TextView)view.findViewById(R.id.textview_current_device_mac);

        pairedDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String macAddress = selectedItem.substring(selectedItem.indexOf(":") + 2);
                labelSelectedDevice.setText(macAddress);
            }
        });

        view.findViewById(R.id.button_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ConnectionFragment.this)
                        .navigate(R.id.action_ConnectionFragment_to_MainMenuFragment);
            }
        });

        view.findViewById(R.id.button_use_selected_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labelCurrentDevice.setText(labelSelectedDevice.getText());
            }
        });
    }
}
