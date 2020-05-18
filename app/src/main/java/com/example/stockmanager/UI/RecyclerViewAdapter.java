package com.example.stockmanager.UI;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmanager.Data.DatabaseHandler;
import com.example.stockmanager.MainActivity;
import com.example.stockmanager.Model.product;
import com.example.stockmanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<product> productList;
    private RecyclerViewAdapter adapter;
    private View v;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView nameDetails;
    private TextView quanDetails;
    private TextView sizeDetails;
    private TextView descDetails;

    private TextView nameUpdate;
    private  TextView quanUpdate;
    private  TextView sizeUpdate;
    private TextView descUpdate;
    private TextView updateBtn;
    private TextView cancelUpBtn;

    public RecyclerViewAdapter(Context context, List<product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.row_list, parent, false);
        adapter = new RecyclerViewAdapter(context, productList);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list,parent,false);

        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position) {
        final product product = productList.get(position);
        holder.proName.setText(product.getProductName());
        holder.proQuan.setText(MessageFormat.format("Quantity: {0}", String.valueOf(product.getProductQuantity())));
        holder.proAddDate.setText(MessageFormat.format("Date: {0}", product.getProductAddDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final product product = productList.get(position);
                productDetails(v, product);

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product product = productList.get(position);

                delete(product);



            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final product product = productList.get(position);
                builder = new AlertDialog.Builder(v.getContext());

                v = LayoutInflater.from(context).inflate(R.layout.update_product, null);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();

                nameUpdate = v.findViewById(R.id.nameETUpdate);
                quanUpdate = v.findViewById(R.id.quantityETUpdate);
                sizeUpdate = v.findViewById(R.id.sizeETUpdate);
                descUpdate = v.findViewById(R.id.descriptionETUpdate);
                updateBtn = v.findViewById(R.id.updateBtn);
                cancelUpBtn = v.findViewById(R.id.cancelUpdateBtn);

                nameUpdate.setText(product.getProductName().trim());
                quanUpdate.setText(String.valueOf(product.getProductQuantity()).trim());
                sizeUpdate.setText(product.getProductSize());
                descUpdate.setText(product.getProductDesc());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update(product,v);
                    }
                });

                cancelUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



            }
        });
    }

    private void update(product product, View v) {
        DatabaseHandler db = new DatabaseHandler(context);

        if(nameUpdate.getText().toString().trim().isEmpty()) {
            nameUpdate.setError("Enter Product Name");
            Snackbar.make(v, "Empty Field", Snackbar.LENGTH_LONG).show();
        }
        else if(quanUpdate.getText().toString().trim().isEmpty()) {
            quanUpdate.setError("Enter Product Quantity");
            Snackbar.make(v, "Empty Field", Snackbar.LENGTH_LONG).show();
        }

        else if(!nameUpdate.getText().toString().isEmpty() && !quanUpdate.getText().toString().isEmpty()){
            product.setProductName(nameUpdate.getText().toString().trim());
            product.setProductQuantity(Integer.parseInt(quanUpdate.getText().toString().trim()));
            product.setProductSize(sizeUpdate.getText().toString().trim());
            product.setProductDesc(descUpdate.getText().toString().trim());

            db.updateProduct(product);
            dialog.dismiss();
            notifyDataSetChanged();


        }
        else{
            Snackbar.make(v, "Empty Field", Snackbar.LENGTH_LONG).show();
            return;
        }



    }

    private void productDetails(View v, product product) {

        builder = new AlertDialog.Builder(v.getContext());

        v = LayoutInflater.from(context).inflate(R.layout.product_details, null);
        builder.setView(v);
        dialog = builder.create();


        dialog.setButton(dialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        nameDetails = v.findViewById(R.id.nameETdetails);
        quanDetails = v.findViewById(R.id.quantityETdetails);
        sizeDetails = v.findViewById(R.id.sizeETdetails);
        descDetails = v.findViewById(R.id.descriptionETdetails);

        nameDetails.setText(product.getProductName());
        quanDetails.setText(String.valueOf(product.getProductQuantity()));
        sizeDetails.setText(product.getProductSize());
        descDetails.setText(product.getProductDesc());
        dialog.show();


    }


    private void delete(product product) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteProduct(product.getId());
        productList.remove(product);
        notifyDataSetChanged();
        if (db.getAllProductCount() < 1) {
            Intent i = new Intent(context, MainActivity.class);

            context.startActivity(i);

        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView proName;
        private TextView proQuan;
        private TextView proAddDate;
        private Button editBtn;
        private Button deleteBtn;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            proName = itemView.findViewById(R.id.p_name);
            proQuan = itemView.findViewById(R.id.p_quantity);
            proAddDate = itemView.findViewById(R.id.date);


            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }

    }

    public void filterList(ArrayList<product> filteredList){
        productList=filteredList;
        notifyDataSetChanged();
    }

}
