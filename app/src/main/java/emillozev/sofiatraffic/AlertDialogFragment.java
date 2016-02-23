package emillozev.sofiatraffic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Location Needed!")
                .setMessage("Turn on your location!")
                .setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
