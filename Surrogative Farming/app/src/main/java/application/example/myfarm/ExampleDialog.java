package application.example.myfarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.zip.Inflater;

public class ExampleDialog extends AppCompatDialogFragment {

    private EditText bidAmountEditText;
    private ExampleDialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view)
                .setTitle("Do you want to bid for this farm ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String b = bidAmountEditText.getText().toString();
                        dialogListener.setBidAmount(b);
                    }
                });
        bidAmountEditText = view.findViewById(R.id.bid_price);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{

            dialogListener = (ExampleDialogListener) context;

        }catch (Exception e){

        }
    }

    public interface ExampleDialogListener{
        void setBidAmount(String bidAmount);
    }

}
