package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class checkOutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_page);

        TextView confirmationMessage = findViewById(R.id.confirmationMessage);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        confirmationMessage.setText("Order Confirmed!\nThank you for your purchase!");

        String orderDetails = getOrderDetailsFromIntent();
        if (!orderDetails.isEmpty()) {
            confirmationMessage.append("\n\n" + orderDetails);
        }

        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String getOrderDetailsFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            StringBuilder details = new StringBuilder();

            double totalAmount = intent.getDoubleExtra("total_amount", 0);
            if (totalAmount > 0) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
                details.append("Total: ").append(format.format(totalAmount)).append("\n");
            }
        }
        return "";
    }
}