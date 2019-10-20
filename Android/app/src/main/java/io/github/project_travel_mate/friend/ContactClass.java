package io.github.project_travel_mate.friend;

class ContactClass {

    private String mId;
    private String mName;
    private String mPhoneNumber;
    private Boolean mIsSelected;


    public ContactClass(String mId, String mName, String mPhoneNumber, Boolean mIsSelected) {
        this.mId = mId;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mIsSelected = mIsSelected;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
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

    public Boolean getmIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(Boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

}
