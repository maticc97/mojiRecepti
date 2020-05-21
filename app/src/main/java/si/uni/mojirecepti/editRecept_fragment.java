package si.uni.mojirecepti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class editRecept_fragment extends Fragment {

    private DatabaseHelper myDb;

    private String dobiIme, dobiKategorijo, dobiPostopek, kategorija;
    private EditText novRecept, postopekPolje;
    private String id;
    private RadioGroup kategorije;
    EditText title;

    Recipe_fragment recipe_fragment;

    ImageButton preklici, posodobi;
    private TextView dogodek;

    private RadioButton predjed, glavna_jed, sladica, ostalo;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_add_recipe,container,false);

        myDb = new DatabaseHelper(getContext());

        dogodek=view.findViewById(R.id.naslovna_vrstica);
        dogodek.setText("Uredi recept");
        id = getArguments().getString("id");
        dobiIme = getArguments().getString("imeRecepta");
        dobiKategorijo = getArguments().getString("kategorija");
        dobiPostopek = getArguments().getString("postopek");
        dobiIme = getArguments().getString("imeRecepta");

        novRecept = view.findViewById(R.id.imeRecepta);
        kategorija = dobiKategorijo;

        kategorije = view.findViewById(R.id.radio_gumbi);
        predjed = view.findViewById(R.id.kategorijaPredjed);
        glavna_jed = view.findViewById(R.id.kategorijaGlavna);
        sladica= view.findViewById(R.id.kategorijaSladica);
        ostalo = view.findViewById(R.id.kategorijaOstalo);

        preklici = view.findViewById(R.id.preklici_btn);
        posodobi = view.findViewById(R.id.shrani);

        postopekPolje = view.findViewById(R.id.postopek_polje);
        postopekPolje.setText(dobiPostopek);

        novRecept.setText(dobiIme);

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
        onSaveButtonclick(view);



        return view;
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
                Toast.makeText(getContext(), kategorija, Toast.LENGTH_SHORT).show();
            }
        });



    }

    //posodobimo recept.
    public void onSaveButtonclick(View view){
        posodobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe_fragment = new Recipe_fragment();
                myDb.updateItem(id,novRecept.getText().toString(),kategorija,postopekPolje.getText().toString());
                Toast.makeText(getContext(), "Recept uspešno posodobljen", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, recipe_fragment).commit();
            }
        });
    }

    public void onCancelButtonClick(View view){
        Toast.makeText(getContext(), "Preklicano", Toast.LENGTH_SHORT).show();
    }
}
