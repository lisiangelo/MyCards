package it.j940549.mycards.Model;


import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import it.j940549.mycards.Card_detail;
import it.j940549.mycards.R;
import it.j940549.mycards.SQLite.DBLayer;
import it.j940549.mycards.Webview_depliant;


public class MyAdapterDepliant extends RecyclerView.Adapter<MyAdapterDepliant.MyViewHolder> {

    private ArrayList<Depliant> listDepliants;
    private Activity myActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{
        public ImageView image_Depliant;
        public TextView url, nome_depliant;

        public MyViewHolder(View view) {
            super(view);
            url= (TextView) view.findViewById(R.id.url_depliant);
            nome_depliant= (TextView) view.findViewById(R.id.nome_depliant);

            image_Depliant=(ImageView)view.findViewById(R.id.image_depliant);

        }

      /*  @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "This is my Toast message!",
                    Toast.LENGTH_LONG).show();
        }*/
    }


    public MyAdapterDepliant(ArrayList<Depliant> listDepliants, Activity myActivity) {
        this.listDepliants= listDepliants;
        this.myActivity=myActivity;

        Log.i("list depliant","dataset.size--"+getItemCount());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_depliant, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt_url= view.findViewById(R.id.url_depliant);
                if(txt_url.getText().toString()!=null||!txt_url.getText().toString().equals("")) {
                  Intent vaiaDepliant= new Intent(view.getContext(), Webview_depliant.class);
                    vaiaDepliant.putExtra("url", txt_url.getText().toString());
                  myActivity.startActivity(vaiaDepliant);

                }else{
                    Toast.makeText(myActivity, "errore su dettaglio", Toast.LENGTH_SHORT).show();
                }


            }
        });



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Depliant depliant= listDepliants.get(position);
        switch (depliant.getNomeDepliant()){
            case "tuodi":
                holder.image_Depliant.setImageResource(R.drawable.logo_tuodi);
            break;
            case "eurospin":
                holder.image_Depliant.setImageResource(R.drawable.eurospin_logo);
                break;
            case "conad zodiaco":
                holder.image_Depliant.setImageResource(R.drawable.conad_superstore);
                break;
            case "conad anzio":
                holder.image_Depliant.setImageResource(R.drawable.conad1);
                break;
            case "risparmiocasa":
                holder.image_Depliant.setImageResource(R.drawable.risparmio_casa);
                break;
            case "acqua&sapone":
                holder.image_Depliant.setImageResource(R.drawable.logoacquasapone);
                break;
            case "unieuro":
                holder.image_Depliant.setImageResource(R.drawable.logo_unieuro);
                break;
            case "euronics":
                holder.image_Depliant.setImageResource(R.drawable.logo_euronics);
                break;
            case "mediaworld":
                holder.image_Depliant.setImageResource(R.drawable.logo_mediaworld);
                break;

        }
        holder.url.setText(depliant.getUrl());
        holder.nome_depliant.setText(depliant.getNomeDepliant());



    }

    @Override
    public int getItemCount() {
        return listDepliants.size();
    }


    }



