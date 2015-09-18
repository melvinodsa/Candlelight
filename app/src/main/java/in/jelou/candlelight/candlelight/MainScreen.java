package in.jelou.candlelight.candlelight;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
                return "Home";
            } else if (position == 1) {
                return "Personal";
            } else {
                return "Community";
            }
        }
    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        EditText username, password, email;
        CardView signup;
        TextView errorsign, commintyShow;
        SeekBar bar;
        FloatingActionButton fab;
        CheckBox logsigoption;

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            final SharedPreferences pref = container.getContext()
                    .getSharedPreferences("jy.jelou.candlelight.candlelight",
                            MODE_PRIVATE);
            Bundle args = getArguments();
            if (args.getCharSequence(ARG_OBJECT).equals("Personal") && pref.getBoolean("islogedin", false) ) {
                LinearLayout rootView = (LinearLayout) inflater.inflate(
                        R.layout.personal, container, false);
                bar = (SeekBar) rootView.findViewById(R.id.seekBar1);
                final SharedPreferences.Editor editor = pref.edit();
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        editor.putInt("timespent",progress);
                        editor.commit();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Intent intent = new Intent(seekBar.getContext(),
                                MainScreen.class);
                        startActivity(intent);
                    }
                });
                return rootView;
            } else if (args.getCharSequence(ARG_OBJECT).equals("Community") && pref.getBoolean("islogedin", false) ){
                CoordinatorLayout rootView = (CoordinatorLayout) inflater.inflate(R.layout.community, container, false);
                commintyShow = (TextView) rootView.findViewById(R.id.community_own);
                fab = (FloatingActionButton) rootView.findViewById(R.id.fabbutton);
                if(!pref.getBoolean("ownCommunity",false) && (!pref.getBoolean("joinCommunity1",false) || !pref.getBoolean("joinCommunity2",false))){
                    commintyShow.setText("It seems you have no community. Create or join one");
                    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
                    TextView newcom = new TextView(rootView.getContext());
                    newcom.setText("New");
                    SubActionButton newcombut = itemBuilder.setContentView(newcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    newcombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    CreateCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    TextView joincom = new TextView(rootView.getContext());
                    joincom.setText("Join");
                    SubActionButton joincombut = itemBuilder.setContentView(joincom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    joincombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    JoinCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    TextView followcom = new TextView(rootView.getContext());
                    followcom.setText("Follow");
                    SubActionButton followcombut = itemBuilder.setContentView(followcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    followcombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    FollowCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .setEndAngle(90)
                            .addSubActionView(newcombut)
                            .addSubActionView(joincombut)
                            .addSubActionView(followcombut)
                            .attachTo(fab)
                            .build();
                } else if(pref.getBoolean("ownCommunity",false) && (!pref.getBoolean("joinCommunity1",false) || !pref.getBoolean("joinCommunity2",false))){
                    commintyShow.setText("It seems you can join a community. Join one");
                    show_comminuty_list(container, rootView);
                    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
                    TextView joincom = new TextView(rootView.getContext());
                    joincom.setText("Join");
                    SubActionButton joincombut = itemBuilder.setContentView(joincom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    joincombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    JoinCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    TextView followcom = new TextView(rootView.getContext());
                    followcom.setText("Follow");
                    SubActionButton followcombut = itemBuilder.setContentView(followcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    followcombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    FollowCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .addSubActionView(joincombut)
                            .addSubActionView(followcombut)
                            .attachTo(fab)
                            .build();
                } else if(!pref.getBoolean("ownCommunity",false) && pref.getBoolean("joinCommunity1",false) && pref.getBoolean("joinCommunity2",false)){
                    commintyShow.setText("It seems you can create a community. Create one");
                    show_comminuty_list(container, rootView);
                    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
                    TextView newcom = new TextView(rootView.getContext());
                    newcom.setText("New");
                    SubActionButton newcombut = itemBuilder.setContentView(newcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    newcombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    CreateCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    TextView followcom = new TextView(rootView.getContext());
                    followcom.setText("Follow");
                    SubActionButton followcombut = itemBuilder.setContentView(followcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    followcombut.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    FollowCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .addSubActionView(newcombut)
                            .addSubActionView(followcombut)
                            .attachTo(fab)
                            .build();
                } else if(pref.getBoolean("ownCommunity",false) && pref.getBoolean("joinCommunity1",false) && pref.getBoolean("joinCommunity2",false)){
                    commintyShow.setText("You can view your communities");
                    show_comminuty_list(container, rootView);
                    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
                    TextView followcom = new TextView(rootView.getContext());
                    followcom.setText("Follow");
                    SubActionButton followcombut = itemBuilder.setContentView(followcom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    followcombut.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(v.getContext(),
                                    FollowCommunity.class);
                            startActivity(myIntent);
                        }
                    });
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .addSubActionView(followcombut)
                            .attachTo(fab)
                            .build();
                }

                return rootView;
            }
            else{


                if (pref.getBoolean("islogedin", false)) {
                    final LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.prayer, container, false);
                    int minus = pref.getInt("timespent",0);
                    ImageView im = (ImageView) rootView.findViewById(R.id.angel);
                    signup = (CardView) rootView.findViewById(R.id.logout);
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("islogedin",false);
                            editor.commit();
                            Intent intent = new Intent(v.getContext(),
                                    MainScreen.class);
                            startActivity(intent);
                        }
                    });
                    if(minus < 480){
                        im.setImageDrawable(getResources().getDrawable(R.drawable.angelcry));
                    }
                    else if(minus < 960){
                        im.setImageDrawable(getResources().getDrawable(R.drawable.angelno));
                    }
                    else{
                        im.setImageDrawable(getResources().getDrawable(R.drawable.angel));
                    }
                    WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    ViewGroup.LayoutParams params = im.getLayoutParams();
                    LinearLayout rs = (LinearLayout) rootView.findViewById(R.id.progress);
                    if(size.x < size.y){
                        params.width = size.x/3*2;
                        params.height = size.y/2;
                        rs.setPadding(0,0,(minus*100/1440)*size.x*6/1000,20);
                    } else {
                        params.width = size.y/2;
                        params.height = size.x/5*2;
                        rs.setPadding(0,(minus*100/1440)*size.y*6/1000, 20, 0);
                    }

                    im.setLayoutParams(params);
                    return rootView;
                }
                else{
                    final LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.welcome, container, false);
                    signup = (CardView) rootView.findViewById(R.id.login);
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorsign = (TextView) rootView.findViewById(R.id.errorsign);
                            username = (EditText) rootView.findViewById(R.id.username);
                            password = (EditText) rootView.findViewById(R.id.password);
                            new LongOperation1().execute("yes");
                        }
                    });
                    logsigoption = (CheckBox) rootView.findViewById(R.id.logsigoption);
                    logsigoption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(logsigoption.isChecked()){
                                errorsign = (TextView) rootView.findViewById(R.id.errorsign);
                                email = (EditText) rootView.findViewById(R.id.email);
                                email.setVisibility(View.VISIBLE);
                                username = (EditText) rootView.findViewById(R.id.username);
                                username.setVisibility(View.VISIBLE);
                                password = (EditText) rootView.findViewById(R.id.password);
                                password.setVisibility(View.VISIBLE);
                                signup = (CardView) rootView.findViewById(R.id.login);
                                signup.setVisibility(View.INVISIBLE);
                                signup = (CardView) rootView.findViewById(R.id.signup);
                                signup.setVisibility(View.VISIBLE);
                                signup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (isValidEmail(email.getText())) {
                                            new LongOperation().execute("yes");
                                        }
                                    }
                                });
                            } else {
                                errorsign = (TextView) rootView.findViewById(R.id.errorsign);
                                username = (EditText) rootView.findViewById(R.id.email);
                                username.setVisibility(View.INVISIBLE);
                                username = (EditText) rootView.findViewById(R.id.username);
                                username.setVisibility(View.VISIBLE);
                                password = (EditText) rootView.findViewById(R.id.password);
                                password.setVisibility(View.VISIBLE);
                                signup = (CardView) rootView.findViewById(R.id.signup);
                                signup.setVisibility(View.INVISIBLE);
                                signup = (CardView) rootView.findViewById(R.id.login);
                                signup.setVisibility(View.VISIBLE);
                                signup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (pref.getString("username", null).equals(null)) {
                                            if (pref.getString("username", null).equals(username.getText().toString()) && pref.getString("password", null).equals(password.getText().toString())) {
                                                pref.edit().putBoolean("islogedin", true);
                                                pref.edit().commit();
                                                Intent intent = new Intent(getActivity().getBaseContext(),
                                                        MainScreen.class);
                                                startActivity(intent);
                                            } else {
                                                new LongOperation1().execute("yes");
                                            }
                                        } else {
                                            new LongOperation1().execute("yes");
                                        }

                                    }
                                });
                            }
                        }
                    });

                    return rootView;
                }
            }
        }

        private boolean isValidEmail(CharSequence target) {
            return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

        private class LongOperation extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                return postData();            }


            @Override
            protected void onPostExecute(String result) {
                Log.i("sdfsf", result);
                errorsign.setText(result);
                if (result.equals("User created")){
                    final SharedPreferences pref = getActivity().getSharedPreferences("jy.jelou.candlelight.candlelight",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("islogedin", true);
                    editor.putString("username", username.getText().toString());
                    editor.putString("email",email.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.commit();
                    Log.i("sdfsf", result);
                }
                Intent intent = new Intent(getActivity().getBaseContext(),
                        MainScreen.class);
                startActivity(intent);
            }
        }

        private class LongOperation1 extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                return postData1();            }


            @Override
            protected void onPostExecute(String result) {
                Log.i("sdfsf", result);
                errorsign.setText(result);
                if (result.equals("User is good")){
                    final SharedPreferences pref = getActivity().getSharedPreferences("jy.jelou.candlelight.candlelight",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("islogedin", true);
                    editor.putString("username", username.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.commit();
                    Log.i("sdfsf", result);
                }
                Intent intent = new Intent(getActivity().getBaseContext(),
                        MainScreen.class);
                startActivity(intent);
            }
        }

        public void show_comminuty_list(final ViewGroup container, View rootView){
            CardView view_comminuities = (CardView) rootView.findViewById(R.id.view_community);
            view_comminuities.setVisibility(View.VISIBLE);
            view_comminuities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(container.getContext());
                    builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Select One Name:-");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            container.getContext(),
                            android.R.layout.select_dialog_singlechoice);
                    final SharedPreferences pref = container.getContext()
                            .getSharedPreferences("jy.jelou.candlelight.candlelight",
                                    MODE_PRIVATE);
                    if(pref.getBoolean("ownCommunity", false)){
                        arrayAdapter.add(pref.getString("oCommunityname",null));
                    }
                    if(pref.getBoolean("joinCommunity1", false)){
                        arrayAdapter.add(pref.getString("Community1name",null));
                    }
                    if(pref.getBoolean("joinCommunity2", false)){
                        arrayAdapter.add(pref.getString("Community2name",null));
                    }
                    int count = pref.getInt("followCommunityCount",0);
                    for(int i = 0; i <= count; i++) {
                        arrayAdapter.add(pref.getString("fCname"+Integer.toString(i),null));
                    }
                    builderSingle.setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builderSingle.setAdapter(arrayAdapter,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        /*String strName = arrayAdapter.getItem(which);
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                container.getContext());
                                        builderInner.setMessage(strName);
                                        builderInner.setTitle("Your Selected Item is");
                                        builderInner.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builderInner.show();*/
                                }
                            });
                    builderSingle.show();
                }
            });
        }

        public String postData() {
            StringBuilder total = new StringBuilder();

            try {
                // create a list to store HTTP variables and their values
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.150.1:8080/registeruser");


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("a", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("a", email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("b", password.getText().toString()));
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

        public String postData1() {
            StringBuilder total = new StringBuilder();

            try {
                // create a list to store HTTP variables and their values
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.150.1:8080/loginuser");


                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("a", username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("b", password.getText().toString()));
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
    }
}
