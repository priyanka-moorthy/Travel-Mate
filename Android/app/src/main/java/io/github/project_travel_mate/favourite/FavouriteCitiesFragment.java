package io.github.project_travel_mate.favourite;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import database.AppDataBase;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import objects.City;

public class FavouriteCitiesFragment extends Fragment {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.fav_cities_list)
    ListView fav_cities_lv;

    private FavouriteCitiesAdapter mFavouriteCitiesAdapter;
    private FlipSettings mSettings = new FlipSettings.Builder().defaultPage().build();
    private AppDataBase mDatabase;
    private List<City> mFavCities;


    public FavouriteCitiesFragment() {
        // Required empty public constructor
    }

    public static FavouriteCitiesFragment newInstance() {
        return new FavouriteCitiesFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_cities, container, false);
        ButterKnife.bind(this, view);

        mDatabase = AppDataBase.getAppDatabase(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFavCities = new ArrayList<>(Arrays.asList(mDatabase.cityDao().loadAllFavourite()));

        if (mFavCities.size() == 0) {
            noResults();
            return;
        }

        mFavouriteCitiesAdapter = new FavouriteCitiesAdapter(getContext(), mFavCities);

        animationView.setVisibility(View.GONE);
        fav_cities_lv.setAdapter(mFavouriteCitiesAdapter);


    }

    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        Toast.makeText(getContext(), R.string.no_favourite_cities, Toast.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

}
