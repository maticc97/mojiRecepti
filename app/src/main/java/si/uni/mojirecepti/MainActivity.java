package si.uni.mojirecepti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    //drawer layout
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //baza - kliče konstruktor tega classa, kjer kreiramo bazo in tabelo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //nastavbimo meni
        drawerLayout= findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        myDb = new DatabaseHelper(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        addData();
    }

    private void addData() {
        Cursor cursor = myDb.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nimate shranjenih receptov", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                data.add(cursor.getString(1));
            }

            itemsAdapter = new MyListAdapter(this, R.layout.list_item, data);
            lvItems.setAdapter(itemsAdapter);
        }
    }


    //to je sam primer kako daš funkcije na gumb v meniju, samo vstaviš notri v case - vsi casi so dodani
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        System.out.println(item.getItemId());
        switch (item.getItemId()){
            case R.id.vsiRecepti_menuItem:
                break;
            case R.id.predjed_menuItem:
                Toast.makeText(MainActivity.this, "Preklopi na stran predjedi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, addRecipeActivity.class);
                startActivity(intent);
                break;

            case R.id.glavne_jedi_MenuItem:
                Toast.makeText(MainActivity.this, "Preklopi na stran glavne jedi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sladice_menuItem:
                Toast.makeText(MainActivity.this, "Preklopi na stran sladice", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    itemsAdapter.notifyDataSetChanged();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public static class ViewHolder {
        TextView title;
        Button button;
    }

    public void onButtonShowPopupWindowClick(View view) {
        Intent intent = new Intent(this, addRecipeActivity.class);
        startActivity(intent);
    }

}

