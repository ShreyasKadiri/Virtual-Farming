package application.example.myfarm;

import android.util.Log;
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

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    ArrayList<ReportClass> reportList;

    public ReportAdapter(ArrayList<ReportClass> reportList){
        this.reportList = new ArrayList<>();
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card_item,parent,false);
        ReportViewHolder fvh = new ReportViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

            ReportClass reportClass = reportList.get(position);
            Picasso.get().load(reportClass.getImgUrl()).into(holder.imageView);
            holder.senderName.setText(reportClass.getSenderName());
            holder.text.setText(reportClass.getMessage());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateString = formatter.format(new Date((reportClass.getTimeStamp())));
            holder.timeText.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends  RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView text;
        public TextView senderName;
        public  TextView timeText;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_report);
            text = itemView.findViewById(R.id.text_report);
            senderName = itemView.findViewById(R.id.sender_report);
            timeText = itemView.findViewById(R.id.time_report);
        }

    }

}
