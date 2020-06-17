package com.example.group6;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ConnectionFragment extends Fragment {

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket mBTSocket = null;
    private Handler mHandler;
    TextView labelSelectedDevice;
    TextView labelCurrentDevice;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random"


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

        labelSelectedDevice = (TextView) view.findViewById(R.id.textview_selected_device_mac);
        labelCurrentDevice = (TextView) view.findViewById(R.id.textview_current_device_mac);

        if (BA != null) {
            pairedDevices = BA.getBondedDevices();

            ListView pairedDevicesList = (ListView) view.findViewById(R.id.pairedDevicesList);
            ArrayList list = new ArrayList();

            for (BluetoothDevice bt : pairedDevices)
                list.add(bt.getName() + " MAC: " + bt.getAddress());
            final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);

            pairedDevicesList.setAdapter(adapter);

            pairedDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    final String macAddress = selectedItem.substring(selectedItem.indexOf(":") + 2);
                    labelSelectedDevice.setText(macAddress);

                    //BluetoothDevice bluetoothDevice = BA.getRemoteDevice(macAddress);
                    // Initiate a connection request in a separate thread
                    //ConnectingThread t = new ConnectingThread(bluetoothDevice, BA);
                    //t.start();

//                new Thread()
//                {
//                    public void run() {
//                        boolean fail = false;
//
//                        BluetoothDevice device = BA.getRemoteDevice(macAddress);
//
//                        try {
//                            mBTSocket = createBluetoothSocket(device);
//                        } catch (IOException e) {
//                            fail = true;
//                        }
//                        // Establish the Bluetooth socket connection.
//                        try {
//                            mBTSocket.connect();
//                        } catch (IOException e) {
//                            try {
//                                fail = true;
//                                mBTSocket.close();
//                                mHandler.obtainMessage(3, -1, -1)
//                                        .sendToTarget();
//                            } catch (IOException e2) {
//                                //insert code to deal with this
//                            }
//                        }
//                        if(fail == false) {
//                            ConnectedThread mConnectedThread = new ConnectedThread(mBTSocket);
//                            mConnectedThread.start();
//
//                            mConnectedThread.write("1");
//                            labelSelectedDevice.setText(Integer.toString(mConnectedThread.read()));
//                        }
//                    }
//                }.start();
                }
            });
        }

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

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }
}
