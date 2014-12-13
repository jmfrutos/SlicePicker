package info.androidhive.slidingmenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by frutos on 13/10/2014.
 */
public class SuscripcionesSQLiteHelper {
    public static final String KEY_ID = "_id";
    public static final String KEY_SUSCRIPCION = "suscripcion";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_MASINFO = "masinfo";
    public static final String TAG = "SuscripcionDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "SlicePickerAPP_BD";
    private static final String SQLITE_TABLE = "tSuscripcion";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_SUSCRIPCION + "," +
                    KEY_DESCRIPCION + "," +
                    KEY_MASINFO + "," +
                    " UNIQUE (" + KEY_SUSCRIPCION + "));";

    public static class  DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db){
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL((DATABASE_CREATE));
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualizando base de datos de la version " + oldVersion + " a " +
                    newVersion + ", la cual destruira toda la información antigua.");
            db.execSQL("DROP TABLE IF EXIST " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public SuscripcionesSQLiteHelper(Context ctx){
        this.mCtx = ctx;
    }

    public SuscripcionesSQLiteHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public long crearSuscripcion(String suscripcion, String descripcion, String masinfo){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUSCRIPCION, suscripcion);
        initialValues.put(KEY_DESCRIPCION, descripcion);
        initialValues.put(KEY_MASINFO, masinfo);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }
    public boolean borrarSuscripciones() {
        int  doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }
    public Cursor fetchSuscripcionesPorNombre(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID,
                            KEY_SUSCRIPCION, KEY_DESCRIPCION, KEY_MASINFO},
                    null, null, null, null, null);
        } else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ID,
                            KEY_SUSCRIPCION, KEY_DESCRIPCION, KEY_MASINFO},
                    KEY_SUSCRIPCION + " like '%" + inputText + "%'", null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllSuscripciones() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID, KEY_SUSCRIPCION, KEY_DESCRIPCION, KEY_MASINFO}, null, null, null, null, null);

        if (mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteContacto(String id) throws SQLException {
        String[] args = new String[] {id};
        mDb.delete(SQLITE_TABLE, KEY_ID+"=?", args);
    }

    public void insertSuscripcionesMuestra() {

        crearSuscripcion("Mamíferos","Un monton de animales","ANIMALES");
        crearSuscripcion("Flores Rojas","Un monton de flores rojas","PLANTAS");
        crearSuscripcion("Pescaooos","Pececillos","ANIMALES ACUATICOS");
        crearSuscripcion("Fotos JFP","Fotografo Juan Fernando Perez","FOTOGRAFOS");
        crearSuscripcion("Atenas","Fotos del mejor Clima","LUGARES");

    }
}
