package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class cartPage extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalPrice;
    private List<Product> cartItems;
    private Button chkOut;
    private Gson gson;
    private SharedPreferences cartPrefs;
    private SharedPreferences mainPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        cartListView = findViewById(R.id.cartListView);
        totalPrice = findViewById(R.id.totalPrice);
        chkOut = findViewById(R.id.checkoutButton);

        gson = new Gson();
        cartPrefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        mainPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        loadCart();
        setupCheckoutButton();
    }

    private void loadCart() {
        String json = cartPrefs.getString("cart_items", "");
        cartItems = new ArrayList<>();

        if (!json.isEmpty()) {
            try {
                Product[] items = gson.fromJson(json, Product[].class);
                if (items != null) {
                    for (Product p : items) {
                        cartItems.add(p);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error loading cart", Toast.LENGTH_SHORT).show();
            }
        }

        updateCartDisplay();
    }

    private void updateCartDisplay() {
        double total = 0.0;
        List<String> displayList = new ArrayList<>();

        for (Product p : cartItems) {
            double itemTotal = p.getPrice();
            total += itemTotal;
            displayList.add(p.getName() + " - $" + String.format("%.2f", itemTotal));
        }

        totalPrice.setText("Total: $" + String.format("%.2f", total));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        cartListView.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        chkOut.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = processCheckout();

            if (success) {
                Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(cartPage.this, checkOutPage.class));
            } else {
                Toast.makeText(this, "Checkout failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean processCheckout() {
        try {
            String productsJson = mainPrefs.getString("DATA", "");
            if (productsJson.isEmpty()) return false;

            Product[] products = gson.fromJson(productsJson, Product[].class);
            if (products == null) return false;

            for (Product cartItem : cartItems) {
                for (Product product : products) {
                    if (product.getName().equals(cartItem.getName())) {
                        product.setQuantity(Math.max(product.getQuantity() - 1, 0));
                        break;
                    }
                }
            }

            mainPrefs.edit()
                    .putString("DATA", gson.toJson(products))
                    .apply();

            cartPrefs.edit()
                    .remove("cart_items")
                    .apply();
            loadCart();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}