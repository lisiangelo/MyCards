package it.j940549.mycards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import it.j940549.mycards.Model.MyAdapter;
import it.j940549.mycards.Model.Row_Card;
import it.j940549.mycards.SQLite.DBLayer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_elenco_cards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_elenco_cards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_elenco_cards extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


  // TODO: Rename and change types of parameters
  private SharedPreferences sharedPref;
  private ArrayList<Row_Card> myDataset= new ArrayList<>();
  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLinearLayoutManager;
  private int codiceCard;
  //private OnFragmentInteractionListener mListener;

  public Fragment_elenco_cards() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   //* @param param1 Parameter 1.
   //* @param param2 Parameter 2.
   * @return A new instance of fragment Fragment_elenco_cards.
   */
  // TODO: Rename and change types and number of parameters
  public static Fragment_elenco_cards newInstance() {
    Fragment_elenco_cards fragment = new Fragment_elenco_cards();

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
//      mParam1 = getArguments().getString(ARG_PARAM1);
  //    mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_elenco_cards, container, false);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.elenco_cards);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a Linear grid layout manager
    mLinearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLinearLayoutManager);

    // specify an adapter (see also next example)

    caricaDatiCards();

    mAdapter = new MyAdapter(myDataset, getActivity());

    mRecyclerView.setAdapter(mAdapter);// Inflate the layout for this fragment

    sharedPref = getActivity().getSharedPreferences("mycards", Context.MODE_PRIVATE);

    leggiPreferenze();

    FloatingActionButton fab= view.findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        codiceCard++;
        salvaPreferenze(codiceCard);
        Intent vaiInsertNewCard = new Intent(getActivity(),InsertNewCard.class);
        vaiInsertNewCard.putExtra("codiceCard", codiceCard);
        startActivity(vaiInsertNewCard);

      }
    });


    return view;

  }

  private void leggiPreferenze(){

    codiceCard= sharedPref.getInt("codCards", 0);

  }
  private void salvaPreferenze(int codCards) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("codCards", codCards);
        editor.commit();
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
    //mListener = null;
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
    void onFragmentInteraction(Uri uri);
  }

  public void caricaDatiCards(){

    DBLayer dbLayer=null;

    try {
      dbLayer = new DBLayer(getActivity());
      dbLayer.open();
      Cursor cursor = dbLayer.getAllCards();

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
    }catch(SQLException ex){
      Toast.makeText(getActivity(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
    dbLayer.close();



}
  public void vaidepliant(){
    Intent intent= new Intent(getActivity(), Webview_depliant.class);
    intent.putExtra("url","https://www.risparmiocasa.com/Volantino");
    startActivity(intent);
  }

}
