package com.crandall.best.practices.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.crandall.best.practices.R;
import com.squareup.picasso.Picasso;

/**
 * Displays the details of a given product. (Image, Name, and Description)
 */
public class ProductDetailsActivity extends ActionBarActivity {

    /**
     * The imageView for the productImage loaded by Picasso.
     */
    private ImageView mProductImage;

    /**
     * The textView for the productName.
     */
    private TextView mProductName;

    /**
     * The textView for the productDescription.
     */
    private TextView mProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mProductImage = (ImageView) findViewById(R.id.ProductDetails_imageViewProductImage);
        mProductName = (TextView) findViewById(R.id.ProductDetails_textViewProductName);
        mProductDescription = (TextView) findViewById(R.id.ProductDetails_textViewProductDescription);

        Bundle extras = getIntent().getExtras();
        displayProductData(extras);

    }

    /**
     * Displays the product data from a given Bundle.
     *
     * @param extras the bundle containing the product data.
     */
    private void displayProductData(Bundle extras) {
        if (extras != null) {
            Picasso.with(this).load(extras.getString(ProductsActivity.PRODUCT_IMAGE_URL))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.error)
                    .into(mProductImage);
            if (extras.getString(ProductsActivity.PRODUCT_NAME) != null) {
                mProductName.setText(extras.getString(ProductsActivity.PRODUCT_NAME));
            } else {
                mProductName.setText(getResources().getString(R.string.ProductDetailsActivity_missingProductName));
            }
            if (extras.getString(ProductsActivity.PRODUCT_DESCRIPTION) != null) {
                mProductDescription.setText(extras.getString(ProductsActivity.PRODUCT_DESCRIPTION));
            } else {
                mProductDescription.setText(getResources().getString(R.string.ProductDetailsActivity_missingProductDescription));
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}

