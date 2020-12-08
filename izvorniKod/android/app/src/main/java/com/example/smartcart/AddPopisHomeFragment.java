package com.example.smartcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPopisHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPopisHomeFragment extends Fragment implements View.OnClickListener {

    private Button BtnAddPopis;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPopisHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPopisHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPopisHomeFragment newInstance(String param1, String param2) {
        AddPopisHomeFragment fragment = new AddPopisHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_popis_home, container, false);
        BtnAddPopis = view.findViewById(R.id.btn_add_popis);
        BtnAddPopis.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        HomeScreenActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new AddPopisFragment()).addToBackStack(null).commit();
    }
}