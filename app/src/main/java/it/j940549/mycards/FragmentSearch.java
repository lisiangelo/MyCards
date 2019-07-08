package it.j940549.mycards;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import it.j940549.mycards.Model.MyAdapter;
import it.j940549.mycards.Model.Row_Card;
import it.j940549.mycards.SQLite.DBLayer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ArrayList<Row_Card> myDataset =new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLinearLayoutManager;

    private  String query;

    public FragmentSearch() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance( String query) {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putString("query", query);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query= getArguments().getString("query");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_PwSearch);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a Linear grid layout manager
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // specify an adapter (see also next example)

        caricadatiSearch(query);

        mAdapter = new MyAdapter(myDataset, getActivity());

        mRecyclerView.setAdapter(mAdapter);// Inflate the layout for this fragment


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
      //  void onFragmentInteraction(Uri uri);
    }




    public RecyclerView getmRecyclerView(){
        return mRecyclerView;
    }

    public void caricadatiSearch(String a_query) {
        myDataset.clear();
        DBLayer dbLayer = null;

        try {
            dbLayer = new DBLayer(getActivity());
            dbLayer.open();
            Cursor cursor = dbLayer.getCard_forName(a_query);

                    if (cursor.getCount() > 0) {
                        cursor.moveToPosition(0);
                        do {
                            Row_Card row_card = new Row_Card();
                            row_card.setNr_barcode(cursor.getString(0));
                            row_card.setNomeCard(cursor.getString(1).toUpperCase());
                            row_card.setPatch_Image_Front(cursor.getString(2));
                            row_card.setPatch_Image_Retro(cursor.getString(3));
                            Log.i("myCards", row_card.getPatch_Image_Front()+"--"+row_card.getPatch_Image_Retro());
                            myDataset.add(row_card);

                        } while (cursor.moveToNext());
                    }


        } catch (SQLException ex) {
            Toast.makeText(getActivity(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
        Log.d("query", ex.toString());
        }
        dbLayer.close();

    }


}
