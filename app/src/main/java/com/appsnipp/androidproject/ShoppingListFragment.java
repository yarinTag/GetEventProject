package com.appsnipp.androidproject;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsnipp.androidproject.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

   private RecyclerView shoppingListRV;
   private SearchView searchSV;
   private List<Product> list;
   private ShoppingListAdapter adapter;

   private FloatingActionButton addBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shopping_list, container, false);

        init(view);


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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_shoppingListFragment_to_myDialog);
            }
        });



        return view;
    }


    private void init (View view){

        shoppingListRV = view.findViewById(R.id.rv_shopping_list);
        searchSV = view.findViewById(R.id.sv_search);

        shoppingListRV.setHasFixedSize(true);
        shoppingListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        initData();

        //adapter
        adapter = new ShoppingListAdapter(getActivity(), list);
        shoppingListRV.setAdapter(adapter);


        addBtn = view.findViewById(R.id.addProductBtn);



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


}