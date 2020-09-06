package com.example.new_rice_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.EventLog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class order extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.text_final_Price)
    TextView txt_final_price;
    @BindView(R.id.btn_order)
    Button btn_order;

    CompositeDisposable compositeDispoable= new CompositeDispoable();
    CartDataSource cartDataSource;

    @Override
    protected void onDestroy() {
        compositeDispoable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        inti();
        intiView();

        getAllItemInCart();

    }

    private void getAllItemInCart() {
        compositeDispoable.add(cartDataSource.getAllCart(Common.currentUser.getFbid(),
                Common.currentRestaurant.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {

                    if(cartItems.isEmpty())
                    {
                        btn_order.setText(getString(R.string.empty_cart));
                        btn_order.setEnabled(false);
                        btn_order.setBackgroundResource(android.R.color.darker_gray);
                    }
                    else{
                        btn_order.setText(getString(R.string.place_order));
                        btn_order.setEnabled(true);
                        btn_order.setBackgroundResource(R.color.colorPrimary);

                        MyCartAdapter adapter = new MyCartAdapter (order.this,cartItems);
                        recycler_cart.setAdapter(com.example.new_rice_shop);
                    }


                }, throwable ->{
                    Toast.makeText(this,"[GET CART]"+throwable.getMessage(),Toast.LENGTH_SHORT.show();

                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intiView() {
        ButterKnife.bind(this);

        toolbar.setTitile(getString(R.string.cart));
        setSupportActionBar(toolbar);
        getSupportActionBar() .setDisplayShowHomeEnabled(true);
        getSupportActionBar() .setDisplayHomeAsUpEnabled(true);

        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        btn_order.setOnClickListener(view -> {
            com.example.new_rice_shop.getDefault().postSticky(new sendTotalCashEvent(txt_final_price.getText().toString()));
            startActivity(new Intent(order.this,PlaceOrderActivity.class));
        });
    }
    private void inti() {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

    }
}