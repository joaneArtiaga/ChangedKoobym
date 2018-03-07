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

import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class RentTransaction extends Fragment {

    private OnRentTransactionInteractionListener mListener;

    public RentTransaction() {
    }


    public static RentTransaction newInstance() {
        RentTransaction fragment = new RentTransaction();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_transaction, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        TabLayout tab = (TabLayout) view.findViewById(R.id.result_tabs);

        tab.setupWithViewPager(viewPager);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRentTransactionOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRentTransactionInteractionListener) {
            mListener = (OnRentTransactionInteractionListener) context;
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


    public interface OnRentTransactionInteractionListener {
        void onRentTransactionOnClick(Uri uri);
    }

    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ToDeliverFrag(), "To Deliver");
        adapter.addFragment(new ToReceiveFrag(), "To Receive");
        adapter.addFragment(new ToReturnFrag(), "To Return");
        adapter.addFragment(new CompleteFrag(), "Complete");
        viewPager.setAdapter(adapter);



    }



    public static class Adapter extends FragmentPagerAdapter {
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

}
