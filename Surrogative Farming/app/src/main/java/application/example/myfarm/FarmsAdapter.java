package application.example.myfarm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FarmsAdapter extends RecyclerView.Adapter<FarmsAdapter.FarmViewHolder> {

    ArrayList<FarmerClass> farmsList;

    public FarmsAdapter(ArrayList<FarmerClass> farmsList){
        this.farmsList = new ArrayList<>();
        this.farmsList = farmsList;
    }


    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener1){
        listener = listener1;
    }


    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_card_item,parent,false);
        FarmViewHolder fvh = new FarmViewHolder(v, listener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {

            FarmerClass farmerClass = farmsList.get(position);
            Picasso.get().load(farmerClass.getFarmImgUrl()).into(holder.farmImage);
            holder.farmerName.setText(farmerClass.getName());
            holder.city.setText("BANGALORE");
            holder.basePrice.setText(farmerClass.getBasePrice());
            holder.cropsGrown.setText(farmerClass.getCropsGrown());
            holder.irrigation.setText(farmerClass.getIrrigation());
            holder.phoneNumber.setText(farmerClass.getPhoneNumber());
            Log.d("RUM",farmerClass.getName());

    }

    @Override
    public int getItemCount() {
        return farmsList.size();
    }

    public static class FarmViewHolder extends  RecyclerView.ViewHolder{
        public ImageView farmImage;
        public TextView city;
        public TextView farmerName;
        public  TextView basePrice;
        public TextView cropsGrown;
        public TextView irrigation;
        public TextView phoneNumber;
        public ImageView phoneCallImg;
        public Button selectButton;

        public FarmViewHolder(@NonNull View itemView, final OnItemClickListener listener1) {
            super(itemView);
            farmImage = itemView.findViewById(R.id.farm_image);
            city = itemView.findViewById(R.id.city_name_fc);
            farmerName = itemView.findViewById(R.id.farmer_name_fc);
            basePrice = itemView.findViewById(R.id.base_price_fc);
            cropsGrown = itemView.findViewById(R.id.crops_grown_fc);
            irrigation = itemView.findViewById(R.id.irrigation_type_fc);
            phoneNumber = itemView.findViewById(R.id.phone_number_fc);
            phoneCallImg = itemView.findViewById(R.id.phone_button_fc);
            selectButton = itemView.findViewById(R.id.select_button_fc);
            selectButton.setOnClickListener(new View.OnClickListener() {
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
        }

    }

}
