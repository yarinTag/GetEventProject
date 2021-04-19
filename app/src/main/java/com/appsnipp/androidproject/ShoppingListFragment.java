package com.appsnipp.androidproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsnipp.androidproject.model.EventViewModel;
import com.appsnipp.androidproject.model.Product;
import com.appsnipp.androidproject.model.ProductFireBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

   private RecyclerView shoppingListRV;
   private SearchView searchSV;
   private List<Product> list;
   private ShoppingListAdapter adapter;
   private FloatingActionButton addBtn;
   public MainActivity mainActivity;
    SwipeRefreshLayout refreshLayout ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shopping_list, container, false);

        shoppingListRV = view.findViewById(R.id.rv_shopping_list);
        searchSV = view.findViewById(R.id.sv_search);

        shoppingListRV.setHasFixedSize(true);
        shoppingListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout = view.findViewById(R.id.refresh);

//        initData();

        list=new ArrayList<>();
        adapter = new ShoppingListAdapter(getActivity(), list);
        ProductFireBase.instance.GetAllProduct(mainActivity.currentEventId, new ProductFireBase.ProductListListener() {
            @Override
            public void onSucceed(List<Product> products) {
                list=products;
                adapter.setData(list);
            }
        });

        //adapter

        shoppingListRV.setAdapter(adapter);


        addBtn = view.findViewById(R.id.addProductBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductDialog();
            }
        });


        searchSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length() >= 2){

                    List<Product> temp = new ArrayList<>();
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getProductName().contains(newText))
                            temp.add(list.get(i));
                    }

                    adapter.setData(temp);

                }else{
                    adapter.setData(list);
                }

                return false;
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) getActivity();
    }

    private void init (View view){

        shoppingListRV = view.findViewById(R.id.rv_shopping_list);
        searchSV = view.findViewById(R.id.sv_search);

        shoppingListRV.setHasFixedSize(true);
        shoppingListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

//        initData();

        list=new ArrayList<>();
        adapter = new ShoppingListAdapter(getActivity(), list);
        ProductFireBase.instance.GetAllProduct(mainActivity.currentEventId, new ProductFireBase.ProductListListener() {
            @Override
            public void onSucceed(List<Product> products) {
                list=products;
                adapter.setData(list);
            }
        });

        //adapter

        shoppingListRV.setAdapter(adapter);


        addBtn = view.findViewById(R.id.addProductBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductDialog();
            }
        });



    }
    EditText name;
    EditText description;

    public void addProductDialog(){
        final Dialog dialog1 = new Dialog(mainActivity);
        dialog1.setContentView(R.layout.my_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);

         name=dialog1.findViewById(R.id.name);
        description=dialog1.findViewById(R.id.description);

        Button doneButton=dialog1.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productQ=description.getText().toString();
                if(TextUtils.isEmpty(productQ)){
                    description.setError("error");
                    return;
                }
                String productName=name.getText().toString();
                if(TextUtils.isEmpty(productName)){
                    name.setError("error");
                    return;
                }

                Product product = new Product(productName,productQ,false,mainActivity.fullName);
                ProductFireBase.instance.NewProduct(product, mainActivity.currentEventId, new ProductFireBase.ProductListener() {
                    @Override
                    public void onSucceed() {
                        ProductFireBase.instance.GetAllProduct(mainActivity.currentEventId, new ProductFireBase.ProductListListener() {
                            @Override
                            public void onSucceed(List<Product> products) {
                                list=products;
                                adapter.setData(list);
                                dialog1.cancel();
                            }
                        });


                    }
                });
            }
        });
        Button cancelButton=dialog1.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
        dialog1.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog1.show();






    }
    private void initData(){

        list=  new ArrayList<>();
        for(int i = 0; i < 10; i++){

            Product p = new Product();
            p.setProductName("item " + i +  "");
            p.setProductQuantity(i + "");

            list.add(p);
        }

        Product p1 = new Product();
        p1.setProductName("yarin");
        p1.setProductQuantity(String.valueOf(4));
        list.add(p1);
    }


    @Override
    public void onRefresh() {

        refreshLayout.setRefreshing(true);
//        viewModel.refresh(new EventViewModel.RefreshEventListener() {
//            @Override
//            public void onComplete() {
//                refreshLayout.setRefreshing(false);
//            }
//        });}
    }

}