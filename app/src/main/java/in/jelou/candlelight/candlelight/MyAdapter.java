package in.jelou.candlelight.candlelight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private String joinOrFollow;
    private SharedPreferences prefs;
    private Context context;
    private String[] communityDetails;


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

        communityDetails = mDataset[position].split(" ");
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
                    if(!prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                        if(!prefs.getString("oCommunityid", null).equals(communityDetails[0])) {
                            new LongOperation().execute(communityDetails[0], prefs.getString("username", null));
                        } else {
                            Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you created this community.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else if(prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                        if(!prefs.getString("Community1id", null).equals(communityDetails[0])) {
                            new LongOperation().execute(communityDetails[0], prefs.getString("username", null));
                        } else {
                            Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you have already joined this community.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                } else {
                    int count = prefs.getInt("followCommunityCount",0);
                    Boolean flag = false;
                    for (int i = 0; i < count; i++) {
                        if(prefs.getString("fCid"+Integer.toString(count), null).equals(communityDetails[0])) {
                            flag = true;
                        }
                    }
                    if(!flag) {
                        if(!prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                            if(!prefs.getString("oCommunityid", null).equals(communityDetails[0])) {
                                new LongOperation1().execute(communityDetails[0], prefs.getString("username", null));
                            } else {
                                Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you created this community.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else if(prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                            if(!prefs.getString("Community1id", null).equals(communityDetails[0])) {
                                new LongOperation1().execute(communityDetails[0], prefs.getString("username", null));
                            } else {
                                Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you have already joined this community.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else if(prefs.getBoolean("joinCommunity1",false) && prefs.getBoolean("joinCommunity2",false)) {
                            if (!prefs.getString("Community1id", null).equals(communityDetails[0]) || !prefs.getString("Community2id", null).equals(communityDetails[0])) {
                                new LongOperation1().execute(communityDetails[0], prefs.getString("username", null));
                            } else {
                                Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you have already joined this community.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(context.getApplicationContext(), "Sorry you have already followed this community.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static String postData(String... params) {
        StringBuilder total = new StringBuilder();

        try {
            // create a list to store HTTP variables and their values
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.150.1:8080/joincommunity");

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


            nameValuePairs.add(new BasicNameValuePair("a", params[0]));
            nameValuePairs.add(new BasicNameValuePair("b", params[1]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return  total.toString();
    }

    public static String postData1(String... params) {
        StringBuilder total = new StringBuilder();

        try {
            // create a list to store HTTP variables and their values
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.150.1:8080/followcommunity");

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


            nameValuePairs.add(new BasicNameValuePair("a", params[0]));
            nameValuePairs.add(new BasicNameValuePair("b", params[1]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return  total.toString();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return postData(params);
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i("oooooooo", result);
            Toast toast = null;
            if("Joined community".equals(result)) {
                final SharedPreferences pref = context.getSharedPreferences("jy.jelou.candlelight.candlelight",
                        context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (!prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                    editor.putString("Community1name", communityDetails[1]);
                    editor.putString("Community1id", communityDetails[0]);
                    editor.putString("Community1country", communityDetails[2]);
                    editor.putString("Community1state", communityDetails[3]);
                    editor.putString("Community1city", communityDetails[4]);
                    editor.putBoolean("joinCommunity1", true);
                    editor.commit();
                    toast = Toast.makeText(context.getApplicationContext(), "Successfully joined " + communityDetails[1], Toast.LENGTH_LONG);
                } else if(prefs.getBoolean("joinCommunity1",false) && !prefs.getBoolean("joinCommunity2",false)) {
                    editor.putString("Community2name", communityDetails[1]);
                    editor.putString("Community2id", communityDetails[0]);
                    editor.putString("Community2country", communityDetails[2]);
                    editor.putString("Community2state", communityDetails[3]);
                    editor.putString("Community2city", communityDetails[4]);
                    editor.putBoolean("joinCommunity2", true);
                    editor.commit();
                    toast = Toast.makeText(context.getApplicationContext(), "Successfully joined "+communityDetails[1] , Toast.LENGTH_LONG);
                }
            } else {
                toast = Toast.makeText(context.getApplicationContext(), result , Toast.LENGTH_LONG);
            }
            toast.show();
            Intent intent = new Intent(context,
                    MainScreen.class);
            context.startActivity(intent);
        }
    }

    private class LongOperation1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return postData1(params);
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i("oooooooo", result);
            Toast toast = null;
            if("Followed community".equals(result)) {
                final SharedPreferences pref = context.getSharedPreferences("jy.jelou.candlelight.candlelight",
                        context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                int count = pref.getInt("followCommunityCount",0);
                count++;
                editor.putString("fCname"+Integer.toString(count), communityDetails[1]);
                editor.putString("fCid"+Integer.toString(count), communityDetails[0]);
                editor.putString("fCcountry"+Integer.toString(count), communityDetails[2]);
                editor.putString("fCstate"+Integer.toString(count), communityDetails[3]);
                editor.putString("fCstate" + Integer.toString(count), communityDetails[4]);
                editor.putInt("followCommunityCount", count);
                editor.commit();

                toast = Toast.makeText(context.getApplicationContext(), "Successfully followed "+communityDetails[1] , Toast.LENGTH_LONG);

            } else {
                toast = Toast.makeText(context.getApplicationContext(), result , Toast.LENGTH_LONG);
            }
            toast.show();
            Intent intent = new Intent(context,
                    MainScreen.class);
            context.startActivity(intent);
        }
    }
}