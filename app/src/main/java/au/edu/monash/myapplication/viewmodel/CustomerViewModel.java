package au.edu.monash.myapplication.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import au.edu.monash.myapplication.entity.Customer;
import au.edu.monash.myapplication.repository.CustomerRepository;

public class CustomerViewModel extends ViewModel {
    private CustomerRepository cRepository;

    public CustomerViewModel() {

    }


    public LiveData<List<Customer>> getAllCustomers() {
        return cRepository.getAllCustomers();
    }

    public void initalizeVars(Application application) {
        cRepository = new CustomerRepository(application);
    }

    public void insert(Customer customer) {
        cRepository.insert(customer);
    }

    public LiveData<Integer> getUpdateStatus (){
        return cRepository.getUpdateStatus();
    }

    public void resetUpdateStatus (){
        cRepository.setUpdateStatus(-1);
    }


    public void insertAll(Customer... customers) {
        cRepository.insertAll(customers);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }

    public void insertAll(Customer customer) {
        cRepository.delete(customer);
    }

    public void update(Customer... customers) {
        cRepository.updateCustomers(customers);
    }

    public Future<Integer> updateByID(int customerId, String firstName, String lastName, double salary) throws ExecutionException, InterruptedException {
        return cRepository.updateCustomerByID(customerId,firstName, lastName, salary);
    }

    public void updateByID2(int customerId, String firstName, String lastName, double salary) {
        cRepository.updateCustomerByID2(customerId,firstName, lastName, salary);
    }

    public Customer insertAll(int id) {
        return cRepository.findByID(id);
    }

    public Customer findByID(int customerId) {
        return cRepository.findByID(customerId);
    }
}