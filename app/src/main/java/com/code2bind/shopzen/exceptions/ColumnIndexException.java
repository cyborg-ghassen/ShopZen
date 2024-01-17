package com.code2bind.shopzen.exceptions;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.sql.SQLException;

public class ColumnIndexException extends SQLException {
    private String message;

    public ColumnIndexException(String message){
        super(message);
        this.message = message;
    }

    public void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Column Index Exception");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the dialog if needed
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Log the exception for additional debugging information
        Log.e("ColumnIndexException", message);
    }
}
