package com.example.lenovo.connetcttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 07-10-2017.
 */

public class AdapterWiFi extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Modelvalue> modelvalues;
    private OnClickWiFi onClickListener;

    public AdapterWiFi(Context context, ArrayList<Modelvalue> modelvalues) {
        this.context = context;
        this.modelvalues = modelvalues;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row,parent,false);
        return new ViewWiFi(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Modelvalue modelist = modelvalues.get(position);
        final ViewWiFi viewWiFi = (ViewWiFi) holder;
        viewWiFi.list_Name.setText(modelist.getName());
        viewWiFi.list_WPA.setText(modelist.getWPA());
        viewWiFi.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickConnect(v,modelist.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelvalues.size();
    }
    public class ViewWiFi extends RecyclerView.ViewHolder{

        private TextView list_Name,list_WPA;
        private LinearLayout list_item;
        public ViewWiFi(View itemView) {
            super(itemView);
            this.list_Name = (TextView) itemView.findViewById(R.id.list_Name);
            this.list_WPA = (TextView) itemView.findViewById(R.id.list_WPA);
            this.list_item = (LinearLayout) itemView.findViewById(R.id.list_item);
        }
    }
    public void SetOnItemClickListener(OnClickWiFi itemClickListener) {
        this.onClickListener = itemClickListener;
    }
    public interface OnClickWiFi{
        public void onClickConnect(View view,String Name);
    }
}
