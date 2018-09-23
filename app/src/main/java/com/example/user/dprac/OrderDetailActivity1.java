package com.example.user.dprac;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.dprac.Adapters.ProductAdapter;
import com.example.user.dprac.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity1 extends AppCompatActivity implements View.OnClickListener {
    RecyclerView productView;
    List<Product> productList;
    ProductAdapter productAdapter;
    JSONArray jsonArray;
    JSONObject jsonObject1;
    TextView heading;
    Button total;
    String order_id;
    LinearLayout deliver_button,return_button,customer_not_available_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity1);
        productView = (RecyclerView)findViewById(R.id.product_list);
        deliver_button = (LinearLayout)findViewById(R.id.deliver_package_button);
        return_button = (LinearLayout)findViewById(R.id.package_return_button);
        customer_not_available_button = (LinearLayout)findViewById(R.id.customer_not_available_button);
        heading = (TextView)findViewById(R.id.heading_order_details);
        total = (Button)findViewById(R.id.total_amount);

        deliver_button.setOnClickListener(this);
        customer_not_available_button.setOnClickListener(this);
        return_button.setOnClickListener(this);

        productView.hasFixedSize();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productList);
        productView.setLayoutManager(new LinearLayoutManager(this));
        productView.setAdapter(productAdapter);
      //  prepareProduct();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(OrderDetailActivity1.this,jsonArray.toString(),Toast.LENGTH_LONG).show();


                    try {

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product product = new Product(jsonObject.getString("name"),"Serial # "+jsonObject.getString("reference"),"5555.00",jsonObject.getString("qty"));
                            productList.add(product);


                        }
                        total.setText("SAR "+jsonObject1.getString("total_paid"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    jsonArray = new JSONArray(getIntent().getStringExtra("product_data"));
                    jsonObject1 = new JSONObject(getIntent().getStringExtra("order_data"));
                    order_id = jsonObject1.getString("order_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void prepareProduct() {
        for(int i=0;i<3;i++){
            Product product = new Product("Nuovo Simonelli Premier Maxi V2 GR","Serial # 965874724","5555.00","1");
            productList.add(product);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.package_return_button:
                Helper.showDialogBox(OrderDetailActivity1.this,"Package Return?","Are you sure the package is returned?","Please confirm that the package is returned","Mark as Return",order_id);
                break;

            case R.id.customer_not_available_button:
                Helper.showDialogBox(OrderDetailActivity1.this,"Customer not available?","Are you sure customer is not available?","Please confirm that the customer is not available.","Yes,Customer not available",order_id);
                break;

            case R.id.deliver_package_button:
                Helper.showDialogBox(OrderDetailActivity1.this,"Deliver Package?","Are you sure the package is delivered?","Please confirm that the package is delivered","Mark as delivered",order_id);
                break;
        }
    }




}
