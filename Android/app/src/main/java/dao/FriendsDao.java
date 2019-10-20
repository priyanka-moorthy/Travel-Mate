package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import objects.Friend;
@Dao
public interface FriendsDao {

    @Insert
    void insert(Friend... friends);

    @Update
    void update(Friend... friends);

    @Delete
    void delete(Friend... friends);

    @Query("Select * FROM friends")
    Friend[] loadAll();


    @Query("DELETE FROM friends")
    void deleteAll();
}