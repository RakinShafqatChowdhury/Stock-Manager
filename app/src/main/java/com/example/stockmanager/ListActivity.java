package com.example.stockmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;

import com.example.stockmanager.Data.DatabaseHandler;
import com.example.stockmanager.Model.product;
import com.example.stockmanager.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHandler databaseHandler;
    private List<product> productList;
    private FloatingActionButton floatingActionButton;

    private EditText nameET;
    private EditText quantityET;
    private EditText sizeET;
    private EditText desET;
    private Button saveBtn;
    private Button cancelBtn;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               search(newText);

               return true;
           }
       });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_count) {
            Toast.makeText(this, "Total Product: "+databaseHandler.getAllProductCount(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search(String newText) {
        ArrayList<product> filteredList = new ArrayList<>();
        for(product products : productList){
            if(products.getProductName().toLowerCase().contains(newText)||
                    products.getProductName().toUpperCase().contains(newText)||
            products.getProductName().contains(newText)){
                filteredList.add(products);
            }
        }
        recyclerViewAdapter.filterList(filteredList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        recyclerView = findViewById(R.id.recyclerView);
        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = findViewById(R.id.floatAB);
        productList = new ArrayList<>();

        productList = databaseHandler.getAllProducts();


        recyclerViewAdapter = new RecyclerViewAdapter(this, productList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAddPopup();
            }
        });

    }

    private void productAddPopup() {
        builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.popup, null);

        nameET = v.findViewById(R.id.nameET);
        quantityET = v.findViewById(R.id.quantityET);
        sizeET = v.findViewById(R.id.sizeET);
        desET = v.findViewById(R.id.descriptionET);

        saveBtn = v.findViewById(R.id.saveBtn);
        cancelBtn = v.findViewById(R.id.canvelBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(nameET.getText().toString().trim().isEmpty()) {
                    nameET.setError("Enter Product Name");
                     Snackbar.make(v, "Empty Field", Snackbar.LENGTH_LONG).show();
                }
                else if(quantityET.getText().toString().trim().isEmpty()) {
                    quantityET.setError("Enter Product Quantity");
                     Snackbar.make(v, "Empty Field", Snackbar.LENGTH_LONG).show();
                }
                else if (!nameET.getText().toString().trim().isEmpty() && !quantityET.getText().toString().trim().isEmpty()) {
                    saveProduct(v);
                }



            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void saveProduct(View v) {
        product product = new product();

        String newProductName = nameET.getText().toString().trim();
        int newProductQuantity = Integer.parseInt(quantityET.getText().toString().trim());
        String newProductSize = sizeET.getText().toString().trim();
        String newProductDesc = desET.getText().toString().trim();

        product.setProductName(newProductName);
        product.setProductQuantity(newProductQuantity);
        product.setProductSize(newProductSize);
        product.setProductDesc(newProductDesc);

        databaseHandler.addproduct(product);


        Snackbar.make(v, "Product saved!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1000);
    }
}
