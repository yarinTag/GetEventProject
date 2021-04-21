package com.appsnipp.androidproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.TextView;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.Model;
import com.appsnipp.androidproject.model.Product;
import com.appsnipp.androidproject.model.ProductModel;
import com.appsnipp.androidproject.model.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Adapter.ShoppingListAdapter;

public class ShoppingListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

   private RecyclerView shoppingListRV;
   private SearchView searchSV;
   private List<Product> list;
   private ShoppingListAdapter adapter;
   private FloatingActionButton addBtn;
    ProductViewModel viewModel;
    LiveData<List<Product>> liveData;
    SwipeRefreshLayout refreshLayout ;
    MainActivity parent;
    String eventId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        parent= (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shopping_list, container, false);
        //parent= this.getParentFragment();
        shoppingListRV = view.findViewById(R.id.rv_shopping_list);
        searchSV = view.findViewById(R.id.sv_search);
        parent.getSupportActionBar().setTitle("Event Shopping List");
        shoppingListRV.setHasFixedSize(true);
        shoppingListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout = view.findViewById(R.id.refresh);
        new ItemTouchHelper(ItemTouchHelperCallback).attachToRecyclerView(shoppingListRV);
        Event event = EventFragmentArgs.fromBundle(getArguments()).getEvent();
        parent.currentEventId = event.getEventID();
        eventId=event.getEventID();

//        initData();

        list=new ArrayList<>();
        adapter = new ShoppingListAdapter(getActivity(), list);

                viewModel.getData(eventId, new ProductModel.GetAllLiveDataListener() {
                            @Override
                            public void onComplete(LiveData<List<Product>> data) {
                                liveData = data;
                                liveData.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                                    @Override
                                    public void onChanged(List<Product> products) {

                                        list = products;
                                        adapter.setList(products);
                                    }
                                });
                                //adapter
                            }
                        });

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


    private void init (View view){

        shoppingListRV = view.findViewById(R.id.rv_shopping_list);
        searchSV = view.findViewById(R.id.sv_search);

        shoppingListRV.setHasFixedSize(true);
        shoppingListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);

//        initData();

        list=new ArrayList<>();
        adapter = new ShoppingListAdapter(getActivity(), list);
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
        final Dialog dialog1 = new Dialog(parent);
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

                Product product = new Product(productName,productQ,false,parent.user.getFullName(), System.currentTimeMillis(),parent.currentEventId);
                Model.instance.NewProduct(product, parent.currentEventId, new Model.ProductListener() {
                    @Override
                    public void onComplete() {
                        dialog1.cancel();
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


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        viewModel.refresh(eventId,new ProductViewModel.RefreshEventListener(){
            @Override
            public void onComplete() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    ItemTouchHelper.SimpleCallback ItemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


            Button yes_btn;
            Button cancel_btn;
            TextView delete_txt;

            final Dialog dialog2 = new Dialog(parent);
            dialog2.setContentView(R.layout.delete_dialog);
            dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = dialog2.getWindow();
            window.setGravity(Gravity.CENTER);

            cancel_btn = dialog2.findViewById(R.id.cancel_btn);
            yes_btn = dialog2.findViewById(R.id.yes_btn);
            delete_txt = dialog2.findViewById(R.id.delete_txt);


            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = list.get(viewHolder.getAdapterPosition());
                    Model.instance.deleteProduct(product, eventId, new Model.DeleteProductListener(){
                        @Override
                        public void onComplete() {
                            dialog2.cancel();
                        }
                    });

                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.notifyDataSetChanged();
                    dialog2.cancel();
                }
            });


            dialog2.setCancelable(false);
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog2.show();


        }
    };


}