package in.jelou.candlelight.candlelight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private String joinOrFollow;
    private SharedPreferences prefs;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout linLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            linLayout = v;
        }
    }

    public MyAdapter(String[] myDataset, String join, SharedPreferences pref, Context context) {
        mDataset = myDataset ;
        joinOrFollow = join;
        prefs = pref;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.communitylist, parent, false);

        ViewHolder vh = new ViewHolder((LinearLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView communityName = (TextView) holder.linLayout.findViewById(R.id.community_name);
        TextView communityCountry = (TextView) holder.linLayout.findViewById(R.id.community_country);
        TextView communityState = (TextView) holder.linLayout.findViewById(R.id.community_state);
        TextView communityCity = (TextView) holder.linLayout.findViewById(R.id.community_city);
        TextView communityId  = (TextView) holder.linLayout.findViewById(R.id.community_id);

        final String[] communityDetails = mDataset[position].split(" ");
        Log.i("aaaa", communityDetails[0] + communityDetails[1] + communityDetails[2]);
        communityName.setText(communityDetails[1]);
        communityId.setText(communityDetails[0]);
        communityCountry.setText(communityDetails[2]);
        communityState.setText(communityDetails[3]);
        communityCity.setText(communityDetails[4]);

        CardView joinButton = (CardView) holder.linLayout.findViewById(R.id.searchjoinCommunity);
        TextView tv = (TextView) joinButton.findViewById(R.id.joinOrSearch);
        tv.setText(joinOrFollow);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor  =prefs.edit();
                if(joinOrFollow.equals("join")){
                    if(!prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)){/*
                        editor.putString("Community1name", communityDetails[1]);
                        editor.putString("Community1id", communityDetails[0]);
                        editor.putString("Community1country", communityDetails[2]);
                        editor.putString("Community1state", communityDetails[3]);
                        editor.putString("Community1state", communityDetails[4]);
                        editor.putBoolean("joinCommunity1", true);*/
                        Intent intent = new Intent(context,
                                MainScreen.class);
                        context.startActivity(intent);

                    } else {/*
                        editor.putString("Community2name", communityDetails[1]);
                        editor.putString("Community2id", communityDetails[0]);
                        editor.putString("Community2country", communityDetails[2]);
                        editor.putString("Community2state", communityDetails[3]);
                        editor.putString("Community2state", communityDetails[4]);
                        editor.putBoolean("joinCommunity2", true);*/
                        Intent intent = new Intent(v.getContext().getApplicationContext(),
                                MainScreen.class);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}