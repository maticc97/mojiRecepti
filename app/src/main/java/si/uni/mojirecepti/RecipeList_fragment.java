package si.uni.mojirecepti;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeList_fragment extends Fragment {

    private DatabaseHelper myDb;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> data = new ArrayList<String>();
    private String recipeCategory;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recipe_list,container,false);
        myDb = new DatabaseHelper(getContext());

        lvItems = view.findViewById(R.id.lvItems);

        if (((MainActivity) getActivity()) != null) {
            recipeCategory = ((MainActivity) getActivity()).recipeCategory;
        }
        addData(recipeCategory);
        return view;
    }

    private void addData(String category) {
        //kazalec na vse shranjene recepte v DatabaseHelper je funkcija recipeTitles, ki vrne kazalec na recepte odvisno od kategorije
        Cursor cursor = myDb.recipeTitles(category);

        if (cursor.getCount() < 0) {
            Toast.makeText(getContext(), "Nimate shranjenih receptov", Toast.LENGTH_SHORT).show();
        } else {
            //se premikamo po en kazalec naprej
            while (cursor.moveToNext()) {
                //v seznam vseh jedi (data) se doda ime jedi -> stolpec 1 je ime, stolpec 0 je id
                data.add(cursor.getString(1));
            }

            itemsAdapter = new MyListAdapter(getContext(), R.layout.list_item, data);
            //set cursor zato, da je seznam jedi v myListAdapter pravilen, odvisen od kategorije
            ((MyListAdapter) itemsAdapter).setCursor(myDb.recipeTitles(category));
            lvItems.setAdapter(itemsAdapter);
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        //kazalec na vse shranjene recepte v DatabaseHelper je funkcija recipeTitles, ki vrne kazalec na recepte odvisno od kategorije
        Cursor cursor = myDb.recipeTitles(recipeCategory);
        private int layout;
        private List<String> mObjects;
        MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        //če je druga kategorija, se je seznam posodobil da je pravilen
        public void setCursor(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.moreButton = (ImageButton) convertView.findViewById(R.id.show_more_btn);
                viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //cursor premaknemo na kliknjeni element seznama, stolpec 0 je id kliknjene jedi
                    cursor.moveToPosition(position);
                    final String id = cursor.getString(0);
                    ((MainActivity)getActivity()).openRecipeLayout(id);
                }
            });
            mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("OPOZORILO!");
                    builder.setMessage("Ali ste prepričani, da želite izbrisati recept?");
                    builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cursor.moveToPosition(position);
                            final String id = cursor.getString(0);
                            myDb.deleteRecipe(id);
                            data.remove(position);
                            itemsAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public static class ViewHolder {
         TextView title;
         ImageButton moreButton;
         ImageButton deleteButton;
    }
}
