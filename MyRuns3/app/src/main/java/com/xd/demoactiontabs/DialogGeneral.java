package com.xd.demoactiontabs;

import android.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DialogGeneral extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String DIALOG_KEY = "dialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog ret = null;
        Bundle bundle = getArguments();
        int dialogId = bundle.getInt(DIALOG_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_general, null);
        builder.setView(view);
        if (dialogId == 0) {
            builder.setTitle("Duration");
        } else if (dialogId == 1) {
            builder.setTitle("Distance");
        } else if (dialogId == 2) {
            builder.setTitle("Calories");
        } else if (dialogId == 3) {
            builder.setTitle("Heart Rate");
        } else {
            builder.setTitle("Comment");
            EditText edt = (EditText) view.findViewById(R.id.editText);
            edt.setRawInputType(InputType.TYPE_CLASS_TEXT);
            edt.setHeight(200);
            edt.setHint("How did it go? Notes here.");
        }
        builder.setPositiveButton("ok", this);
        builder.setNegativeButton("cancel", this);
        ret = builder.create();
        return ret;
    }

    public void onClick(DialogInterface dialog, int item){
        if(item == DialogInterface.BUTTON_POSITIVE){
            Toast.makeText(getActivity(), "ok clicked", Toast.LENGTH_LONG).show();
        }else if(item == DialogInterface.BUTTON_NEGATIVE) {
            Toast.makeText(getActivity(), "cancel clicked", Toast.LENGTH_LONG).show();
        }
    }
}
