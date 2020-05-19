package si.uni.mojirecepti;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Recipe_fragment extends Fragment {

    private DatabaseHelper myDb;
    private String title;
    private String id;
    private String category;
    private String ingredients;
    private String process;
    private ArrayList<String> data = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_recipe,container,false);

        myDb = new DatabaseHelper(getContext());
        if (((MainActivity)getActivity()) != null) {
            id = ((MainActivity) getActivity()).currentRecipeId;
        }

        ListView lvItems = view.findViewById(R.id.ingredientsList);

        //funkcija ki pridobi potrebne podatke iz baze
        getData();

        //v seznam sestavin dodamo sestavine, sestavine se nastavijo v data znotraj funkcije getData(), z ukazom data.add(fixed)
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        lvItems.setAdapter(itemsAdapter);

        TextView recipeTitle = view.findViewById(R.id.recipeTitle);
        TextView recipeCategory = view.findViewById(R.id.recipeCategory);
        TextView recipeProcess = view.findViewById(R.id.recipeProcess);
        recipeTitle.setText(title);
        recipeCategory.setText(category);
        recipeProcess.setText(process);
        return view;
    }

    private void getData() {
        //v database helper je funkcija getRecipe, ki nam vrne recept odvisno od parametra id
        Cursor cursor = myDb.getRecipe(id);
        cursor.moveToFirst();
        //stolpec 0 je id, stolpec 1 je ime jedi, stolpec 2 je kategorija, stolpec 3 so sestavine, stolpec 4 je postopek
        title = cursor.getString(1);
        category = cursor.getString(2);
        ingredients = cursor.getString(3);
        //String sestavin ločimo po vejicah in jih shranimo v array stringov test
        String [] test = ingredients.split(",");
        //premikamo se sestavino po sestavino, ime trenutne sestavine se shrani v spremenljivko ingredient
        for (String ingredient : test) {
            //iz spremenljivke ingredient odstranimo morebitne oglate oklepaje
            String fixed = ingredient.replaceAll("[\\[\\]]", "");
            //končno ime sestavine spravimo v arraylist
            data.add(fixed);
        }
        process = cursor.getString(4);
    }
}
