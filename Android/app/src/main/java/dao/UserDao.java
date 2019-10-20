package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import objects.User;

public interface UserDao {

    @Insert
    void insert(User... user);

    @Update
    void update(User... user);

    @Delete
    void delete(User... user);

    @Query("Select * FROM user")
    User[] loadAll();

    @Query("DELETE FROM user")
    void deleteAll();

}
