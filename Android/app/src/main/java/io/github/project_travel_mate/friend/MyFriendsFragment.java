package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import database.AppDataBase;
import io.github.project_travel_mate.R;
import objects.Friend;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class MyFriendsFragment extends Fragment {

    private final List<User> mFriends = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private String mToken;
    private Handler mHandler;
    private Activity mActivity;
    private MyFriendsAdapter mAdapter;
    private AppDataBase mDatabase;
    static int ADDNEWFRIEND_ACTIVITY = 204;
    private  List<Friend> mMyFriends;
    public MyFriendsFragment() {
        // Required empty public constructor
    }

    public static MyFriendsFragment newInstance() {
        return new MyFriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());
        mDatabase = AppDataBase.getAppDatabase(this.getContext());

        listMyFriends();
//        myFriends();
        return view;

    }

    private void listMyFriends() {

        mMyFriends = new ArrayList<>();
        Friend[] friends = mDatabase.friendsDao().loadAll();
        for (int i = 0; i < friends.length; i++) {
            String friendName = friends[i].getmName();
            mMyFriends.add(friends[i]);
            animationView.setVisibility(GONE);
            mAdapter = new MyFriendsAdapter(mActivity.getApplicationContext(), mMyFriends);
            recyclerView.setAdapter(mAdapter);

        }

    }

    private void myFriends() {

        String uri = API_LINK_V2 + "trip-friends-all";

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONArray arr;
                        try {
                            final String res = response.body().string();
                            Log.v("Response", res);
                            arr = new JSONArray(res);

                            if (arr.length() <= 1) {
                                noResults();
                                return;
                            }

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                String userName = object.getString("username");
                                String firstName = object.getString("first_name");
                                String lastName = object.getString("last_name");
                                int id = object.getInt("id");
                                String imageURL = object.getString("image");
                                String dateJoined = object.getString("date_joined");
                                String status = object.getString("status");
                                mFriends.add(new User(userName, firstName, lastName, id, imageURL, dateJoined, status));
                                animationView.setVisibility(GONE);
//                                mAdapter = new MyFriendsAdapter(mActivity.getApplicationContext(), mFriends);
                                recyclerView.setAdapter(mAdapter);
                            }
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }


    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        Toast.makeText(mActivity, R.string.no_friends_message, Toast.LENGTH_SHORT).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    /**
     * Navigate to Add Friend
     */
    @OnClick(R.id.add_new_friend)
    void addFriend() {
        Intent intent = new Intent(getContext(), AddNewFriendActivity.class);
        startActivityForResult(intent, ADDNEWFRIEND_ACTIVITY);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        for (Fragment fragment : getFragmentManager().getFragments()) {
//            if (fragment != null) {
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    @Override
    public void onResume() {
        super.onResume();
        listMyFriends();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }





}
