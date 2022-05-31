package com.merryblue.femalefitness.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.model.GroupSectionModel;
import com.merryblue.femalefitness.data.model.GroupViewModel;
import com.merryblue.femalefitness.ui.adapters.GroupAdapter;
import com.merryblue.femalefitness.ui.adapters.decoration.GroupItemDecoration;
import com.merryblue.femalefitness.ui.adapters.decoration.PreCachingLayoutManager;
import com.merryblue.femalefitness.ui.base.BaseFragment;
import com.merryblue.femalefitness.utils.ViewUtils;

import java.util.ArrayList;


public class RountinesListFragment extends BaseFragment {

    private ArrayList<GroupViewModel> data;
    private RecyclerView recyclerView;
    private GroupAdapter adapter;

    public RountinesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_workouts_list, container, false);
            initViews();
            initObservers();
            initEvents();
            initData();
        }
        return rootView;
    }

    private void initData() {
//        data.add(new GroupViewModel(GroupViewModel.TYPE_LAYOUT, R.layout.guide_add_favorite_layout));
        data.add(
                new GroupViewModel(
                        GroupViewModel.TYPE_GROUP_SECTION,
                        new GroupSectionModel(
                                0,
                                getString(R.string.routine),
                                new ArrayList<String>() {{
                                    add("11");
                                    add("12");

                                }}
                        )
                )
        );
        data.add(
                new GroupViewModel(
                        GroupViewModel.TYPE_FRAGMENT,
                        new AdsHorizontalFragment()
                )
        );

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initViews() {
        super.initViews();
        data = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new PreCachingLayoutManager(getContext(), ViewUtils.getHeightDevicePixel(getActivity()) * 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(5);
        recyclerView.addItemDecoration(new GroupItemDecoration(getContext()));
        adapter = new GroupAdapter(getChildFragmentManager(), data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.destroy();
    }
}
