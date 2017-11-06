package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFrag extends Fragment {


    private OnProfileFragInteractionListener mListener;
    User user;



    public ProfileFrag() {
    }

    public static ProfileFrag newInstance(Bundle bundle) {
        ProfileFrag fragment = new ProfileFrag();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        user = new User();

        user = (User) getArguments().getSerializable("user");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        TabLayout tab = (TabLayout) view.findViewById(R.id.result_tabs);

        tab.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        RentTransaction.Adapter adapter = new RentTransaction.Adapter(getChildFragmentManager());
        adapter.addFragment(AboutProfFrag.newInstance(bundle), "User Profile");
        adapter.addFragment(DisplayMyBooks.newInstance(bundle), "Shelf");
        viewPager.setAdapter(adapter);



    }



    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfileFragOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragInteractionListener) {
            mListener = (OnProfileFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnProfileFragInteractionListener {
        void onProfileFragOnClick(Uri uri);
    }
}
