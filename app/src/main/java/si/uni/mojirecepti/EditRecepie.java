package si.uni.mojirecepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditRecepie extends AppCompatActivity {


    DatabaseHelper myDb;
    String dobiIme, dobiKategorijo, dobiPostopek, kategorija;
    EditText novRecept, postopekPolje;
    ImageButton save;
    RadioGroup kategorije;
    String id;

    RadioButton predjed, glavna_jed, sladica, ostalo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        myDb = new DatabaseHelper(this);

        id = getIntent().getStringExtra("id");
        dobiIme = getIntent().getStringExtra("imeRecepta");
        dobiKategorijo = getIntent().getStringExtra("kategorija");
        dobiPostopek = getIntent().getStringExtra("postopek");

        novRecept = (EditText)findViewById(R.id.imeRecepta);
        save = findViewById(R.id.shrani);
        kategorija=dobiKategorijo;

        //get radiobuttons
        kategorije = findViewById(R.id.radio_gumbi);
        predjed = findViewById(R.id.kategorijaPredjed);
        glavna_jed = findViewById(R.id.kategorijaGlavna);
        sladica=findViewById(R.id.kategorijaSladica);
        ostalo = findViewById(R.id.kategorijaOstalo);

        postopekPolje = findViewById(R.id.postopek_polje);
        postopekPolje.setText(dobiPostopek);

        novRecept.setText(dobiIme);
        //set checked
        System.out.println(id + dobiPostopek + dobiKategorijo + dobiIme);

        switch(dobiKategorijo){
            case "Predjed":
                predjed.setChecked(true);
                break;

            case "Glavna jed":
                glavna_jed.setChecked(true);
                break;

            case "Sladica":
                sladica.setChecked(true);
                break;

            case "Ostalo":
                ostalo.setChecked(true);
                break;
        }

        GetRadioButtonData();
    }

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
                Toast.makeText(EditRecepie.this, kategorija, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSaveButtonclick(View view){
        myDb.updateItem(id,novRecept.getText().toString(),kategorija,postopekPolje.getText().toString());
        Toast.makeText(EditRecepie.this, "Recept uspešno dodan", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditRecepie.this, MainActivity.class);
        startActivity(intent);
    }

    public void onCancelButtonClick(View view){
        Toast.makeText(EditRecepie.this, "Preklicano", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditRecepie.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
