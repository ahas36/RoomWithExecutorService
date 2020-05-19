package au.edu.monash.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import au.edu.monash.myapplication.database.CustomerDatabase;
import au.edu.monash.myapplication.entity.Customer;
import au.edu.monash.myapplication.viewmodel.CustomerViewModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    CustomerDatabase db = null;
    EditText editText = null;
    TextView textView_insert = null;
    TextView textView_read = null;
    TextView textView_delete = null;
    TextView textView_update = null;
    CustomerViewModel customerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //db = CustomerDatabase.getInstance(this);

        Button addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.editText);
        textView_insert = findViewById(R.id.textView_add);
        //Button readButton = findViewById(R.id.readButton);
        textView_read = findViewById(R.id.textView_read);
        Button deleteButton = findViewById(R.id.deleteButton);
        textView_delete = findViewById(R.id.textView_delete);
        Button updateButton = findViewById(R.id.updateButton);
        Button updateButton2 = findViewById(R.id.updateButton2);
        textView_update = findViewById(R.id.textView_update);

        customerViewModel = new
                ViewModelProvider(this).get(CustomerViewModel.class);

        customerViewModel.initalizeVars(getApplication());
        customerViewModel.getAllCustomers().observe(this, new
                Observer<List<Customer>>() {
                    @Override
                    public void onChanged(@Nullable final List<Customer> customers)
                    {
                        String allCustomers = "";
                        for (Customer temp : customers) {
                            String customerstr = (temp.getId() + " " +
                                    temp.getFirstName() + " " + temp.getLastName() + " " + temp.getSalary());
                            allCustomers = allCustomers +
                                    System.getProperty("line.separator") + customerstr;
                        }
                        textView_read.setText("All data: " + allCustomers);
                    }
                });

        customerViewModel.getUpdateStatus().observe(this, new
                Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable final Integer status)
                    {
                        if(status == 0){
                            textView_insert.setText("Update Record Failed");
                            Toast.makeText(getApplicationContext(),"Could not update the data. Id Not found",Toast.LENGTH_SHORT).show();
                        }else if(status == 1){
                            textView_insert.setText("Updated Record");
                            Toast.makeText(getApplicationContext(),"The data has been updated successfully",Toast.LENGTH_SHORT).show();
                        }
//                        customerViewModel.resetUpdateStatus();
                    }
                });


        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(editText.getText().toString().isEmpty())) {
                    String[] details = editText.getText().toString().split(" ");
                    if (details.length == 3) {
                        Customer customer = new Customer(details[0],
                                details[1], Double.parseDouble(details[2]));
                        customerViewModel.insert(customer);
                        textView_insert.setText("Added Record: " + Arrays.toString(details));
                    }
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                customerViewModel.deleteAll();
                textView_delete.setText("All data was deleted");
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] details = editText.getText().toString().split(" ");
                if (details.length == 4) {
                    new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... voids) {
                                try {
                                    Future<Integer> row = customerViewModel.updateByID(new Integer(details[0]).intValue(), details[1], details[2], new Double(details[3]).doubleValue());
                                    while (!row.isDone())
                                    {
                                    }
                                    return row.get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }

                            @Override
                            protected void onPostExecute(Integer status) {
                                if(status == 1)
                                {
                                    textView_insert.setText("Updated Record");
                                    Toast.makeText(getApplicationContext(),"The data has been updated successfully",Toast.LENGTH_SHORT).show();

                                }else {
                                    textView_insert.setText("Update Record Failed");
                                    Toast.makeText(getApplicationContext(),"Could not update the data. Id Not found",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();
                }
            }
        });
        updateButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] details = editText.getText().toString().split(" ");
                if (details.length == 4) {
                    customerViewModel.updateByID2(new Integer(details[0]).intValue(), details[1], details[2], new Double(details[3]).doubleValue());
                }
            }
        });
    }
}