package si.uni.mojirecepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class RecipeActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    String title;
    String id;
    String category;
    String ingredients;
    String process;

    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        //pridobimo dodatno poslan parameter ID, ki je id našega recepta
        id = getIntent().getStringExtra("ID");
        myDb = new DatabaseHelper(this);

        ListView lvItems = (ListView) findViewById(R.id.ingredientsList);

        //funkcija ki pridobi potrebne podatke iz baze
        getData();

        //v seznam sestavin dodamo sestavine, sestavine se nastavijo v data znotraj funkcije getData(), z ukazom data.add(fixed)
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lvItems.setAdapter(itemsAdapter);

        TextView recipeTitle = (TextView)findViewById(R.id.recipeTitle);
        TextView recipeCategory = (TextView)findViewById(R.id.recipeCategory);
        TextView recipeProcess = (TextView)findViewById(R.id.recipeProcess);
        recipeTitle.setText(title);
        recipeCategory.setText(category);
        recipeProcess.setText(process);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    public void onButtonBackClick(View view) {
        Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
