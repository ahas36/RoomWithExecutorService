package au.edu.monash.myapplication.repository;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import au.edu.monash.myapplication.dao.CustomerDAO;
import au.edu.monash.myapplication.database.CustomerDatabase;
import au.edu.monash.myapplication.entity.Customer;

public class CustomerRepository {
    private CustomerDAO dao;
    private LiveData<List<Customer>> allCustomers;
    private Customer customer;

    private MutableLiveData<Integer> updateStatus;

    public void setUpdateStatus(Integer value){
        updateStatus.setValue(value);
    }

    public LiveData getUpdateStatus(){
        return updateStatus;
    }

    public CustomerRepository(Application application){
        CustomerDatabase db = CustomerDatabase.getInstance(application);
        updateStatus = new MutableLiveData<>(-1);
        dao=db.customerDao();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        allCustomers=dao.getAll();
        return allCustomers;
    }
    public void insert(final Customer customer){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(customer);
            }
        });
    }
    public void deleteAll(){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }
    public void delete(final Customer customer){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(customer);
            }
        });
    }
    public void insertAll(final Customer... customers){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(customers);
            }
        });
    }
    public void updateCustomers(final Customer... customers){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Integer outcome = dao.updateCustomers(customers);
                updateStatus.postValue(outcome);
            }
        });
    }
    public Customer findByID(final int customerId){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Customer runCustomer= dao.findByID(customerId);
                setCustomer(runCustomer);
            }
        });
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer=customer;
    }

//
    public Future<Integer> updateCustomerByID(final int customerId, final String firstName, final String lastName, final double salary) throws ExecutionException, InterruptedException {
        Callable<Integer> task = () -> {
            return dao.updatebyID(customerId, firstName, lastName, salary);
        };
        return CustomerDatabase.databaseWriteExecutor.submit(task);
    }

    public void updateCustomerByID2(final int customerId, final String firstName, final String lastName, final double salary)  {
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int id = dao.updatebyID(customerId, firstName, lastName, salary);
                updateStatus.postValue(id);
            }
        });

    }
}
