package si.uni.mojirecepti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Recept.db";
    public static final String TABELA = "Seznam_receptov";
    public static final String ID_ = "ID";
    public static final String COL_2 = "IME";
    public static final String COL_3 = "KATEGORIJA";
    public static final String COL_4 = "SESTAVINE";
    public static final String COL_5 = "POSTOPEK";



//tukaj ustvarimo bazo z DATABASE_NAME imenom
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABELA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,IME TEXT,KATEGORIJA TEXT,SESTAVINE TEXT, POSTOPEK TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABELA);
        onCreate(db);
    }

    public boolean insertData(String ime, String kategorija, ArrayList<String> sestavine, String postopek){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //key, vsebina
        contentValues.put(COL_2, ime);
        contentValues.put(COL_3, kategorija);
        contentValues.put(COL_4, String.valueOf(sestavine));
        contentValues.put(COL_5, postopek);
        //vrne -1 če ni okej
        long result = db.insert(TABELA, null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABELA;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
