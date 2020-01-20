package com.xd.demoactiontabs;

import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DialogGeneral extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String DIALOG_KEY = "dialog";
    public static final int CAMERA_DIALOG = 0;
    public static final int TEST_DIALOG = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog ret = null;
        Bundle bundle = getArguments();
        int dialogId = bundle.getInt(DIALOG_KEY);
        if(dialogId == TEST_DIALOG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_general, null);
            builder.setView(view);
            builder.setTitle("my title");
            builder.setPositiveButton("ok", this);
            builder.setNegativeButton("cancel", this);
            ret = builder.create();
        }
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
