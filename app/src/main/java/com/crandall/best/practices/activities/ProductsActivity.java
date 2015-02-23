package com.crandall.best.practices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crandall.best.practices.R;
import com.crandall.best.practices.adapters.ProductListAdapter;
import com.crandall.best.practices.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Displays a list of test products, each with an image loaded by Picasso.
 */
public class ProductsActivity extends ActionBarActivity {

    /**
     * The key for the image URL.
     */
    public static final String PRODUCT_IMAGE_URL = "productImageUrl";
    /**
     * The key for the name of the test product.
     */
    public static final String PRODUCT_NAME = "productName";
    /**
     * The key for the description of the test product.
     */
    public static final String PRODUCT_DESCRIPTION = "productDescription";

    /**
     * The array of products displayed in the activity.
     */
    private Product[] mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ListView productList = (ListView) findViewById(R.id.ProductActivity_listViewProducts);

        mProducts = populateProductsFromJson();

        ProductListAdapter adapter = new ProductListAdapter(this, mProducts);

        productList.setOnItemClickListener(new ProductListClickListener());

        productList.setAdapter(adapter);
    }

    private class ProductListClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ProductsActivity.this, ProductDetailsActivity.class);
            intent.putExtra(PRODUCT_IMAGE_URL, mProducts[position].getImageUrl());
            intent.putExtra(PRODUCT_NAME, mProducts[position].getProductName());
            intent.putExtra(PRODUCT_DESCRIPTION, mProducts[position].getProductDescription());
            startActivity(intent);
        }
    }

    /**
     * Populates the product array from a JSON file stored in the assets directory.
     *
     * @return The populated product array.
     */
    private Product[] populateProductsFromJson() {
        Product[] products = null;
        String jsonString;
        try {

            InputStream inputStream = getAssets().open("ProductJSON.json");
            byte[] buffer = new byte[8192];

            inputStream.read(buffer);
            inputStream.close();

            jsonString = new String(buffer);

            JSONObject jsonObj = new JSONObject(jsonString);

            JSONArray productArray = jsonObj.getJSONArray("Product");

            products = new Product[productArray.length()];

            for (int i = 0; i < productArray.length(); i++) {
                JSONObject productData = productArray.getJSONObject(i);

                String imageUrl = productData.getString(PRODUCT_IMAGE_URL);
                String name = productData.getString(PRODUCT_NAME);
                String description = productData.getString(PRODUCT_DESCRIPTION);

                products[i] = new Product(imageUrl, name, description);
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return products;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}


