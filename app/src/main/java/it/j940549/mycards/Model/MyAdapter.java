package it.j940549.mycards.Model;


import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import it.j940549.mycards.Card_detail;
import it.j940549.mycards.R;
import it.j940549.mycards.SQLite.DBLayer;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Row_Card> listCards;
    private Activity myActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{
        public TextView nomeCard, nr_barcode;
        public ImageView image_front;

        public MyViewHolder(View view) {
            super(view);
            nomeCard= (TextView) view.findViewById(R.id.nome_card);
            nr_barcode = (TextView) view.findViewById(R.id.nr_barcode);
            image_front=(ImageView)view.findViewById(R.id.image_front);

        }

      /*  @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "This is my Toast message!",
                    Toast.LENGTH_LONG).show();
        }*/
    }


    public MyAdapter(ArrayList<Row_Card> listCards, Activity myActivity) {
        this.listCards = listCards;
        this.myActivity=myActivity;

        Log.i("list cards","dataset.size--"+getItemCount());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_row_card, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nr_codbare= view.findViewById(R.id.nr_barcode);
                if(nr_codbare.getText().toString()!=null||!nr_codbare.getText().toString().equals("")) {
                  Intent vaiaCard_Detail = new Intent(view.getContext(), Card_detail.class);
                  vaiaCard_Detail.putExtra("nr_barcode", nr_codbare.getText().toString());
                  myActivity.startActivity(vaiaCard_Detail);

                }else{
                    Toast.makeText(myActivity, "errore su dettaglio", Toast.LENGTH_SHORT).show();
                }


            }
        });


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TextView nr_barcode= (TextView) view.findViewById(R.id.nr_barcode);

                apriPopup(view,nr_barcode.getText().toString());
                return true;
            }
        });


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Row_Card row_card= listCards.get(position);
        holder.nomeCard.setText(row_card.getNomeCard());
        holder.nr_barcode.setText(row_card.getNr_barcode());


      Uri imageUri = null;
      Bitmap bitmap=null;
      try {
Uri uri=Uri.parse(row_card.getPatch_Image_Front());
holder.image_front.setImageURI(uri);
/*        imageUri =Uri.parse("content:/"+row_card.getPatch_Image_Front());
                //Uri.fromFile( new File( row_card.getPatch_Image_Front()));

        Glide.with(myActivity)
          .load(imageUri )
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .skipMemoryCache(true)
          .into(holder.image_front);
        //bitmap= BitmapFactory.decodeStream(url.openStream());
*/
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    @Override
    public int getItemCount() {
        return listCards.size();
    }

    public void apriPopup(View v, final String nr_barcode){
        android.support.v7.widget.PopupMenu popupMenu=new android.support.v7.widget.PopupMenu(myActivity,v);
        MenuInflater menuInflater=popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DBLayer dbLayer=null;

                try {
                    dbLayer=new DBLayer(myActivity);
                    dbLayer.open();
                            dbLayer.deleteCard(nr_barcode);

                    ricreateActivity(nr_barcode);

                }catch (SQLException ex){
                    Toast.makeText(myActivity, ""+ex.toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                Toast.makeText(myActivity, "dato cancellato!", Toast.LENGTH_SHORT).show();


                dbLayer.close();
                try {
                    File file = new File(myActivity.getExternalFilesDir(null), nr_barcode+ ".jpg");
                    if (file != null) {
                        file.delete();
                        /*if(imageAggiunte.contains(nomeApp+".jpg")){
                            imageAggiunte.remove(nomeApp+".jpg");
                        }*/
                    }
                }catch (Exception e){
                    Toast.makeText(myActivity, e.toString(), Toast.LENGTH_SHORT).show();
                }

                return true;
            }

        });


        popupMenu.show();

    }

    public void ricreateActivity(String tipo_App){
        if(tipo_App.contains("-")){
            String[] split=tipo_App.split("-");
            tipo_App=split[0];
        }
//ricostruire il fragment con ele co cards
/*        Intent ricreaMainActivity=new Intent(myActivity,MainActivity.class);
        ricreaMainActivity.putExtra("user", user);
        ricreaMainActivity.putExtra("qualeFragment", tipo_App);
        ricreaMainActivity.putExtra("isffpp",MainActivity.isffpp);

        myActivity.startActivity(ricreaMainActivity);
        myActivity.finish();
*/
    }


}
