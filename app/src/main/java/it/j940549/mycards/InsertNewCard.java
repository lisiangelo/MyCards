package it.j940549.mycards;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import it.j940549.mycards.Model.ImageBarcode;
import it.j940549.mycards.SQLite.DBLayer;

public class InsertNewCard extends AppCompatActivity {
  private Uri mCropImageUri;
  private CropImageView mCropImageView;
  private ImageButton imagebtn_front, imagebtn_retro, imagebtn_barcode;
  private   String path_image_front,path_image_retro;
  private EditText txt_nome_card;
  private TextView txt_nr_barcode, status_message;
  private Boolean request_From_Front=false;
  private Boolean request_From_Retro=false;
  static final int REQUEST_IMAGE_FRONT_CAPTURE = 1;
  static final int REQUEST_IMAGE_RETRO_CAPTURE = 2;
  static final int REQUEST_IMAGE_BARCODE_CAPTURE = 9001;
  private int codiceCard;
    private CompoundButton autoFocus;
    private CompoundButton useFlash;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_insert_new_card);
    imagebtn_front = findViewById(R.id.image_front);
    imagebtn_retro = findViewById(R.id.image_retro);
    imagebtn_barcode = findViewById(R.id.image_barcode);
    txt_nr_barcode=findViewById(R.id.nr_barcode);
    txt_nome_card=findViewById(R.id.nome_card);
    status_message=findViewById(R.id.status_message);
    mCropImageView = (CropImageView)  findViewById(R.id.CropImageView);
      autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
      useFlash = (CompoundButton) findViewById(R.id.use_flash);

    String barcode_data=txt_nr_barcode.getText().toString();
    caricaImageBarcode(barcode_data);

    codiceCard = getIntent().getExtras().getInt("codiceCard");

    imagebtn_front.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        request_From_Front=true;
        request_From_Retro=false;
        startActivityForResult(getPickImageChooserIntent(), 200);

      }
    });

    imagebtn_retro.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          request_From_Front=false;
          request_From_Retro=true;
        startActivityForResult(getPickImageChooserIntent(), 200);

      }
    });

    imagebtn_barcode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dispatchTakeBarcodeIntent(REQUEST_IMAGE_BARCODE_CAPTURE, codiceCard);
      }
    });



    setTitle("Insert new Cards");


  }


  /**
   * Crop the image and set it back to the  cropping view.
   */
  public void onCropImageClick(View view) {
      int width=((ImageView) findViewById(R.id.image_front)).getWidth();
      int height=((ImageView) findViewById(R.id.image_front)).getHeight();

      Bitmap cropped =  mCropImageView.getCroppedImage(width, height);
      OutputStream outputStream= null;
      File file=null;
    if (cropped != null){
      if(request_From_Front) {
          file = new File( getBaseContext().getExternalFilesDir(null), codiceCard+"-front.jpeg");
          ((ImageView) findViewById(R.id.image_front)).setImageBitmap(cropped);
          Uri fileUri = Uri.fromFile(file);
          path_image_front=fileUri.toString();
          galleryAddPic(fileUri );
      }
      if(request_From_Retro){
          file = new File( getBaseContext().getExternalFilesDir(null), codiceCard+"-retro.jpeg");
          ((ImageView) findViewById(R.id.image_retro)).setImageBitmap(cropped);
          Uri fileUri = Uri.fromFile(file);
          path_image_retro=fileUri.toString();
          galleryAddPic(fileUri );

      }
      try{
          outputStream= new FileOutputStream(file);
          cropped.compress(Bitmap.CompressFormat.JPEG,85,outputStream);
          outputStream.flush();
          outputStream.close();
      }catch (Exception e){
          Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
      }
        View layout_crop = findViewById(R.id.layout_crop);
      layout_crop.setVisibility(View.GONE);
        View layout_insert = findViewById(R.id.layout_insert);
        layout_insert.setVisibility(View.VISIBLE);

  }
  }

    private void galleryAddPic(Uri fileUri) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(fileUri);
       // Toast.makeText(this, "galleriaddpicc", Toast.LENGTH_SHORT).show();
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
  protected void onActivityResult(int  requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
        View layout_crop = findViewById(R.id.layout_crop);
        layout_crop.setVisibility(View.VISIBLE);
        View layout_insert = findViewById(R.id.layout_insert);
        layout_insert.setVisibility(View.GONE);

        Uri imageUri = getPickImageResultUri(data);

        // For API >= 23 we need to check specifically that we have permissions to read external storage,
        // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
        boolean requirePermissions = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                isUriRequiresPermissions(imageUri)) {

            requirePermissions = true;
            mCropImageUri = imageUri;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        if (!requirePermissions) {
            mCropImageView.setImageUriAsync(imageUri);
        }
    }
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(request_From_Front) {
                    ((ImageView) findViewById(R.id.image_front)).setImageURI(result.getUri());
                    Log.i("inser",result.getUri().getPath());
                }
                if(request_From_Retro){
                    ((ImageView) findViewById(R.id.image_retro)).setImageURI(result.getUri());
                    Log.i("inser",result.getUri().getPath());
                }
              //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                status_message.setText(R.string.barcode_success);
                txt_nr_barcode.setText(barcode.displayValue);
                caricaImageBarcode(txt_nr_barcode.getText().toString());
                Log.d("barcode", "Barcode read: " + barcode.displayValue);
            } else {
                status_message.setText(R.string.barcode_failure);
                Log.d("barcode", "No barcode captured, intent data is null");
            }
        } else {
            status_message.setText(String.format(getString(R.string.barcode_error),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        }

    }


  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      mCropImageView.setImageUriAsync(mCropImageUri);
    } else {
      Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
    }
  }

    /**
   * Create a chooser intent to select the  source to get image from.<br/>
   * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
   * All possible sources are added to the  intent chooser.
   */
  public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
    Uri outputFileUri =  getCaptureImageOutputUri();

    List<Intent> allIntents = new ArrayList<>();
    PackageManager packageManager =  getPackageManager();

// collect all camera intents
    Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
    for (ResolveInfo res : listCam) {
      Intent intent = new  Intent(captureIntent);
      intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
      intent.setPackage(res.activityInfo.packageName);
      if (outputFileUri != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if(request_From_Front) {
            path_image_front = outputFileUri.toString();
            Log.i("insetP", path_image_front);
        }
          if(request_From_Retro) {
              path_image_retro = outputFileUri.toString();
              Log.i("insetP", path_image_retro);
          }
      }
      allIntents.add(intent);
    }

// collect all gallery intents
    Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
    galleryIntent.setType("image/*");
    List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
    for (ResolveInfo res : listGallery) {
      Intent intent = new  Intent(galleryIntent);
      intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
      intent.setPackage(res.activityInfo.packageName);
      allIntents.add(intent);
    }

// the main intent is the last in the  list (fucking android) so pickup the useless one
    Intent mainIntent =  allIntents.get(allIntents.size() - 1);
    for (Intent intent : allIntents) {
      if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
        mainIntent = intent;
        break;
      }
    }
    allIntents.remove(mainIntent);

// Create a chooser from the main  intent
    Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

// Add all other intents
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

    return chooserIntent;
  }

  /**
   * Get URI to image received from capture  by camera.
   */
  private Uri getCaptureImageOutputUri() {
    Uri outputFileUri = null;
    File getImage = getExternalCacheDir();
    String codCard="";
      if(request_From_Front==true){
          codCard= codiceCard +"-front.jpg";
      }else{
          codCard= codiceCard +"-retro.jpg";
      }

      if (getImage != null) {
      outputFileUri = Uri.fromFile(new  File(getImage.getPath(), codCard));
    }
    return outputFileUri;
  }

  /**
   * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
   * Will return the correct URI for camera  and gallery image.
   *
   * @param data the returned data of the  activity result
   */
  public Uri getPickImageResultUri(Intent  data) {
    boolean isCamera = true;
    if (data != null && data.getData() != null) {
      String action = data.getAction();
      isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
    }
    return isCamera ?  getCaptureImageOutputUri() : data.getData();
  }

  /**
   * Test if we can open the given Android URI to test if permission required error is thrown.<br>
   */
  public boolean isUriRequiresPermissions(Uri uri) {
    try {
      ContentResolver resolver = getContentResolver();
      InputStream stream = resolver.openInputStream(uri);
      stream.close();
      return false;
    } catch (FileNotFoundException e) {
      if (e.getCause() instanceof ErrnoException) {
        return true;
      }
    } catch (Exception e) {
    }
    return false;
  }



  //////////





  private void caricaImageBarcode(String barcode_data){
//  Toast.makeText(this, "barcodedata.."+barcode_data, Toast.LENGTH_SHORT).show();
  Bitmap bitmap=null;
  try {
    //123456789128
    bitmap = new ImageBarcode().genera(barcode_data, 600,300);
    imagebtn_barcode.setImageBitmap(bitmap);

  } catch (Exception e) {
    e.printStackTrace();
  }

}

  private void dispatchTakeBarcodeIntent(int actionCode, int codiceCard) {

  Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
        intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

  startActivityForResult(intent, actionCode);
  }



  private void inserisciNewCards(){
    String nr_barcode=txt_nr_barcode.getText().toString();
  //  Toast.makeText(this, "barcode.."+nr_barcode, Toast.LENGTH_SHORT).show();
      String nome_card=txt_nome_card.getText().toString();
Log.i("myCardsIns", nome_card+"-"+path_image_front+"-"+path_image_retro);
        if (!nome_card.equals("")&&!nr_barcode.equals("")&&!path_image_front.equals("")&&!path_image_retro.equals("")) {

      DBLayer dbLayer=new DBLayer(this);
      dbLayer.open();

      boolean inserito=dbLayer.inserisciNewCard(nr_barcode,nome_card, path_image_front, path_image_retro);
      if(inserito) {
        Toast.makeText(this, "Nuova card inserita", Toast.LENGTH_SHORT).show();
                finish();


      }else {
        Toast.makeText(this, "Nessun nuovo dato inserito", Toast.LENGTH_SHORT).show();
      }
      dbLayer.close();
    }else{
      Toast.makeText(this, "completa  i dati essenziali", Toast.LENGTH_SHORT).show();
    }

  }

  }

