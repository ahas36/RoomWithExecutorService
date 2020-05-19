package au.edu.monash.myapplication.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import au.edu.monash.myapplication.entity.Customer;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CustomerDAO {
    @Query("SELECT * FROM customer")
    LiveData<List<Customer>> getAll();
    @Query("SELECT * FROM customer WHERE uid = :customerId LIMIT 1")
    Customer findByID(int customerId);
    @Insert
    void insertAll(Customer... customers);
    @Insert
    long insert(Customer customer);
    @Delete
    void delete(Customer customer);
    @Update(onConflict = REPLACE)
    int updateCustomers(Customer... customers);
    @Query("DELETE FROM customer")
    void deleteAll();

    @Query("UPDATE customer SET first_name=:firstName, last_name=:lastName, salary=:salary WHERE uid = :id")
    int updatebyID(int id, String firstName, String lastName, double salary);
}