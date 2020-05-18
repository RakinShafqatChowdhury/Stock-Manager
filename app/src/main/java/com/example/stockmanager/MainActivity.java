package com.example.stockmanager;

import android.content.Intent;
import android.os.Bundle;

import com.example.stockmanager.Data.DatabaseHandler;
import com.example.stockmanager.Model.product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Handler;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText quantityET;
    private EditText sizeET;
    private EditText desET;
    private Button saveBtn;
    private Button cancelBtn;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);
        divertActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productAddPopup();

            }
        });
    }

    private void divertActivity() {
        if (databaseHandler.getAllProductCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

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

        builder.setView(v);
        dialog = builder.create();
        dialog.show();

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


    }

    private void saveProduct(View view) {

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


        Snackbar.make(view, "Product saved!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
