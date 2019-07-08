package it.j940549.mycards;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import it.j940549.mycards.Model.Depliant;
import it.j940549.mycards.Model.MyAdapter;
import it.j940549.mycards.Model.MyAdapterDepliant;
import it.j940549.mycards.Model.Row_Card;
import it.j940549.mycards.SQLite.DBLayer;


public class Fragment_depliant extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ArrayList<Depliant> myDataset= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mGridLayoutManager;

//    private OnFragmentInteractionListener mListener;

    public Fragment_depliant() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_depliant newInstance() {
        Fragment_depliant fragment = new Fragment_depliant();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_depliant, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.elenco_depliant);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a Linear grid layout manager
        mGridLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // specify an adapter (see also next example)

        caricaDatiDepliant();

        mAdapter = new MyAdapterDepliant(myDataset, getActivity());

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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    public void caricaDatiDepliant(){


        Depliant eurispin= new Depliant();
        eurispin.setUrl("https://www.eurospin.it/volantino-nazionale/");
        eurispin.setNomeDepliant("eurospin");
        Depliant conadzodica= new Depliant();
        conadzodica.setUrl("https://www.conad.it/ricerca-negozi/negozio.006228.html");
        conadzodica.setNomeDepliant("conad zodiaco");
        Depliant conadAnzio= new Depliant();
        conadAnzio.setUrl("https://www.conad.it/ricerca-negozi/negozio.008625.html");
        conadAnzio.setNomeDepliant("conad anzio");
        Depliant risparmiocasa= new Depliant();
        risparmiocasa.setUrl("https://www.risparmiocasa.com/Volantino");
        risparmiocasa.setNomeDepliant("risparmiocasa");
        Depliant tuodi= new Depliant();
        tuodi.setUrl("http://www.tuodi.it/negozi-dettaglio.cfm?negozio=90276&promo=3");
        tuodi.setNomeDepliant("tuodi");
        Depliant acqua_sapone= new Depliant();
        acqua_sapone.setUrl("https://www.acquaesapone.it/mobile/elenco-promozioni.php?provincia=RM&comuneA=ANZIO&negozio=%25");
        acqua_sapone.setNomeDepliant("acqua&sapone");
        Depliant unieuro= new Depliant();
        unieuro.setUrl("https://www.unieuro.it/online/volantino");
        unieuro.setNomeDepliant("unieuro");
        Depliant euronics= new Depliant();
        euronics.setUrl("https://tufano.euronics.it/volantino/");
        euronics.setNomeDepliant("euronics");
        Depliant mediaworld= new Depliant();
        mediaworld.setUrl("https://www.mediaworld.it/mw/promozioni/promo-index?ds_rl=1250284&ds_rl=1250284&ds_rl=1250284&gclid=CjwKCAiA4t_iBRApEiwAn-vt-1HnczTY6ERoejzU-svbqHIq2deVaGFRD68ocYt9DcCoza1ziP-5ZhoCjR8QAvD_BwE&gclsrc=aw.ds");
        mediaworld.setNomeDepliant("mediaworld");

        myDataset.add(eurispin);
        myDataset.add(conadzodica);
        myDataset.add(conadAnzio);
        myDataset.add(risparmiocasa);
        myDataset.add(tuodi);
        myDataset.add(acqua_sapone);
        myDataset.add(unieuro);
        myDataset.add(euronics);
        myDataset.add(mediaworld);
    }

}
