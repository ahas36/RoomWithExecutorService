package au.edu.monash.myapplication.database;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import au.edu.monash.myapplication.dao.CustomerDAO;
import au.edu.monash.myapplication.entity.Customer;

@Database(entities = {Customer.class}, version = 2, exportSchema = false)
public abstract class CustomerDatabase extends RoomDatabase {

    public abstract CustomerDAO customerDao();

    private static CustomerDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized CustomerDatabase getInstance(final Context
                                                                    context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CustomerDatabase.class, "CustomerDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}