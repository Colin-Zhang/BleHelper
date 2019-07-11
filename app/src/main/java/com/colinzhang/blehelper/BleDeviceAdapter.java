package com.colinzhang.blehelper;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

public class BleDeviceAdapter extends RecyclerView.Adapter<BleDeviceAdapter.ViewHolder> implements View.OnClickListener {
    private List<BluetoothDevice> devices;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onclick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BleDeviceAdapter(List<BluetoothDevice> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
        return new BleDeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.deviceName.setText(devices.get(i).getName());
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onclick((Integer) v.getTag());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;

        ViewHolder(View view) {
            super(view);
            deviceName = view.findViewById(R.id.deviceName);
        }
    }

}
