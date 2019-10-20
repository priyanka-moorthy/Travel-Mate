package objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "friends")
public class Friend {
    @PrimaryKey
    @NonNull
    public String mId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "phone_number")
    public String mPhoneNumber;

    @NonNull
    public String getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public Friend(){

    }

    public Friend(@NonNull String mId, String mName, String mPhoneNumber) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mId = mId;
    }

}
