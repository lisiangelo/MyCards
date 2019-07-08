package it.j940549.mycards;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.j940549.mycards.Model.ImageBarcode;
import it.j940549.mycards.SQLite.DBLayer;

public class Barcode_detail extends AppCompatActivity {
  private ScaleGestureDetector mScaleGestureDetector;
  private float mScaleFactor= 1.0f;
  ImageView image_barcode;
  TextView txt_nome_card, txt_nr_barcode, status_message;
  String path_imageretro;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_barcode_detail);

    image_barcode = findViewById(R.id.image_barcode);
    txt_nr_barcode = findViewById(R.id.nr_barcode);
    txt_nome_card = findViewById(R.id.nome_card);
    status_message = findViewById(R.id.status_message);

    final String nr_barcode = getIntent().getExtras().getString("nr_barcode");
    path_imageretro=getIntent().getExtras().getString("image_retro");;
    caricaImageBarcode(nr_barcode);

mScaleGestureDetector=new ScaleGestureDetector(this,new ScaleListener());

    setTitle("DETTAGLIO BARCODE");
  }
  public boolean onTouchEvent (MotionEvent motionEvent){
    mScaleGestureDetector.onTouchEvent(motionEvent);
    return true;
  }

  @Override
  public void onBackPressed() {
/*    Intent ritornaCardDetail= new Intent(this,Card_detail.class);
    ritornaCardDetail.putExtra("nr_barcode", txt_nr_barcode.getText().toString());
    startActivity(ritornaCardDetail);
    finish();*/
    this.onSuperBackPressed();

  }

  public  void onSuperBackPressed(){
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case android.R.id.home: {
        /*Intent ritornaCardDetail= new Intent(this,Card_detail.class);
        ritornaCardDetail.putExtra("nr_barcode", txt_nr_barcode.getText().toString());
        startActivity(ritornaCardDetail);
        finish();*/            onBackPressed();

        return true;
      }
    }
    return super.onOptionsItemSelected(menuItem);
  }

  private void caricaImageBarcode(String barcode_data){
  //  Toast.makeText(this, "barcodedata.."+barcode_data, Toast.LENGTH_SHORT).show();
    Bitmap bitmap=null;
    try {
      //123456789128
   //   bitmap = new ImageBarcode().genera(barcode_data, 600,300);
 //     image_barcode.setImageBitmap(bitmap);
      txt_nr_barcode.setText(barcode_data);


      if (!path_imageretro.equals("")) {
      /*Bitmap bitmaporig = BitmapFactory.decodeFile(path_image_retro);
      Bitmap bitmap = Bitmap.createScaledBitmap(bitmaporig, 200, 150, true);//BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

      image_retro.setImageBitmap(bitmap);
      */
        Uri uri=Uri.parse(path_imageretro);
        image_barcode.setImageURI(uri);

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

      @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector){
          mScaleFactor *=scaleGestureDetector.getScaleFactor();
          mScaleFactor=Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
          image_barcode.setScaleX(mScaleFactor);
          image_barcode.setScaleY(mScaleFactor);
          return true;
      }
}



}



