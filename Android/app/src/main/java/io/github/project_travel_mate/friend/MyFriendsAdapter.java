package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.User;
import objects.Friend;

class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendsViewHolder> {

    private final Context mContext;
    private final List<Friend> mFriends;
    private LayoutInflater mInflater;

    MyFriendsAdapter(Context context, List<Friend> friends) {
        this.mContext = context;
        this.mFriends = friends;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.friend_listitem, parent, false);
        return new MyFriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFriendsViewHolder holder, int position) {

        Picasso.with(mContext).load(R.drawable.default_user_icon).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon)
                .into(holder.friendImage);
        String fullName = mFriends.get(position).getmName();
        holder.friendDisplayName.setText(fullName);
        holder.friendDisplayPhoneNumber.setText(mFriends.get(position).getmPhoneNumber());

        holder.call_my_frined.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mFriends.get(position).getmPhoneNumber()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });

        holder.msg_my_frined.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:" + mFriends.get(position).getmPhoneNumber());
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            smsIntent.putExtra("sms_body", "");
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(smsIntent);
        });


//        holder.my_friends_linear_layout.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
//                    mFriends.get(position).getmPhoneNumber()));
//            mContext.startActivity(intent);
//
//        });

    }

    public static String getDefaultSmsAppPackageName(@NonNull Context context) {
        String defaultSmsPackageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            return defaultSmsPackageName;
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms");
            final List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos != null && !resolveInfos.isEmpty())
                return resolveInfos.get(0).activityInfo.packageName;

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    class MyFriendsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        ImageView friendImage;
        @BindView(R.id.friend_display_name)
        TextView friendDisplayName;
        @BindView(R.id.friend_display_number)
        TextView friendDisplayPhoneNumber;
        @BindView(R.id.my_friends_linear_layout)
        LinearLayout my_friends_linear_layout;
        @BindView(R.id.call_friend)
        ImageButton call_my_frined;
        @BindView(R.id.msg_friend)
        ImageButton msg_my_frined;
        MyFriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
