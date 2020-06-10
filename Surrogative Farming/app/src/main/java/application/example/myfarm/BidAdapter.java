package application.example.myfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidViewHolder> {

    ArrayList<BidClass> bidsList;

    public BidAdapter(ArrayList<BidClass> bidsList){
        this.bidsList = new ArrayList<>();
        this.bidsList = bidsList;
    }


    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteItem(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener1){
        listener = listener1;
    }


    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_card_item,parent,false);
        BidViewHolder fvh = new BidViewHolder(v, listener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {

        BidClass bid = bidsList.get(position);
        holder.bidPrice.setText(bid.getBidAmount());

    }

    @Override
    public int getItemCount() {
        return bidsList.size();
    }

    public static class BidViewHolder extends  RecyclerView.ViewHolder{

        public  TextView bidPrice;
        public Button viewButton;
        public Button deleteButton;

        public BidViewHolder(@NonNull View itemView, final OnItemClickListener listener1) {
            super(itemView);


            bidPrice = itemView.findViewById(R.id.bid_price);
            viewButton = itemView.findViewById(R.id.view_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener1 != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener1.onItemClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener1 != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener1.onDeleteItem(position);
                        }
                    }
                }
            });
        }

    }
}
