package it.j940549.mycards;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.io.IOException;

import it.j940549.mycards.Model.ImageBarcode;
import it.j940549.mycards.Model.Row_Card;
import it.j940549.mycards.SQLite.DBLayer;

public class Card_detail extends AppCompatActivity  {

  ImageView image_front, image_retro, image_barcode;
  String path_image_front, path_image_retro;
  TextView txt_nome_card, txt_nr_barcode, status_message;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card_detail);

    image_front = findViewById(R.id.image_front);
    image_retro = findViewById(R.id.image_retro);
    image_barcode = findViewById(R.id.image_barcode);
    txt_nr_barcode = findViewById(R.id.nr_barcode);
    txt_nome_card = findViewById(R.id.nome_card);
    status_message = findViewById(R.id.status_message);

    final String nr_barcode = getIntent().getExtras().getString("nr_barcode");

    caricaDati(nr_barcode);

    if (!path_image_front.equals("")) {
      Uri uri=Uri.parse(path_image_front);
      /*holder.image_front.setImageURI(uri);

      Bitmap bitmaporig = BitmapFactory.decodeFile(path_image_front);
      Bitmap bitmap = Bitmap.createScaledBitmap(bitmaporig, 200, 150, true);//BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
      image_front.setImageBitmap(bitmap);
            */

      image_front.setImageURI(uri);
    }

    if (!path_image_retro.equals("")) {
      /*Bitmap bitmaporig = BitmapFactory.decodeFile(path_image_retro);
      Bitmap bitmap = Bitmap.createScaledBitmap(bitmaporig, 200, 150, true);//BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

      image_retro.setImageBitmap(bitmap);
      */
      Uri uri=Uri.parse(path_image_retro);
      image_retro.setImageURI(uri);

    }

    caricaImageBarcode(nr_barcode);

    image_barcode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        vaiaBarcode_Detail(nr_barcode);

      }
    });
    image_retro.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        vaiaBarcode_Detail(nr_barcode);

      }
    });

  setTitle("DETTAGLIO CARD");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.bottom_navigation_detail_card, menu);

    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // action with ID action_refresh was selected
      case R.id.action_detailbarcode:
        vaiaBarcode_Detail(txt_nr_barcode.getText().toString());
        break;
        case android.R.id.home:
            onBackPressed();
            break;

        default:
        break;
    }

    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }


  private void vaiaBarcode_Detail(String nr_barcode) {

  Intent vaiaBarcode_Detail = new Intent(this, Barcode_detail.class);
        vaiaBarcode_Detail.putExtra("nr_barcode",nr_barcode);
      vaiaBarcode_Detail.putExtra("image_retro",path_image_retro);

  startActivity(vaiaBarcode_Detail);
//  finish();
}

  private void caricaImageBarcode(String barcode_data){
    //Toast.makeText(this, "barcodedata.."+barcode_data, Toast.LENGTH_SHORT).show();
    Bitmap bitmap=null;
    try {
      //123456789128
      bitmap = new ImageBarcode().genera(barcode_data, 700,300);
      image_barcode.setImageBitmap(bitmap);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }




  private void caricaDati(String nr_barcode){

//    Toast.makeText(this, "barcode.."+nr_barcode, Toast.LENGTH_SHORT).show();

    if (!nr_barcode.equals("")) {
      DBLayer dbLayer=null;
      try{
      dbLayer=new DBLayer(this);
      dbLayer.open();

      Cursor cursor = dbLayer.getCard(nr_barcode);

      if (cursor.getCount() > 0) {
        cursor.moveToPosition(0);
        do {
          txt_nr_barcode.setText(cursor.getString(0));
          txt_nome_card.setText(cursor.getString(1).toUpperCase());
          path_image_front=cursor.getString(2);
          path_image_retro=cursor.getString(3);
          Log.i("myCards", txt_nome_card.getText().toString()+"-"+path_image_front+"-"+path_image_retro);

        } while (cursor.moveToNext());
      }
    }catch(SQLException ex){
      Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
    dbLayer.close();


  }else{
      Toast.makeText(this, "il nr_barcode è nullo", Toast.LENGTH_SHORT).show();
    }

  }

}

