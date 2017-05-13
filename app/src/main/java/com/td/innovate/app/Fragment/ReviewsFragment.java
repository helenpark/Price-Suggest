package com.td.innovate.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.td.innovate.app.Adapter.ReviewsAdapter;
import com.td.innovate.app.Model.Result;
import com.td.innovate.app.R;

import java.util.ArrayList;


public class ReviewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> myList;
    private ReviewsAdapter itemsAdapter;

    public static ArrayList<Result> myResults;
    private ListView lv;

//    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myList = new ArrayList<>();
//        myList.add("test");
        itemsAdapter = new ReviewsAdapter(getActivity().getApplicationContext(),myList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        lv = (ListView) view.findViewById(R.id.reviewsListView);
        lv.setAdapter(itemsAdapter);
        myList.add("Xbox One has really upped their game. With backwards compatibility on it's way my son will be able to play most of his 360 games. I like the new controller design and I really like that I can replace the batteries. The addition of the headphone jack is a bonus. I shopped around and found a bundle at Walmart that Best Buy matched. With Best Buy points it adds up and saves me money. Why buy elsewhere when they price match.");
        myList.add("Glad I finally got the Xbox One, but I must say the dashboard is a bit confusing compared to the 360. I am sure I will enjoy it and have good times with the system. The graphics are really good.\n" +
                "\n" +
                "As for Gears?\n" +
                "\n" +
                "Yea, I am not impressed so far. It looks fantastic, but the matchmaking is pretty bad on multiplayer. Takes forever sometimes, and people keep dropping and coming back, then dropping. You just sit there.\n" +
                "\n" +
                "The penalty for quitting needs to be stiffer. Like you cant play matchmaking for x hours if you keep doing it or your rating drops really hard.\n" +
                "\n" +
                "The rating system is also bad for multiplayer. I am level 12 now and I play vs level 42 players? How does this system work. Seems random to me.\n" +
                "\n" +
                "The system is good and for the price its not bad, but you might want to rethink getting the GOW Ultimate Edition version.");
        itemsAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("yay", String.valueOf(position));
                TextView tv = (TextView) view.findViewById(R.id.reviewsTV);
                if (tv.getMaxLines() == 3) {
                    tv.setMaxLines(Integer.MAX_VALUE);
                    for (int i = 0; i < lv.getCount(); i++) {
                        View parentView = getViewByPosition(i, lv);
                        TextView otv = (TextView) parentView.findViewById(R.id.reviewsTV);
                        if(otv.getMaxLines() > 3 && i != position){
                        otv.setMaxLines(3);
                        }
                    }
                } else {
                    tv.setMaxLines(3);
                }
                itemsAdapter.notifyDataSetChanged();
            }
        });

//        Log.i("yay", String.valueOf(lv.getCount()));

        return view;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


}


