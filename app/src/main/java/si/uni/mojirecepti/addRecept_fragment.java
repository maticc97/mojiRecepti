package si.uni.mojirecepti;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class addRecept_fragment extends Fragment {

    EditText imeRecepta, opisPostopka;
    RadioGroup kategorije;
    RadioButton kat1, kat2, kat3, kat4;

    //TO DO
    String kategorija;
    ImageButton btnShrani;
    DatabaseHelper myDb;

    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 0;

    EditText napisiSestavino;
    ImageButton dodaj, capturePhoto;
    ImageButton preklici, delete;
    ListView lv;
    Layout a;
    ImageView dodajSliko;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_add_recipe,container,false);
        myDb = new DatabaseHelper(getContext());

        imeRecepta = view.findViewById(R.id.imeRecepta);
        kategorije = view.findViewById(R.id.radio_gumbi);
        kat1 = view.findViewById(R.id.kategorijaPredjed);
        kat2 = view.findViewById(R.id.kategorijaGlavna);
        kat3 = view.findViewById(R.id.kategorijaSladica);
        kat4 = view.findViewById(R.id.kategorijaOstalo);

        opisPostopka = view.findViewById(R.id.postopek_polje);
        btnShrani = view.findViewById(R.id.shrani);
        napisiSestavino = view.findViewById(R.id.sestavina_add);
        dodaj = view.findViewById(R.id.btnSestavine);
        lv = view.findViewById(R.id.listView_lv);

        dodajSliko = view.findViewById(R.id.dodajSliko);
        capturePhoto = view.findViewById(R.id.button_add_pic);

        delete = view.findViewById(R.id.odstranisest);
        preklici = view.findViewById(R.id.preklici_btn);

        arrayList = new ArrayList<String>();
        //adapter1 = new ArrayAdapter<String>(addRecipeActivity.this, R.layout.list_item_layout, R.id.lst_txt, arrayList);
        //lv.setAdapter(adapter1);

        adapter = new MyAdapter(getContext(), R.layout.list_item_layout, arrayList);
        lv.setAdapter(adapter);


        preklici();
        dodajSestavino();
        GetRadioButtonData();
        AddData();

        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

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
            addRecipeActivity.addViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                addRecipeActivity.addViewHolder viewHolder = new addRecipeActivity.addViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.lst_txt);
                viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.odstranisest);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (addRecipeActivity.addViewHolder) convertView.getTag();
            mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(position);
                    System.out.println(arrayList);
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    private void SelectImage(){
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if(items[which].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
                else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK) {
            if (resultCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                dodajSliko.setImageBitmap(bmp);
            } else if (resultCode == SELECT_FILE) {
                Uri selectedImgUri = data.getData();
                dodajSliko.setImageURI(selectedImgUri);
            }
        }
    }

    public static class addViewHolder {
        TextView title;
        ImageButton deleteButton;
    }

    public void dodajSestavino(){
        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = napisiSestavino.getText().toString();
                if(!result.equals("")) {
                    arrayList.add(result);
                    adapter.notifyDataSetChanged();
                    napisiSestavino.setText("");
                }
                else{
                    Toast.makeText(getActivity(), "Vnesite sestavino", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            }
        });
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
                            //TODO tukaj dodaj prehod na glavni fregment vsi recepti
                            ((MainActivity)getActivity()).openAllRecipesLayout(getView());
                            Toast.makeText(getActivity(), "Recept uspešno dodan", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Prosimo vpiši vse podatke", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void preklici(){
        preklici.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO prehod na fragment main
                ((MainActivity)getActivity()).openAllRecipesLayout(getView());
                Toast.makeText(getActivity(), "Dodajanje recepta preklicano", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
