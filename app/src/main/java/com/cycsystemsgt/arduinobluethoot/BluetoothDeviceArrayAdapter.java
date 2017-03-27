package com.cycsystemsgt.arduinobluethoot;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by angel on 26/03/2017.
 */

public class BluetoothDeviceArrayAdapter extends ArrayAdapter {

    private List<BluetoothDevice> deviceList; // Contendra el listado de dispositivos
    private Context context;

    public BluetoothDeviceArrayAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, List<BluetoothDevice> objects) {
        super(context, textViewResourceId, objects);

        deviceList = objects;
        this.context = context;
    }

    @Override
    public int getCount(){
        if(deviceList != null)
            return deviceList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position){
        return (deviceList  == null? null : deviceList.get(position));
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent){
        if(deviceList == null || context == null){
            return null;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elemento = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView nombre = (TextView) elemento.findViewById(android.R.id.text1);
        TextView tvDireccion = (TextView) elemento.findViewById(android.R.id.text2);

        BluetoothDevice dispositivo = (BluetoothDevice) getItem(posicion);
        if(dispositivo!=null){
            nombre.setText(dispositivo.getName());
            tvDireccion.setText(dispositivo.getAddress());
        }else{
            nombre.setText("Error");
        }
        return elemento;
    }


}
