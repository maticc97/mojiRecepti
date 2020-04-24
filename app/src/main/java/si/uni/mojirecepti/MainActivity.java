package si.uni.mojirecepti;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    DatabaseHelper myDb;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //baza - kliče konstruktor tega classa, kjer kreiramo bazo in tabelo

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        addData();
    }

    private void addData() {
        Cursor cursor = myDb.recipeTitles();

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

    private class MyListAdapter extends ArrayAdapter<String> {
        Cursor cursor = myDb.recipeTitles();
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
                viewHolder.moreButton = (Button) convertView.findViewById(R.id.show_more_btn);
                viewHolder.deleteButton = (Button) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);
                    final String id = cursor.getString(0);
                    Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
            });
            mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
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
        Button moreButton;
        Button deleteButton;
    }

    public void onButtonShowPopupWindowClick(View view) {
        Intent intent = new Intent(this, addRecipeActivity.class);
        startActivity(intent);
    }

}

