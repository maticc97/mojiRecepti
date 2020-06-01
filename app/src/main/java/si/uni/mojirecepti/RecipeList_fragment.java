package si.uni.mojirecepti;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeList_fragment extends Fragment {

    private DatabaseHelper myDb;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> data = new ArrayList<String>();
    private String recipeCategory;
    private List<String> imgUriStr = new ArrayList<String>();
    private Uri imgUri;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recipe_list,container,false);
        myDb = new DatabaseHelper(getContext());

        lvItems = view.findViewById(R.id.lvItems);

        //search
        EditText search = view.findViewById(R.id.searchFilter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (((MainActivity) getActivity()) != null) {
            recipeCategory = ((MainActivity) getActivity()).recipeCategory;
        }
        addData(recipeCategory);
        return view;
    }

    private void addData(String category) {
        //kazalec na vse shranjene recepte v DatabaseHelper je funkcija recipeTitles, ki vrne kazalec na recepte odvisno od kategorije
        final Cursor cursor = myDb.recipeTitles(category);

        if (cursor.getCount() < 0) {
            Toast.makeText(getContext(), "Nimate shranjenih receptov", Toast.LENGTH_SHORT).show();
        } else {
            //se premikamo po en kazalec naprej
            while (cursor.moveToNext()) {
                //v seznam vseh jedi (data) se doda ime jedi -> stolpec 1 je ime, stolpec 0 je id
                data.add(cursor.getString(1));
                imgUriStr.add(cursor.getString(2));
            }

            itemsAdapter = new MyListAdapter(getContext(), R.layout.list_item, data);
            //set cursor zato, da je seznam jedi v myListAdapter pravilen, odvisen od kategorije
            ((MyListAdapter) itemsAdapter).setCursor(myDb.recipeTitles(category));
            lvItems.setAdapter(itemsAdapter);

            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);
                    final String clickedRecipeId = cursor.getString(0);
                    ((MainActivity)getActivity()).openRecipeLayout(clickedRecipeId);
                }
            });
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
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.moreButton = (ImageButton) convertView.findViewById(R.id.show_more_btn);
                viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_btn);
                imgUri = Uri.parse(imgUriStr.get(position));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
                } catch (IOException e) {
                    imgUri = Uri.parse("android.resource://si.uni.mojirecepti/drawable/main_dish");
                }
                viewHolder.image.setImageURI(imgUri);
                viewHolder.image.setBackgroundColor(0x00000000);
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
                            imgUriStr.remove(position);
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
        ImageView image;
         TextView title;
         ImageButton moreButton;
         ImageButton deleteButton;
    }
}
