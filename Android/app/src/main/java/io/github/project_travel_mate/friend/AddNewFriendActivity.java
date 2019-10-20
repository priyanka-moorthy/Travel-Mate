package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import database.AppDataBase;
import io.github.project_travel_mate.R;
import objects.Friend;


public class AddNewFriendActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Cursor mCursor;
    private String mName, mPhonenumber, mId;
    private AppDataBase mDatabase;

    private ArrayList<ContactClass> mContacts;
    private HashMap<String, Friend> mSelectedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.contacts_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mContacts = new ArrayList<ContactClass>();
        mSelectedContacts = new HashMap<String, Friend>();

        getContacts();

        mAdapter = new ContactsAdapter(mContacts);
        mRecyclerView.setAdapter(mAdapter);
        mDatabase = AppDataBase.getAppDatabase(this);







//        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getContacts() {

        mCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (mCursor.moveToNext()) {

            mId = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

            mName = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            mPhonenumber = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactClass object = new ContactClass(mId, mName, mPhonenumber, false);
            mContacts.add(object);
        }

        mCursor.close();

    }

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
        private ArrayList<ContactClass> mValues;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txtHeader;
            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                txtHeader = (TextView) v.findViewById(R.id.contactName);
                txtFooter = (TextView) v.findViewById(R.id.contactNumber);
            }
        }

        public void add(int position, ContactClass item) {
            mValues.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
        }

        public ContactsAdapter(ArrayList<ContactClass> myDataset) {
            mValues = myDataset;
        }

        @Override
        public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v =
                    inflater.inflate(R.layout.contact_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final String name = mValues.get(position).getmName();
            final String contactNumber = mValues.get(position).getmPhoneNumber();


            holder.txtHeader.setText(name);
            holder.layout.setBackgroundColor(mValues.get(position).getmIsSelected() ?
                    Color.parseColor("#c8f1f9") : Color.WHITE);
            holder.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mValues.get(position).setmIsSelected(!mValues.get(position).getmIsSelected());
//                    holder.layout.setBackgroundColor(mValues.get(position).getmIsSelected() ?
//                            Color.parseColor("#c8f1f9") : Color.WHITE);
//                    Log.i("info", mValues.get(position).getmId());
                    if (!mValues.get(position).getmIsSelected()) {
                        Friend friend = new Friend(mValues.get(position).getmId(),
                                mValues.get(position).getmName(), mValues.get(position).getmPhoneNumber());
                        mSelectedContacts.put(mValues.get(position).getmId(), friend);
                        mValues.get(position).setmIsSelected(!mValues.get(position).getmIsSelected());
                        holder.layout.setBackgroundColor(mValues.get(position).getmIsSelected() ?
                                Color.parseColor("#c8f1f9") : Color.WHITE);
                        String jsonString = new com.google.gson.Gson().toJson(mSelectedContacts.values());
                        Log.i("selected friends added", jsonString);

                    } else if (mSelectedContacts.containsKey(mValues.get(position).getmId())) {
                        Log.i("id is", mValues.get(position).getmId());
                        mValues.get(position).setmIsSelected(!mValues.get(position).getmIsSelected());
                        holder.layout.setBackgroundColor(mValues.get(position).getmIsSelected() ?
                                Color.parseColor("#c8f1f9") : Color.WHITE);
                        mSelectedContacts.remove(mValues.get(position).getmId());
                        String jsonString = new com.google.gson.Gson().toJson(mSelectedContacts.values());
                        Log.i("selected friends-r", jsonString);

                    }

                }
            });

            holder.txtFooter.setText(contactNumber);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


    }

    @OnClick(R.id.add_selected_friends)
    void addFriend() {
        String jsonString = new com.google.gson.Gson().toJson(mSelectedContacts.values());
        Log.i("selected friends", jsonString);
        for (String currentKey : mSelectedContacts.keySet()) {
            mDatabase.friendsDao().insert(mSelectedContacts.get(currentKey));
        }

        Friend[] friends = mDatabase.friendsDao().loadAll();
        Log.i("friends in db", friends.toString());

        setResult(Activity.RESULT_OK);
        finish();
    }
}
