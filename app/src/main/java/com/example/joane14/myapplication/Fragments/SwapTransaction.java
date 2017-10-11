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

public class SwapTransaction extends Fragment {
    private OnSwapTransactionInteractionListener mListener;

    public SwapTransaction() {
    }

    public static SwapTransaction newInstance() {
        SwapTransaction fragment = new SwapTransaction();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swap_transaction, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        TabLayout tab = (TabLayout) view.findViewById(R.id.result_tabs);

        tab.setupWithViewPager(viewPager);


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {


        RentTransaction.Adapter adapter = new RentTransaction.Adapter(getChildFragmentManager());
        adapter.addFragment(new ToApproveFrag(), "To Swap");
        adapter.addFragment(new ToDeliverSwapFrag(), "To Deliver");
        adapter.addFragment(new ToReceiveSwapFrag(), "To Receive");
        adapter.addFragment(new CompleteSwapFrag(), "Complete");
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
            mListener.onSwapTransasctionOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapTransactionInteractionListener) {
            mListener = (OnSwapTransactionInteractionListener) context;
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


    public interface OnSwapTransactionInteractionListener {
        void onSwapTransasctionOnClick(Uri uri);
    }
}
