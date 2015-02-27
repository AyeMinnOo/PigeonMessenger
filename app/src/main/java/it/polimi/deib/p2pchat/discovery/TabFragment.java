package it.polimi.deib.p2pchat.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.polimi.deib.p2pchat.R;
import it.polimi.deib.p2pchat.discovery.chatmessages.WiFiChatFragment;
import it.polimi.deib.p2pchat.discovery.services.WiFiP2pServicesFragment;
import lombok.Getter;


/**
 * Class that represents a Fragment with other Fragments as Tabs.
 * Created by Stefano Cappa on 05/02/15.
 */
public class TabFragment extends Fragment {

    @Getter
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Getter
    private ViewPager mViewPager;
    @Getter
    private static WiFiP2pServicesFragment wiFiP2pServicesFragment;
    @Getter
    private static List<WiFiChatFragment> wiFiChatFragmentList;


    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        wiFiP2pServicesFragment = WiFiP2pServicesFragment.newInstance();
        wiFiChatFragmentList = new ArrayList<>();
        return fragment;
    }

    /**
     * Default Fragment constructor.
     */
    public TabFragment() {
    }

    /**
     * Method to get the Fragment, specifying the position / tabnumber.
     *
     * @param tabNumber int that represents the position of this fragment inside the list of tabs.
     * @return The {@link WiFiChatFragment } at position tabNumber in the list of
     * {@link it.polimi.deib.p2pchat.discovery.chatmessages.WiFiChatFragment}.
     */
    public WiFiChatFragment getChatFragmentByTab(int tabNumber) {
        return wiFiChatFragmentList.get(tabNumber - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);


        // When swiping between different sections, select the corresponding
        // tab.
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }


    /**
     * Method to check if tabNum is valid.
     * tabNum is valid if tabNum >= 1 && tabNum <= this.getWiFiChatFragmentList().size().
     * </br>
     * ----------------------------------------------------------------------------------
     * Example:
     * getWiFiChatFragmentList 0 1 2 3 4 5 6 7 8   <-Index of the list. The Size() == 9
     * tabNum                  1 2 3 4 5 6 7 8 9   <-number of tabs
     * </br>
     * Condition for tabNum:
     * 1] 0 is reserved to servicelist  ----> tabNum>=1
     * 2] 9 <= size()=9 ----> tabNum <= tabFragment.getWiFiChatFragmentList().size()
     * </br>
     * Finally 1 && 2!!!
     * ----------------------------------------------------------------------------------
     * </br>
     * @param tabNum int that represents the tab number to check
     * @return true of false, if the condition is valid or not.
     */
    public boolean isValidTabNum (int tabNum) {
        return tabNum >= 1 && tabNum <= this.getWiFiChatFragmentList().size();
    }

    /**
     * Class that represents the FragmentPagerAdapter of this Fragment, that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return wiFiP2pServicesFragment; //the first fragment reserved to the serviceListFragment
            } else {
                return wiFiChatFragmentList.get(position - 1); //chatFragments associated to this position
            }
        }

        @Override
        public int getCount() {
            //because the first fragment (not inside into the list) is a WiFiP2pServicesFragment
            return wiFiChatFragmentList.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return ("Services").toUpperCase(l);
                default:
                    //if possibile use the devicename like tabname.
                    //Attention this isn't working. Please be careful.
//                    if(DeviceTabList.getInstance().getDevice(position)!=null) {
//                        return DeviceTabList.getInstance().getDevice(position).deviceName.toUpperCase(l);
//                    }
                    //use this to be sure
                    return ("Chat" + position).toUpperCase(l);
            }
        }
    }
}
