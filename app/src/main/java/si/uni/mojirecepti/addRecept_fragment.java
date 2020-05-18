package si.uni.mojirecepti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import androidx.fragment.app.Fragment;

public class addRecept_fragment extends Fragment {

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
    ArrayAdapter<String> adapter1;
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_recipe,container,false);
    }
}
