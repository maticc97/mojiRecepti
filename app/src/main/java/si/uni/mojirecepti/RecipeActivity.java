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
        id = getIntent().getStringExtra("ID");
        myDb = new DatabaseHelper(this);

        ListView lvItems = (ListView) findViewById(R.id.ingredientsList);

        getData();

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
        Cursor cursor = myDb.getRecipe(id);
        cursor.moveToFirst();
        title = cursor.getString(1);
        category = cursor.getString(2);
        ingredients = cursor.getString(3);
        String [] test = ingredients.split(",");
        for (String ingredient : test) {
            String fixed = ingredient.replaceAll("[\\[\\]]", "");
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
