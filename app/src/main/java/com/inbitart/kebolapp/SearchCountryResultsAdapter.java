package com.inbitart.kebolapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inbitart.kebolapp.model.Country;

import java.util.ArrayList;

/**
 * Created by XandrOSS on 06/08/2016.
 */
public class SearchCountryResultsAdapter extends RecyclerView.Adapter<
        SearchCountryResultsAdapter.CountryCodeViewHolder> {

    private Context mContext;
    private ArrayList<Country> data;
    private static CountryCodeItemClickListener countryCodeItemClickListener;

    public SearchCountryResultsAdapter(Context context, ArrayList<Country> data) {
        this.data = data;
        mContext = context;
    }

    public static class CountryCodeViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context vContext;
        private TextView txtCountryName;
        private TextView txtDescription;
        private TextView txtCode;

        // constructor for CountryCodeViewHolder
        public CountryCodeViewHolder(Context context, View itemView) {
            super(itemView);
            vContext = context;

            itemView.setOnClickListener(this);

            txtCountryName = (TextView) itemView.findViewById(R.id.txtCountryName);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
        }

        public void bindCountry(Country country) {
            txtCountryName.setText(country.getName());
            txtDescription.setText(country.getDescription());
            txtCode.setText("+"+country.getCode());

//            txtGameName.setText(game.getShortName());
//            txtDistributor.setText(game.getDistributor());
//            // load image into view
//            Glide.with(vContext).load(game.getImageBytes()).
//                    centerCrop().into(imgGameThumb);

//            try {
//                if (getFavoritesDatabaseHelper().isFavorite(game.getId())) {
//                    imgGameFavorite.setImageResource(R.drawable.ic_star_filled);
//                } else imgGameFavorite.setImageResource(R.drawable.ic_star_empty);
//            } catch (SQLException e) {
//                e.printStackTrace();
//                imgGameFavorite.setImageResource(R.drawable.ic_star_empty);
//            }
//
//            if (favoritesDatabaseHelper != null) {
//                favoritesDatabaseHelper.close();
//                favoritesDatabaseHelper = null;
//            }
        }

        @Override
        public void onClick(View view) {
            countryCodeItemClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(CountryCodeItemClickListener countryCodeItemClickListener) {
        this.countryCodeItemClickListener = countryCodeItemClickListener;
    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_code_list_item_1, parent, false);
        CountryCodeViewHolder ccvh = new CountryCodeViewHolder(mContext, itemView);
        return ccvh;
       // return null;
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder holder, int position) {
        Country item = data.get(position);
        holder.bindCountry(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
       // return 0;
    }

    public void refreshAdapterData(ArrayList<Country> data) {
        this.data = data;
    }
}
