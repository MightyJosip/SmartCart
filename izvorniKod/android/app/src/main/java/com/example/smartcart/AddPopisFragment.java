package com.example.smartcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPopisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPopisFragment extends Fragment {
    private EditText idPopis, imePopis, artikl;
    private Button bnSave;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPopisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPopisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPopisFragment newInstance(String param1, String param2) {
        AddPopisFragment fragment = new AddPopisFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_popis, container, false);


        imePopis = view.findViewById(R.id.editTextTextPersonName4);
        bnSave = view.findViewById(R.id.button4);

        bnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ime = imePopis.getText().toString();

                Popis popis = new Popis(ime);

                SmartCartDatabase db = SmartCartDatabase.getInstance(AddPopisFragment.this.getContext());
                PopisDao dao = db.popisDao();
                dao.dodajPopise(popis);
                Toast.makeText(getActivity(), "Popis uspjesno dodan", Toast.LENGTH_SHORT).show();


            }
        });
        return view;


    }
}