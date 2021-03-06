package in.jelou.candlelight.candlelight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

/**
 * Created by hacker on 27/7/15.
 */
public class CreateCommunity extends FragmentActivity {


    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

    }


    private class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            args.putCharSequence(DemoObjectFragment.ARG_OBJECT, getPageTitle(i));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "New Community";
        }
    }

    public static class DemoObjectFragment extends Fragment implements AdapterView.OnItemSelectedListener {
        public static final String ARG_OBJECT = "object";
        public static int privacycomm = 0;
        public static String countrycomm = "";
        public static String statecomm = "";
        public static EditText name;
        public static EditText city;
        public static String username = "";
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            final SharedPreferences pref = container.getContext()
                    .getSharedPreferences("jy.jelou.candlelight.candlelight",
                            MODE_PRIVATE);
            username = pref.getString("username","jjjjjj");
            Log.d("poi",username);
            LinearLayout rootView = (LinearLayout) inflater.inflate(
                    R.layout.commuintycreate, container, false);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.privacycomm);
            Spinner countryspin = (Spinner) rootView.findViewById(R.id.country);
            Spinner statespin = (Spinner) rootView.findViewById(R.id.state);
            final ArrayAdapter<String> arrayCountryAdapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item, Countrylists.countrylist);
            arrayCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countryspin.setAdapter(arrayCountryAdapter);
            countryspin.setOnItemSelectedListener(this);
            statespin.setOnItemSelectedListener(this);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item);
            arrayAdapter.add("Public");
            arrayAdapter.add("Private");
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(this);
            CardView backButton = (CardView)rootView.findViewById(R.id.backfromcommunitycreate);
            CardView createButton = (CardView) rootView.findViewById(R.id.communitycreate);
            name = (EditText) rootView.findViewById(R.id.commoninewname);
            city = (EditText) rootView.findViewById(R.id.commoninewcity);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),
                            MainScreen.class);
                    startActivity(intent);
                }
            });

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LongOperation().execute("yes");
                }
            });

            return rootView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner) parent;
            if (spinner.getId() == R.id.privacycomm){

            } else if(spinner.getId() == R.id.country) {
                countrycomm = (String) parent.getItemAtPosition(position);
                Spinner statespin = (Spinner) parent.getRootView().findViewById(R.id.state);
                final ArrayAdapter<String> arrayStateAdapter = new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_spinner_item, Countrylists.buildStates(position));
                arrayStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statespin.setAdapter(arrayStateAdapter);
            } else {
                statecomm = (String) parent.getItemAtPosition(position);
            }

        }

        public static String postData() {
            StringBuilder total = new StringBuilder();

            try {
                // create a list to store HTTP variables and their values
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.150.1:8080/registercommunity");

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("a", Integer.toString(privacycomm)));
                nameValuePairs.add(new BasicNameValuePair("a", countrycomm));
                nameValuePairs.add(new BasicNameValuePair("b", statecomm));
                nameValuePairs.add(new BasicNameValuePair("b", name.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("b", city.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("b", username));
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

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            privacycomm = 0;
        }

        private class LongOperation extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                return postData();
            }


            @Override
            protected void onPostExecute(String result) {
                Log.i("sdfsf", result);
                Toast toast = null;
                final SharedPreferences pref = getActivity().getSharedPreferences("jy.jelou.candlelight.candlelight",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String[] resultP = result.split("  ");
                if(resultP[0].equals("Community created")) {
                    editor.putBoolean("ownCommunity", true);
                    editor.putString("oCommunityname", name.getText().toString());
                    editor.putString("oCommunityid", resultP[1]);
                    editor.putString("oCommunitycountry", countrycomm);
                    editor.putString("oCommunitystate", statecomm);
                    editor.putString("oCommunitycity", city.getText().toString());
                    editor.putString("oCommunityprivacy", Integer.toString(privacycomm));
                    editor.commit();
                    toast = Toast.makeText(getActivity(), "Successfully created " + name.getText().toString(), Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(getActivity(), resultP[0], Toast.LENGTH_LONG);
                }
                toast.show();
                Intent intent = new Intent(getActivity().getBaseContext(),
                        MainScreen.class);
                startActivity(intent);
            }
        }
    }
}
