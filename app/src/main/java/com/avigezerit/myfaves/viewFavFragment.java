package com.avigezerit.myfaves;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* * * * * * * * * * * * * FAV VIEW FRAGMENT - ON TAP * * * * * * * * * * * * */

public class viewFavFragment extends Fragment {

    View view;

    ///////////////////////////////////////// CONSTANTS /////////////////////////////////////////////

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public viewFavFragment() {}

    public static viewFavFragment newInstance(String param1, String param2) {
        viewFavFragment fragment = new viewFavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ///////////////////////////////////////// ON CREATE /////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: inflate view
        view = inflater.inflate(R.layout.fragment_view_fav, container, false);

        //TODO: bind to xml

        //TODO: set values

        return view;
    }
}
