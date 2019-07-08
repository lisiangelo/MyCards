package it.j940549.mycards.SQLite;

/**
 * Created by J940549 on 22/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by J940549 on 22/04/2017.
 */

public class DBLayer {

    private static final String DATABASE_NAME = "my_reg_elettronico.db";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private  static Context ourContext;
    private SQLiteDatabase ourDatabase;
    //private static Crypto crypto;

    public DBLayer(Context c){
        this.ourContext = c;
    }

    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //  crypto=new Crypto(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {

                db.execSQL("CREATE TABLE IF NOT EXISTS tableCards (" +
                        " nr_barcode INTEGER PRIMARY KEY , " +
                        "nome_card TEXT, path_image_front TEXT, path_image_retro TEXT);");


            }catch (SQLException ex){
                Toast.makeText(ourContext, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS tableCards;");


            onCreate(db);
        }
    }


    public DBLayer open() throws SQLException {
        this.ourHelper = new DbHelper(ourContext);
        this.ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.ourHelper.close();
    }



    public Cursor  getAllCards(){

        Cursor c = ourDatabase.rawQuery("select * from tableCards",null);

        return c;

    }


    public Cursor getCard(String nr_barcode ){
            nr_barcode="\""+nr_barcode+"\"";


            Cursor c= ourDatabase.rawQuery("select * from tableCards where nr_barcode= "+nr_barcode,null);
            Log.i("getCard x ..",nr_barcode);
            return c;
    }

    public Cursor getCard_forName(String name ){
        name="\"%"+name+"%\"";


        Cursor c= ourDatabase.rawQuery("select * from tableCards where nome_card like "+name,null);
        Log.i("getCard x name..",name);
        return c;
    }

    public boolean inserisciNewCard(String nr_barcode,String nome_card, String path_image_front, String path_image_retro){
        long barcode=Long.parseLong(nr_barcode);
        nome_card="\""+nome_card+"\"";
        path_image_front="\""+path_image_front+"\"";
        path_image_retro="\""+path_image_retro+"\"";

        boolean c = false;
        String Query="insert into tableCards (nr_barcode,nome_card,path_image_front, path_image_retro)" +
                "values ("+barcode+","+nome_card+","+path_image_front+","+path_image_retro+");";
        Log.i("query",Query);
        try{
                    ourDatabase.execSQL(Query);
        c=true;
            Log.i("query succesfully",""+c);
        }catch (Exception e){
            c=false;
            Log.i("query errore",e.toString());
        }

        return c;
    }



    public int modificaCard(String nr_barcode,String nomeCard, String pathImageFront, String pathImageRetro,
                                        String pathImageBarcode){
    //    String passwordCrypto=crypto.encrypt(password);

        ContentValues contentValues= new ContentValues();
        contentValues.put("nome_card",nomeCard);
        contentValues.put("path_image_front",pathImageFront);
        contentValues.put("path_image_retro",pathImageRetro);
        contentValues.put("path_image_barcode",pathImageBarcode);
        int c=-1;

        try{
            c=ourDatabase.update("tableCards",contentValues,"nr_barcode="+"\""+nr_barcode+"\"",null);

        }catch (Exception e){
            Log.d("errore modicaCard  ",e.toString());

        }

        return c;
    }


    public int deleteCard(String nr_barcode){
    nr_barcode="\""+nr_barcode+"\"";
    int res=ourDatabase.delete("tableCards", "nr_barcode="+nr_barcode,null);
    //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
    return res;
}


}
