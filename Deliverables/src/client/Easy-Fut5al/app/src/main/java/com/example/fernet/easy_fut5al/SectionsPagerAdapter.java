package com.example.fernet.easy_fut5al;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;


    //Prende anche  come parametro il menuItemScelto
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }


    @Override
    //Tab da visualizzare
    public int getCount() {
        return 2;

    }


    @Override
    //La position(numero scheda) ha senso solo se si sceglie il menu principale come item
    public Fragment getItem(int position) {

        return SezioneHome.newInstance(position);


    }
}
