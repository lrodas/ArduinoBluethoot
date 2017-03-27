package com.cycsystemsgt.arduinobluethoot;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cycsystemsgt.arduinobluethoot.Util.Util;

import java.util.ArrayList;

import static android.R.attr.action;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_INTERNET = 1;
    private static final int ACCESS_NETWORK_STATE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private Button btnBuscarDispositivo;
    private BluetoothAdapter bAdapter;
    private ArrayList<BluetoothDevice> arrayDevices;
    // Instanciamos un BroadcastReceiver que se encargara de detectar cuando
    // un dispositivo es descubierto.
    private BroadcastReceiver bReceiver;
    private ListView lvDispositivos;
    private Button btnBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.checkForPermission(MainActivity.this, Manifest.permission.INTERNET, PERMISSION_INTERNET);

        btnBuscarDispositivo = (Button)findViewById(R.id.btnBuscarDispositivo);
        btnBluetooth  = (Button)findViewById(R.id.btnBluetooth);
        lvDispositivos = (ListView)findViewById(R.id.lvDispositivos);

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bAdapter == null){
            btnBluetooth.setEnabled(false);
            return;
        }

        if(bAdapter.isEnabled()){
            btnBluetooth.setText(R.string.btn_desactivar_bluetooth);
        }else{
            btnBluetooth.setText(R.string.btn_activar_bluetooth);
        }

        bReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                final String action = intent.getAction();

                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                    final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                    switch (estado){
                        // Apagado
                        case BluetoothAdapter.STATE_OFF:{
                            ((Button)findViewById(R.id.btnBluetooth)).setText(R.string.btn_activar_bluetooth);
                            break;
                        }
                        // Encendido
                        case BluetoothAdapter.STATE_ON:{
                            ((Button)findViewById(R.id.btnBluetooth)).setText(R.string.btn_desactivar_bluetooth);

                            Intent discoberableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            discoberableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                            startActivity(discoberableIntent);

                            break;
                        }
                        default:
                            break;
                    }
                }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                    // Cada vez que se descubra un nuevo dispositivo por Bluetooth, se ejecutara
                    // este fragmento de codigo

                    if(arrayDevices == null) arrayDevices = new ArrayList<BluetoothDevice>();

                    // Extraemos el dispositivo del intent mediante la clave BluetoothDevice.EXTRA_DEVICE
                    BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Añadimos el dispositivo al array
                    arrayDevices.add(dispositivo);

                    // Le asignamos un nOmbre del estilo NombreDispositivo [00:11:22:33:44]
                    String descripcionDispositivo = dispositivo.getName() + " [" + dispositivo.getAddress() + "]";

                    // Mostramos que hemos encontrado el dispositivo por el Toast
                    Toast.makeText(getBaseContext(), getString(R.string.DetectadoDispositivo) + ": " + descripcionDispositivo, Toast.LENGTH_SHORT).show();
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    // Codigo que se ejecutara cuando el Bluetooth finalice la busqueda de dispositivos.

                    ArrayAdapter arrayAdapter = new BluetoothDeviceArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2, arrayDevices);
                    lvDispositivos.setAdapter(arrayAdapter);
                    Toast.makeText(MainActivity.this, "Fin de la busqueda", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnBuscarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayDevices != null) arrayDevices.clear();

                // Comprobamos si existe un descubrimiento en curso. En caso afirmativo, se cancela.
                if(bAdapter.isDiscovering()) bAdapter.cancelDiscovery();

                // Iniciamos la busqueda de dispositivos y mostramos el mensaje de que el proceso ha comenzado
                if(bAdapter.startDiscovery()) {
                    Toast.makeText(MainActivity.this, "Iniciando búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Error al iniciar búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBluetooth.setOnClickListener(this);
    }

    private void registrarEventosBluetooth(){
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        this.registerReceiver(bReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBluetooth:
                if(bAdapter.isEnabled()){
                    bAdapter.disable();
                }else{
                    Intent enabledBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enabledBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            default:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){

                }else{

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(bReceiver);
    }
}
