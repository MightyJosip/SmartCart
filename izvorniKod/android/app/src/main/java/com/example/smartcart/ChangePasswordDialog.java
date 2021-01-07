package com.example.smartcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangePasswordDialog extends AppCompatDialogFragment {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordAgain;
    private ChangePasswordDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_password_change, null);

        builder.setView(view).setTitle("Change password")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String oldPass = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String newPassAgain = newPasswordAgain.getText().toString();
                listener.getText(oldPass, newPass, newPassAgain);
            }
        });

        oldPassword = view.findViewById(R.id.oldpassword);
        newPassword = view.findViewById(R.id.newpassword);
        newPasswordAgain = view.findViewById(R.id.newpasswordagain);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ChangePasswordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ChangePasswordDialogListener!");
        }
    }

    public interface ChangePasswordDialogListener{

        void getText(String oldPassword, String newPassword, String newPasswordAgain);
    }
}
