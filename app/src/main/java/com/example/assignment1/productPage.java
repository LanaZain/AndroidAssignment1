package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class productPage extends AppCompatActivity {
    private TextView p_name, p_price, p_description;
    private EditText quantityIn;
    private Button backButton, addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        p_name = findViewById(R.id.productName);
        p_price = findViewById(R.id.productPrice);
        p_description = findViewById(R.id.productDescription);
        quantityIn = findViewById(R.id.quantityInput);
        addToCartButton = findViewById(R.id.addToCartButton);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String name = intent.getStringExtra("product_name");
            double price = intent.getDoubleExtra("product_price", 0.0);
            String description = intent.getStringExtra("product_description");
            int quantity = intent.getIntExtra("product_quantity", 1);

            p_name.setText(name);
            p_price.setText(String.format("Price: $%.2f", price));
            p_description.setText(description);
            quantityIn.setText(String.valueOf(quantity));
        }

        backButton.setOnClickListener(v -> finish());
        addToCartButton.setOnClickListener(v -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();

            String productsJson = prefs.getString("DATA", "");
            if (!productsJson.equals("")) {
                Product[] products = gson.fromJson(productsJson, Product[].class);

                String updatedProductsJson = gson.toJson(products);
                editor.putString("DATA", updatedProductsJson);
                editor.apply();
            }

            SharedPreferences cartPrefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
            SharedPreferences.Editor cartEditor = cartPrefs.edit();

            String jsonCart = cartPrefs.getString("cart_items", "");
            List<Product> cartItems;

            if (!jsonCart.equals("")) {
                Product[] savedItems = gson.fromJson(jsonCart, Product[].class);
                cartItems = new ArrayList<>(List.of(savedItems));
            } else {
                cartItems = new ArrayList<>();
            }

            Product newProduct = new Product(
                    0,
                    p_name.getText().toString(),
                    "",
                    "",
                    Double.parseDouble(p_price.getText().toString().replace("Price: $", "")),
                    1,
                    false ,
                    p_description.getText().toString()
            );

            cartItems.add(newProduct);

            String updatedCartJson = gson.toJson(cartItems);
            cartEditor.putString("cart_items", updatedCartJson);
            cartEditor.apply();

            finish();
        });
    }
}


