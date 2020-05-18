package com.example.stockmanager.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stockmanager.Model.product;
import com.example.stockmanager.UI.table;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, table.DB_NAME, null, table.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + table.TABLE_NAME + "("
                + table.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + table.KEY_NAME + " TEXT,"
                + table.KEY_QUANTITY + " INTEGER,"
                + table.KEY_SIZE + " TEXT,"
                + table.KEY_DESCRIPTION + " TEXT,"
                + table.KEY_DATE + " LONG);";

        db.execSQL(CREATE_PRODUCT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + table.TABLE_NAME);
        onCreate(db);
    }


    public void addproduct(product product) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(table.KEY_NAME, product.getProductName());
        values.put(table.KEY_QUANTITY, product.getProductQuantity());
        values.put(table.KEY_SIZE, product.getProductSize());
        values.put(table.KEY_DESCRIPTION, product.getProductDesc());
        values.put(table.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert("product", null, values);


        db.close();

        //Log.d("row", "addproduct: " + rowID);


    }


    public product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table.TABLE_NAME, new String[]{
                        table.KEY_ID,
                        table.KEY_NAME,
                        table.KEY_QUANTITY,
                        table.KEY_SIZE,
                        table.KEY_DESCRIPTION},
                table.KEY_ID + "=?",
                new
                        String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        product product = new product();
        product.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(table.KEY_ID))));
        product.setProductName(cursor.getString(cursor.getColumnIndex(table.KEY_NAME)));
        product.setProductQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(table.KEY_QUANTITY))));
        product.setProductSize(cursor.getString(cursor.getColumnIndex(table.KEY_SIZE)));
        product.setProductDesc(cursor.getString(cursor.getColumnIndex(table.KEY_DESCRIPTION)));

        DateFormat dateFormat = DateFormat.getDateInstance();
        String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(table.KEY_DATE))).getTime());

        product.setProductAddDate(date);
        return product;
    }


    public List<product> getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<product> productList = new ArrayList<>();

        Cursor cursor = db.query(table.TABLE_NAME, new String[]{
                        table.KEY_ID,
                        table.KEY_NAME,
                        table.KEY_QUANTITY,
                        table.KEY_SIZE,
                        table.KEY_DESCRIPTION,
                        table.KEY_DATE},
                null, null, null, null,
                table.KEY_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                product product = new product();
                product.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(table.KEY_ID))));
                product.setProductName(cursor.getString(cursor.getColumnIndex(table.KEY_NAME)));
                product.setProductQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(table.KEY_QUANTITY))));
                product.setProductSize(cursor.getString(cursor.getColumnIndex(table.KEY_SIZE)));
                product.setProductDesc(cursor.getString(cursor.getColumnIndex(table.KEY_DESCRIPTION)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(table.KEY_DATE))).getTime());

                product.setProductAddDate(date);

                productList.add(product);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return productList;
    }

    public int updateProduct(product product) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(table.KEY_ID, product.getId());
        values.put(table.KEY_NAME, product.getProductName());
        values.put(table.KEY_QUANTITY, product.getProductQuantity());
        values.put(table.KEY_SIZE, product.getProductSize());
        values.put(table.KEY_DESCRIPTION, product.getProductDesc());
        values.put(table.KEY_DATE, java.lang.System.currentTimeMillis());

        return db.update(table.TABLE_NAME, values, table.KEY_ID + "=?", new String[]{
                String.valueOf(product.getId())});

    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(table.TABLE_NAME, table.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getAllProductCount() {

        String queryCount = " SELECT * FROM " + table.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryCount, null);

        return cursor.getCount();
    }
}
