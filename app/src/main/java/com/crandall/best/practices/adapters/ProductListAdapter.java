package com.crandall.best.practices.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crandall.best.practices.R;
import com.crandall.best.practices.models.Product;
import com.squareup.picasso.Picasso;

/**
 * The custom adapter to display each product row.
 */
public class ProductListAdapter extends ArrayAdapter {

    /**
     * The instance of the calling activity.
     */
    private Activity mContext;

    /**
     * The array of products to be bound to each row.
     */
    private Product[] mProducts;

    public ProductListAdapter(Activity context, Product[] products) {
        super(context, R.layout.listview_row_product, products);

        this.mContext = context;
        this.mProducts = products;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_row_product, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewProductName = (TextView) rowView.findViewById(R.id.ListViewRow_textViewProductName);
            viewHolder.imageViewProductImage = (ImageView) rowView.findViewById(R.id.ListViewRow_imageViewProductImage);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        String productName = mProducts[position].getProductName();

        holder.textViewProductName.setText(productName);

        Picasso.with(mContext).load(mProducts[position].getImageUrl())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.error)
                .into(holder.imageViewProductImage);

        return rowView;
    }

    public static class ViewHolder {
        public ImageView imageViewProductImage;
        public TextView textViewProductName;

    }

}

