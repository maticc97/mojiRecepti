package si.uni.mojirecepti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "AddActivity";
    DatabaseHelper myDb;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> data = new ArrayList<String>();
    private TextView headerTitle;
    public String recipeCategory = "all";
    public String currentRecipeId;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //drawer layout
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    boolean doubleBackToExitPressedOnce = false;

    AlertDialog.Builder builder;

    ImageButton preklici, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //baza - kliče konstruktor tega classa, kjer kreiramo bazo in tabelo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);

        //nastavbimo meni
        drawerLayout= findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        headerTitle  = headerView.findViewById(R.id.nav_header);
       // System.out.println(headerTitle.getText());
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        myDb = new DatabaseHelper(this);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
        }

        delete = findViewById(R.id.odstranisest);
        preklici = findViewById(R.id.preklici_btn);


    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
        drawerLayout.closeDrawers();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    //to je sam primer kako daš funkcije na gumb v meniju, samo vstaviš notri v case - vsi casi so dodani
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //System.out.println(item.getItemId());
        switch (item.getItemId()){
            case R.id.vsiRecepti_menuItem:
                recipeCategory = "all";
                navigationView.setCheckedItem(R.id.vsiRecepti_menuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.predjed_menuItem:
                recipeCategory = "Predjed";
                navigationView.setCheckedItem(R.id.predjed_menuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.glavne_jedi_MenuItem:
                recipeCategory = "Glavna jed";
                navigationView.setCheckedItem(R.id.glavne_jedi_MenuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.sladice_menuItem:
                recipeCategory = "Sladica";
                navigationView.setCheckedItem(R.id.sladice_menuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.ostalo_menuItem:
                recipeCategory = "Ostalo";
                navigationView.setCheckedItem(R.id.ostalo_menuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.onas_menuItem:
                navigationView.setCheckedItem(R.id.onas_menuItem);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Onas_fragment()).commit();
                drawerLayout.closeDrawers();
        }
        return false;
    }

    public void openAllRecipesLayout(View view) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new RecipeList_fragment()).commit();
    }

    public void openAddRecipeLayout(View view) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new addRecept_fragment()).commit();
    }

    public void openRecipeLayout(String id) {
        currentRecipeId = id;
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new Recipe_fragment()).commit();
    }


}

