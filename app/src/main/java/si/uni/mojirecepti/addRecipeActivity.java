package si.uni.mojirecepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class addRecipeActivity extends AppCompatActivity {

    EditText imeRecepta, opisPostopka;
    RadioGroup kategorije;
    RadioButton kat1, kat2, kat3, kat4;

    //TO DO
    String kategorija;
    ImageButton btnShrani;
    DatabaseHelper myDb;

    EditText napisiSestavino;
    ImageButton dodaj;
    ImageButton preklici, delete;
    ListView lv;
    Layout a;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        myDb = new DatabaseHelper(this);

        imeRecepta = findViewById(R.id.imeRecepta);
        kategorije = findViewById(R.id.radio_gumbi);
        kat1 = findViewById(R.id.kategorijaPredjed);
        kat2 = findViewById(R.id.kategorijaGlavna);
        kat3 = findViewById(R.id.kategorijaSladica);
        kat4 = findViewById(R.id.kategorijaOstalo);

        opisPostopka = findViewById(R.id.postopek_polje);
        btnShrani = findViewById(R.id.shrani);
        napisiSestavino = findViewById(R.id.sestavina_add);
        dodaj = findViewById(R.id.btnSestavine);
        lv = findViewById(R.id.listView_lv);

        delete = findViewById(R.id.odstranisest);
        preklici = findViewById(R.id.preklici_btn);



        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(addRecipeActivity.this, R.layout.list_item_layout, R.id.lst_txt, arrayList);
        lv.setAdapter(adapter);
        dodajSestavino();
        AddData();
        GetRadioButtonData();
        preklici();

    }




    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void AddData(){
        imeRecepta.getText().toString();
        opisPostopka.getText().toString();
        btnShrani.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(imeRecepta.getText().toString(),
                                kategorija,arrayList,
                                opisPostopka.getText().toString());
                        if(isInserted){
                            Toast.makeText(addRecipeActivity.this, "Recept uspešno dodan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(addRecipeActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                        else{
                            Toast.makeText(addRecipeActivity.this, "Prosimo vpiši vse podatke", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    //dobiti vrednost obkljukanega radiobuttna
    public void GetRadioButtonData() {
        kategorije.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.kategorijaPredjed:
                        kategorija = "Predjed";
                        break;

                    case R.id.kategorijaGlavna:
                        kategorija = "Glavna jed";
                        break;

                    case R.id.kategorijaSladica:
                        kategorija = "Sladica";
                        break;

                    case R.id.kategorijaOstalo:
                        kategorija = "Ostalo";
                        break;
                }
                Toast.makeText(addRecipeActivity.this, kategorija, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //za dodajanje posamezne sestavine v text polje
    public void dodajSestavino(){
        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = napisiSestavino.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
                napisiSestavino.setText("");
            }
        });
    }

   public void preklici(){
        preklici.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(addRecipeActivity.this, "Dodajanje recepta preklicano", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(addRecipeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
   }


}

