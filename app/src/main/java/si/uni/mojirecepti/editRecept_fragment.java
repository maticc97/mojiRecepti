package si.uni.mojirecepti;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class editRecept_fragment extends Fragment {

    private DatabaseHelper myDb;

    private String dobiIme, dobiKategorijo, dobiPostopek, kategorija;
    private EditText novRecept, postopekPolje;
    private String id;
    private RadioGroup kategorije;
    private ArrayList<String> dobiSestavine;
    private ArrayAdapter<String> adapter;
    EditText title;
    EditText napisiSestavino;

    int heightpri;


    //Img URi
    Uri imgUri;
    String imgStrUri;

    ImageView dodajsliko;
    ImageButton dodaj;
    ListView sestavine;
    Recipe_fragment recipe_fragment;

    ImageButton preklici, posodobi;
    private TextView dogodek;

    private RadioButton predjed, glavna_jed, sladica, ostalo;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_recipe, container, false);

        myDb = new DatabaseHelper(getContext());



        dodaj = view.findViewById(R.id.btnSestavine);
        napisiSestavino = view.findViewById(R.id.sestavina_add);
        dogodek = view.findViewById(R.id.naslovna_vrstica);
        dogodek.setText("Uredi recept");
        id = getArguments().getString("id");
        dobiIme = getArguments().getString("imeRecepta");
        dobiKategorijo = getArguments().getString("kategorija");
        dobiPostopek = getArguments().getString("postopek");
        dobiIme = getArguments().getString("imeRecepta");
        dobiSestavine = getArguments().getStringArrayList("sestavine");

        dodajsliko = view.findViewById(R.id.dodajSliko);
        imgStrUri = getArguments().getString("imgUriStr");

        imgUri = Uri.parse(imgStrUri);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
        } catch (IOException e) {
            imgUri = Uri.parse("android.resource://si.uni.mojirecepti/drawable/main_dish");
        }
        dodajsliko.setImageURI(imgUri);
        dodajsliko.setBackgroundColor(0x00000000);

        novRecept = view.findViewById(R.id.imeRecepta);
        kategorija = dobiKategorijo;

        kategorije = view.findViewById(R.id.radio_gumbi);
        predjed = view.findViewById(R.id.kategorijaPredjed);
        glavna_jed = view.findViewById(R.id.kategorijaGlavna);
        sladica = view.findViewById(R.id.kategorijaSladica);
        ostalo = view.findViewById(R.id.kategorijaOstalo);

         sestavine = view.findViewById(R.id.listView_lv);

        heightpri = sestavine.getLayoutParams().height;

         //
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sestavine.getLayoutParams();
        params.height = sestavine.getLayoutParams().height+155*dobiSestavine.size();

        System.out.println(dobiSestavine.size());
        adapter = new MyAdapter(getContext(), R.layout.list_item_layout, dobiSestavine);
        sestavine.setAdapter(adapter);

        preklici = view.findViewById(R.id.preklici_btn);
        posodobi = view.findViewById(R.id.shrani);

        postopekPolje = view.findViewById(R.id.postopek_polje);
        postopekPolje.setText(dobiPostopek);

        novRecept.setText(dobiIme);

        switch (dobiKategorijo) {
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
        preklici();
        dodajSestavino();

        return view;
    }


    private class MyAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        private MyAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            addViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                addViewHolder viewHolder = new addViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.lst_txt);
                viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.odstranisest);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (addViewHolder) convertView.getTag();
            mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sestavine.getLayoutParams();
                    params.height = sestavine.getLayoutParams().height-heightpri;
                    dobiSestavine.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public static class addViewHolder {
        TextView title;
        ImageButton deleteButton;
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
                // Toast.makeText(getContext(), kategorija, Toast.LENGTH_SHORT).show();
            }
        });


    }

    //posodobimo recept.
    public void onSaveButtonclick(View view) {
        posodobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe_fragment = new Recipe_fragment();
                boolean isUpdated = myDb.updateItem(id, novRecept.getText().toString(), kategorija, postopekPolje.getText().toString(), dobiSestavine);
                if (isUpdated) {
                    Toast.makeText(getContext(), "Recept uspešno posodobljen", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, recipe_fragment).commit();
                } else {
                    Toast.makeText(getContext(), "Prosimo zapolnite vsa polja", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void dodajSestavino() {
        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = napisiSestavino.getText().toString();
                if (!result.equals("")) {
                    dobiSestavine.add(result);
                    adapter.notifyDataSetChanged();
                    napisiSestavino.setText("");
                    if(dobiSestavine.size()!=0){
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sestavine.getLayoutParams();
                        params.height = sestavine.getLayoutParams().height+heightpri;
                    }
                } else {
                    Toast.makeText(getActivity(), "Vnesite sestavino", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void preklici(){
        preklici.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openAllRecipesLayout(getView());
                Toast.makeText(getActivity(), "Recept ni bil posodobljen", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
