package com.appsnipp.androidproject.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.androidproject.R;
import com.appsnipp.androidproject.model.Product;

import java.util.List;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<Product> shoppingList;
    private Activity mActivity;



    public void setList(List<Product> products){
        shoppingList=products;
        notifyDataSetChanged();
    }

    public ShoppingListAdapter(Activity activity, List<Product> list){
        this.mActivity = activity;
        this.shoppingList = list;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {

        Product product = shoppingList.get(position);

        holder.itemTV.setText(product.getProductName());
        holder.quantityTV.setText(product.getProductQuantity());
        holder.userNameTV.setText(product.userName);
    }

    @Override
    public int getItemCount() {
        if(shoppingList!=null)
            return shoppingList.size();
        else return 0;
    }

    public void setData(List<Product> list){
        this.shoppingList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTV;
        private TextView quantityTV;
        private TextView userNameTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            itemTV = itemView.findViewById(R.id.tv_item_product);
            quantityTV = itemView.findViewById(R.id.tv_quantity);
            userNameTV = itemView.findViewById(R.id.tv_user_name);


        }
    }
}
