package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private Spinner categorySpinner;
    private Switch switchDelivery;
    private ListView productsList;
    private Button cartButton;
    ArrayAdapter<Product> adapter;
    public List<Product> products;
    public static final String DATA = "DATA";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPrefs();

        searchInput = findViewById(R.id.searchInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        switchDelivery = findViewById(R.id.switchDelivery);
        productsList = findViewById(R.id.listView);
        searchButton = findViewById(R.id.searchButton);
        cartButton = findViewById(R.id.cartButton);

        gson = new Gson();
        products = new ArrayList<Product>();

        if (prefs.getString(DATA, "").equals("")) {
            products.add(new Product(1, "Dog Food", "Dog", "Food", 20.99, 16, true,"Healthy Dog Food"));
            products.add(new Product(2, "Cat Toy", "Cat", "Toys", 5.49, 30, false,"Funny Toy for your playful cat!"));
            products.add(new Product(3, "Bird Cage", "Bird", "Accessories", 99.99, 50, true,"Colorful Bird Cage for your colorful bird! \n buy it now!!"));
            products.add(new Product(4, "Cat Bed", "Cat", "Accessories", 200.0, 25, false,"Get Your Cat this comfortable Bed!"));
            products.add(new Product(5, "Cat Food", "Cat", "Food", 30.60, 25, false,"Feed Your cat that HEALTHY food!"));
            products.add(new Product(6, "Dog Toy", "Dog", "Toys", 25.0, 18, false,"Let your playful dog play with that Funny toy!!!"));
            products.add(new Product(7, "Bird Food", "Bird", "Food", 45.99, 25, true,"Your best choice for birds food!"));
            products.add(new Product(8, "Dog Bed", "Dog", "Accessories", 299.99, 22, true,"Comfort your dog with that bed!!"));
            products.add(new Product(9, "Cat Collar", "Cat", "Accessories", 7.50, 100, false,"Colorful Cat collar with a small bell"));
            products.add(new Product(10, "Dog Collar", "Dog", "Accessories", 10.0, 70, true,"Colorgul Dog Collar with a small bell! \n BUY it now!"));

            save();
        }
        load();

        setupCategorySpinner();

        performSearch();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, productPage.class);
            Product clickedProduct = adapter.getItem(position);

            intent.putExtra("product_name", clickedProduct.getName());
            intent.putExtra("product_price", clickedProduct.getPrice());
            intent.putExtra("product_quantity", clickedProduct.getQuantity());
            intent.putExtra("product_description", clickedProduct.getDesc());

            startActivity(intent);
        };

        productsList.setOnItemClickListener(itemClickListener);

        cartButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, cartPage.class);
            startActivity(intent1);
        });
    }
    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.product_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
    private void performSearch() {
        String input = searchInput.getText().toString().toLowerCase().trim();
        String selectedCat = categorySpinner.getSelectedItem().toString();
        boolean isHomeDelivery = switchDelivery.isChecked();

        List<Product> filteredList = new ArrayList<>();

        for (Product p : products) {
            boolean matchesInput = input.isEmpty() || p.getName().toLowerCase().contains(input);
            boolean matchesCategory = selectedCat.equals("All") || p.getCategory().equalsIgnoreCase(selectedCat);
            boolean matchesDelivery = !isHomeDelivery || p.isHomeDelivery();

            if (matchesInput && matchesCategory && matchesDelivery && p.getQuantity()>0) {
                filteredList.add(p);
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        productsList.setAdapter(adapter);
    }
    public void load() {
        String str = prefs.getString(DATA, "");
        if (!str.equals("")) {
            Product[] products1 = gson.fromJson(str, Product[].class);

            products.clear();
            for (Product p : products1) {
                products.add(p);
            }
        }
    }
    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }
    private void save() {
        String str = gson.toJson(products);
        editor.putString(DATA, str);
        editor.commit();
    }
}
