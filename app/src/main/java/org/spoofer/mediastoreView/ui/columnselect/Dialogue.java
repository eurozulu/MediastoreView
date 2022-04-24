package org.spoofer.mediastoreView.ui.columnselect;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import org.spoofer.mediastoreView.R;

public class Dialogue {
    public static AlertDialog createApplyChangesDialog(Activity activity, DialogInterface.OnClickListener onOkListener) {
        return new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_apply_title)
                .setMessage(R.string.confirm_apply)
                .setPositiveButton(R.string.action_apply, onOkListener)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                })
                .setCancelable(true)
                .create();
    }
}
