package com.code2bind.shopzen.ui.contact;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.databinding.FragmentContactBinding;
import com.code2bind.shopzen.models.DBHelper;
import com.code2bind.shopzen.models.contact.Contact;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText name = (EditText) root.findViewById(R.id.nameEditText);
        EditText phone = (EditText) root.findViewById(R.id.phoneEditText);
        EditText email = (EditText) root.findViewById(R.id.emailEditText);
        Button saveButton = (Button) root.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(container.getContext());
                dbHelper.addContactData(new Contact(name.getText().toString(), phone.getText().toString(), email.getText().toString()));
                AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
                builder.setTitle("Contact added");
                builder.setMessage("Contact successfully added to database.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog if needed
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                name.setText("");
                phone.setText("");
                email.setText("");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}