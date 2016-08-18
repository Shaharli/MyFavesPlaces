package com.avigezerit.myfaves;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* * * * * * * * * * * * * HEADER FRAGMENT DESIGN - LOCATED IN ALL ACTIVITIES * * * * * * * * * * * * */

public class HeaderFragment extends Fragment {

    ///////////////////////////////////////// CONSTANTS /////////////////////////////////////////////

    private static final String CONTEXT_NAME = "context";

    View view;

    public static HeaderFragment newInstance(String context) {

        HeaderFragment header = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(CONTEXT_NAME, context);
        header.setArguments(args);
        return header;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TO DONE: inflate view
        view = inflater.inflate(R.layout.fragment_header, container, false);

        TextView contextTV = (TextView) view.findViewById(R.id.contextTV);
        contextTV.setText(getArguments().getString(CONTEXT_NAME));

        return view;

    }

}

