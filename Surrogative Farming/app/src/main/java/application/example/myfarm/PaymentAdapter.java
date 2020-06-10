package application.example.myfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ReportViewHolder> {

    ArrayList<PaymentClass> paymentList;

    public PaymentAdapter(ArrayList<PaymentClass> paymentList){
        this.paymentList = new ArrayList<>();
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card_item,parent,false);
        ReportViewHolder fvh = new ReportViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

            PaymentClass paymentClass = paymentList.get(position);
            Picasso.get().load(paymentClass.getImgUrl()).into(holder.imageView);
            holder.senderName.setText(paymentClass.getSenderName());
            holder.text.setText(paymentClass.getMessage());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateString = formatter.format(new Date((paymentClass.getTimeStamp())));
            holder.timeText.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class ReportViewHolder extends  RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView text;
        public TextView senderName;
        public  TextView timeText;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_payment);
            text = itemView.findViewById(R.id.text_payment);
            senderName = itemView.findViewById(R.id.sender_payment);
            timeText = itemView.findViewById(R.id.time_payment);
        }

    }

}
