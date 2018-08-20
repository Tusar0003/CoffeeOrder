package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    String priceMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            //Show an error message as a toast

            /*First option to display the toast message
            Context context = getApplicationContext();
            CharSequence text = "You can not have more than 100 cups of coffee";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/

            //Second option to display the toast message
            Toast.makeText(this, "You can not have more than 100 cups of coffee", Toast.LENGTH_SHORT).show();
            //Exit this method early because there's nothing left to do
            return;
        } else {
            quantity++;

            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            /*Show an error message as a toast

            /*First option to display the toast message
            Context context = getApplicationContext();
            CharSequence text = "You can not have less than 1 cup of coffee";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/

            //Second option to display the toast message
            Toast.makeText(this, "You can not have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            //Exit this method early because there's nothing left to do
            return;
        } else {
            quantity--;

            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Find the username
        EditText editTextName = (EditText) findViewById(R.id.name_editText);
        String name = editTextName.getText().toString();

        //Figure out if the user wants whipped cream topping
        CheckBox checkBoxWhippedCream = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = checkBoxWhippedCream.isChecked();

        //Figure out if the user wants chocolate topping
        CheckBox checkBoxChocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = checkBoxChocolate.isChecked();


        int price = calculatePrice(hasWhippedCream, hasChocolate);
        priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for "+name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * This method calculates the price of the order
     *
     * @return price of the coffee(s) ordered
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if(addWhippedCream && addChocolate) {
            /*Add $1 + $2 if the user wants whipped cream and chocolate*/
            basePrice += 1 + 2;
        } else if (addWhippedCream) {
            /*Add $1 if the user wants whipped cream*/
            basePrice += 1;
        } else {
            if (addChocolate) {
                /*Add $2 if the user wants chocolate*/
                basePrice += 2;
            }
        }

        return quantity * basePrice;
    }

    /**
     * This method creates summary of the order
     * @param name of the customer
     * @param price of the order
     * @param addWhippedCream is whether or not the user wants the whipped cream topping
     * @param addChocolate is whether or not the user wants the chocolate topping
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        priceMessage = getString(R.string.order_summary_name) + name;
        priceMessage += "\n"+ getString(R.string.order_summary_add_whipped_cream) + addWhippedCream;
        priceMessage += "\n"+ getString(R.string.order_summary_add_chocolate) + addChocolate;
        priceMessage += "\n" + getString(R.string.order_summary_quantity) + quantity;
        priceMessage += "\n" + getString(R.string.order_summary_total) + price;
        priceMessage += "\n" + getString(R.string.order_summary_thank_you);

        return priceMessage;
    }
}