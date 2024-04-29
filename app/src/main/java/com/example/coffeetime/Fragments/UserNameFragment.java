package com.example.coffeetime.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.coffeetime.R;

import org.jetbrains.annotations.Nullable;


public class UserNameFragment extends DialogFragment {
    private String name;
    private OnUserNameSetListener onUserNameSetListener;

    public static UserNameFragment newInstance() {
        return new UserNameFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_user_name, null);
        final EditText etUserName = dialogView.findViewById(R.id.etUserName);
        ConstraintLayout btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> onSubmitClicked(etUserName));

        builder.setView(dialogView);
        setCancelable(false);

        return builder.create();
    }

    private void onSubmitClicked(EditText etUserName) {
        name = etUserName.getText().toString();
        if (name.isEmpty()) {
            etUserName.setError(getString(R.string.error_empty_name));
        } else {
            notifyUserNameSet();
            dismiss();
        }
    }

    private void notifyUserNameSet() {
        if (onUserNameSetListener != null) {
            onUserNameSetListener.onUserNameSet(name);
        }
    }

    public void setOnUserNameSetListener(OnUserNameSetListener listener) {
        this.onUserNameSetListener = listener;
    }
      public void onUserNameSet(String userName) {
        if (onUserNameSetListener != null) {
            onUserNameSetListener.onUserNameSet(userName);
        }
        dismiss(); // Закрываем фрагмент после передачи данных
    }

    public interface OnUserNameSetListener {
        void onUserNameSet(String userName);
    }
}
