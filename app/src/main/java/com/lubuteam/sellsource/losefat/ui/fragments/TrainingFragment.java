package com.lubuteam.sellsource.losefat.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.activities.ChooseLevelActivity;
import com.lubuteam.sellsource.losefat.ui.activities.TipsListActivity;
import com.lubuteam.sellsource.losefat.ui.base.BaseFragment;
import com.lubuteam.sellsource.losefat.ui.cardviewpager.CardFragmentPagerAdapter;
import com.lubuteam.sellsource.losefat.ui.cardviewpager.ShadowTransformer;
import com.lubuteam.sellsource.losefat.utils.ViewUtils;




public class TrainingFragment extends BaseFragment {

    private CardFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public TrainingFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_training, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }


    @Override
    protected void initViews() {
        super.initViews();
        Toolbar toolbar = rootView.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        viewPager = rootView.findViewById(R.id.view_pager);
        pagerAdapter = new CardFragmentPagerAdapter(getChildFragmentManager(), ViewUtils.convertDpToPixel(0, getContext()));
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(AppSettings.getInstance().getLevel() - 1);
//        rootView.findViewById(R.id.lvAds).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AdsCompat.getInstance(getActivity()).showStoreAds(new AdsCompat.AdCloseListener() {
//                    @Override
//                    public void onAdClosed() {
//
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.menu_tips).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TipsListActivity.class);
            startActivity(intent);
        });
        rootView.findViewById(R.id.menu_level).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ChooseLevelActivity.class);
            startActivityForResult(intent, 123);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            viewPager.setCurrentItem(AppSettings.getInstance().getLevel() - 1);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
