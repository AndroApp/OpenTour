package org.wultimaproject.db2.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;

import org.wultimaproject.db2.R;

/**
 * Created by antoniocoppola on 22/07/15.
 */
public class GPSDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_insufficient_gps, null);

        builder.setView(view);

        // Click on OK
        builder.setPositiveButton(R.string.confirmGPSlaunch, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        return builder.create();
    }



}//fine classe
