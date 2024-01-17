package com.code2bind.shopzen.exceptions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.sql.SQLException;

public class ItemDoesNotExistsException extends SQLException {
    private String message;

    public ItemDoesNotExistsException(String message){
        super(message);
        this.message = message;
    }

    @SuppressLint("LongLogTag")
    public void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Item Does Not Exist Exception");
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
        Log.e("ItemDoesNotExistsException", message);
    }
}
