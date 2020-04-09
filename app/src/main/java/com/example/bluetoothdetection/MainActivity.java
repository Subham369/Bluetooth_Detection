package com.example.bluetoothdetection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_ENABLE_DISCOVER=1;
    TextView status,pairedId;
    ImageView bluetoothIcon;
    Button blueOn,blueOff,discover,pairedDevice;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status=findViewById(R.id.status);
        pairedId=findViewById(R.id.pairedId);
        bluetoothIcon=findViewById(R.id.bluetoothIcon);
        blueOn=findViewById(R.id.blueOn);
        blueOff=findViewById(R.id.blueOff);
        discover=findViewById(R.id.discover);
        pairedDevice=findViewById(R.id.pairedDevice);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter==null)
        {
            status.setText("Bluetooth is not available");
        }
        else
        {
            status.setText("Bluetooth is available");
        }

        if (bluetoothAdapter.isEnabled())
        {
            bluetoothIcon.setImageResource(R.drawable.ic_blue_on);
        }
        else
        {
            bluetoothIcon.setImageResource(R.drawable.ic_blue_off);
        }

        blueOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled())
                {
                    showToast("Bluetooth ON");
                    Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                    status.setText("Bluetooth is available");
                }
                else
                {
                    showToast("Bluetooth is already ON");
                }

            }
        });

        blueOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.disable();
                    showToast("Turning bluetooth OFF");
                    pairedId.setText("");
                    status.setText("Bluetooth is unavailable");
                }

                else
                {
                    showToast("bluetooth is already OFF");
                }
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isDiscovering())
                {
                    showToast("Making your device discoverable");
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_DISCOVER);
                }
            }
        });

        pairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled())
                {
                    pairedId.setText("Paired Devices");
                    Set<BluetoothDevice> devices=bluetoothAdapter.getBondedDevices();

                    for (BluetoothDevice device:devices)
                    {
                        pairedId.append("\n Device - "+device.getName()+","+device);
                    }
                }
                else
                {
                    showToast("Turn on your bluetooth to get the paired devices");
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode)
        {
            case REQUEST_ENABLE_BT:
                if (requestCode==RESULT_OK) {
                    bluetoothIcon.setImageResource(R.drawable.ic_blue_on);
                    showToast("Bluetooth is ON");
                }
                else
                {
                    showToast("Couldn't on bluetooth");
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
