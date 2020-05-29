package si.uni.mojirecepti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Recept.db";
    public static final String TABELA = "Seznam_receptov";
    public static final String ID_ = "ID";
    public static final String COL_2 = "IME";
    public static final String COL_3 = "KATEGORIJA";
    public static final String COL_4 = "SESTAVINE";
    public static final String COL_5 = "POSTOPEK";
    public static final String COL_6 = "SLIKA";



//tukaj ustvarimo bazo z DATABASE_NAME imenom
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABELA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,IME TEXT,KATEGORIJA TEXT,SESTAVINE TEXT, POSTOPEK TEXT, SLIKA TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABELA);
        onCreate(db);
    }

    public boolean insertData(String ime, String kategorija, ArrayList<String> sestavine, String postopek, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //key, vsebina

        if(ime.equals("") || kategorija==null || sestavine==null || postopek.equals("")){
            return false;
        }
        contentValues.put(COL_2, ime);
        contentValues.put(COL_3, kategorija);
        contentValues.put(COL_4, String.valueOf(sestavine));
        contentValues.put(COL_5, postopek);
        contentValues.put(COL_6, image);
        //vrne -1 če ni okej
        long result = db.insert(TABELA, null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }


    //Item is a class representing any item with id, name and description
    public boolean updateItem(String id, String imeRecepta, String kategorija, String postopek, ArrayList<String> sestavine) {
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println(sestavine);

        if(imeRecepta.equals("") || kategorija==null|| sestavine.size()<=0 ||postopek.equals("")){
            return false;
        }
        //key, vsebina
        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, imeRecepta);
            contentValues.put(COL_3, kategorija);
            contentValues.put(COL_4, String.valueOf(sestavine));
            contentValues.put(COL_5, postopek);
            //vrne -1 če ni okej

            db.update(TABELA, contentValues, "ID = ?", new String[]{id});
            return true;
        }
    }

    public Cursor recipeTitles(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select ID, IME from " + TABELA;

        if (!category.equals("all")) {
            query = "Select ID, IME from "+TABELA+" where KATEGORIJA = '"+category+"'";
        }

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getRecipe(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABELA+" where ID = "+id;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void deleteRecipe(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABELA, "ID = ?", new String[] {id});

    }
}
