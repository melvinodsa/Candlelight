package in.jelou.candlelight.candlelight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainScreen extends FragmentActivity {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
                getSupportFragmentManager());
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Prayer";
            } else if (position == 1) {
                return "Home";
            } else {
                return "My home";
            }
        }
    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        EditText username, password, email;
        Button signup;
        TextView errorsign;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Bundle args = getArguments();
            if (args.getCharSequence(ARG_OBJECT).equals("Candle")) {
                LinearLayout rootView = (LinearLayout) inflater.inflate(
                        R.layout.welcome, container, false);
                return rootView;
            }
            else{
                final SharedPreferences pref = container.getContext()
                        .getSharedPreferences("jy.jelouodsa.candlelight",
                                MODE_PRIVATE);
                if (!pref.getBoolean("issignedup", false)) {
                    final LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.welcome, container, false);
                    email = (EditText) rootView.findViewById(R.id.email);
                    email.setVisibility(View.VISIBLE);
                    username = (EditText) rootView.findViewById(R.id.username);
                    username.setVisibility(View.VISIBLE);
                    password = (EditText) rootView.findViewById(R.id.password);
                    password.setVisibility(View.VISIBLE);
                    signup = (Button) rootView.findViewById(R.id.signup);
                    signup.setVisibility(View.VISIBLE);
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( isValidEmail(email.getText())){
                                new LongOperation().execute("yes");
                            }
                        }
                    });
                    return rootView;
                }
                else if (!pref.getBoolean("islogedin", false)){
                    LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.welcome, container, false);
                    username = (EditText) rootView.findViewById(R.id.username);
                    username.setVisibility(View.VISIBLE);
                    password = (EditText) rootView.findViewById(R.id.password);
                    password.setVisibility(View.VISIBLE);
                    signup = (Button) rootView.findViewById(R.id.login);
                    signup.setVisibility(View.VISIBLE);
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("islogedin", true);
                            editor.putString("username", username.getText()
                                    .toString());
                            editor.putString("password", password.getText()
                                    .toString());
                            editor.commit();
                            Intent intent = new Intent(v.getContext(),
                                    MainScreen.class);
                            startActivity(intent);
                        }
                    });
                    return rootView;
                }
                else {
                    LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.welcome, container, false);
                    return rootView;
                }
            }
        }

        private boolean isValidEmail(CharSequence target) {
            return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

        private class LongOperation extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... params) {
                postData();
                return null;
            }
        }

        public void postData() {

            try {
                // create a list to store HTTP variables and their values
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.68.182:8080");


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("a", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("a", email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("b", password.getText().toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {

            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }
}
