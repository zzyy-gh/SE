package com.example.karat.Customer.CHome;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.karat.R;
import com.example.karat.inventory.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.Viewholder>{

    private static final String TAG = "StaggeredRecyclerViewAd";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<Double> mDiscount = new ArrayList<>();
    private ArrayList<String> mCategory = new ArrayList<>();
    private static ArrayList<Integer> mListingId = new ArrayList<>();
    private static ArrayList<Integer> mQty = new ArrayList<>();
    private static ArrayList<Double> mPrice = new ArrayList<>();
    private FirebaseDatabase firebaseDB;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Context mContext;

    public StaggeredRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public void reset(ArrayList<Listing> listing){

        mNames.clear();
        mImageUrls.clear();
        mListingId.clear();
        mQty.clear();
        mPrice.clear();
        mDescription.clear();
        mDiscount.clear();
        mCategory.clear();
        for(Listing mlisting: listing) {
            mNames.add(mlisting.getListingName());
            mImageUrls.add(mlisting.getImage_url());
            mListingId.add(mlisting.getListingId());
            mQty.add(Integer.parseInt(mlisting.getListingQuantity()+""));
            mPrice.add(Double.parseDouble(mlisting.getListingPrice()+""));
            mDescription.add(mlisting.getDescription());
            mDiscount.add(Double.parseDouble(mlisting.getListingDiscount()+""));
            mCategory.add(mlisting.getListingCategory());
        }
    }

    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_display, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {

        firebaseDB = FirebaseDatabase.getInstance();
        mDatabase = firebaseDB.getReference();
        mAuth = FirebaseAuth.getInstance();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext).load(mImageUrls.get(position)).apply(requestOptions).into(holder.image);

        holder.name.setText(mNames.get(position));

        holder.price.setText(mPrice.get(position)+"");

        holder.quantity.setText(mQty.get(position)+"");

        holder.category.setText(mCategory.get(position));

        holder.description.setText(mDescription.get(position));

        holder.discount.setText(mDiscount.get(position)+"");

        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mNames.get(position));
            }
        });
        holder.homequantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                holder.minus.setEnabled(true);
                holder.plus.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = holder.homequantity.getText().toString();
                if (text.isEmpty()) {
                }
                else {
                    int value = Integer.parseInt(text);
                    if (value < 0){
                        CharSequence text_2 = "Min Quantity Reached!!!";
                        Toast.makeText(mContext, text_2, Toast.LENGTH_SHORT).show();
                        holder.homequantity.setText(Integer.toString(value + 1));
                        holder.minus.setEnabled(false);


                    }
                    if (value > mQty.get(position)) {
                        CharSequence text_2 = "Max Quantity Reached!!!";
                        Toast.makeText(mContext, text_2, Toast.LENGTH_SHORT).show();
                        holder.homequantity.setText(Integer.toString(value -1));
                        holder.plus.setEnabled(false);

                    }
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.homequantity.getText().toString();
                if (text.isEmpty()){
                }
                else {
                    int value = Integer.parseInt(text);
                    holder.homequantity.setText(Integer.toString(value + 1));
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.homequantity.getText().toString();
                if (text.isEmpty()){
                }
                else {
                    int value = Integer.parseInt(text);
                    if (value > 0) {
                        holder.homequantity.setText(Integer.toString(value - 1));
                    }
                    else {
                        CharSequence text_2 = "Quantity cannot be lower than 0";
                        Toast.makeText(mContext,text_2,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.addtoCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = mAuth.getCurrentUser().getEmail().replace("@", "")
                        .replace(".", "");
                int id = mListingId.get(position);
                mDatabase.child("UserCart").child(email).child(id+"").child("listingId").setValue(id);
                mDatabase.child("UserCart").child(email).child(id+"").child("cartQty")
                        .setValue(Integer.parseInt(holder.homequantity.getText().toString()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder /*extends RecyclerView.ViewHolder */{
        ImageView image;
        TextView name, price, category, description, quantity, discount;
        Button addtoCart;
        EditText homequantity;
        Button plus;
        Button minus;

        public Viewholder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.price_widget);
            this.image = itemView.findViewById(R.id.imageview_widget);
            this.name = itemView.findViewById(R.id.name_widget);
            this.addtoCart = itemView.findViewById(R.id.addtoCart);
            this.homequantity = itemView.findViewById(R.id.homequantity);
            this.plus = itemView.findViewById(R.id.plus2);
            this.minus = itemView.findViewById(R.id.minus2);
            this.category = itemView.findViewById(R.id.category_widget);
            this.description = itemView.findViewById(R.id.description_text);
            this.discount = itemView.findViewById(R.id.disc_widget);
            this.quantity = itemView.findViewById(R.id.qty_widget);
        }
    }
}