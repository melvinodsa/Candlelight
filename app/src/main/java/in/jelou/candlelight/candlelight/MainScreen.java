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
import android.widget.Button;
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
        Button signup;
        TextView errorsign, commintyShow;
        SeekBar bar;
        FloatingActionButton fab;

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            final SharedPreferences pref = container.getContext()
                    .getSharedPreferences("jy.jelou.candlelight.candlelight",
                            MODE_PRIVATE);
            Bundle args = getArguments();
            if (args.getCharSequence(ARG_OBJECT).equals("Personal") && pref.getBoolean("issignedup", false) ) {
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
            } else if (args.getCharSequence(ARG_OBJECT).equals("Community") && pref.getBoolean("issignedup", false) ){
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
                    TextView joincom = new TextView(rootView.getContext());
                    joincom.setText("Join");
                    SubActionButton joincombut = itemBuilder.setContentView(joincom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .setEndAngle(90)
                            .addSubActionView(newcombut)
                            .addSubActionView(joincombut)
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
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .addSubActionView(joincombut)
                            .attachTo(fab)
                            .build();
                } else if(!pref.getBoolean("ownCommunity",false) && pref.getBoolean("joinCommunity1",false) && pref.getBoolean("joinCommunity2",false)){
                    commintyShow.setText("It seems you can create a community. Create one");
                    show_comminuty_list(container, rootView);
                    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
                    TextView joincom = new TextView(rootView.getContext());
                    joincom.setText("New");
                    SubActionButton joincombut = itemBuilder.setContentView(joincom)
                            .setLayoutParams(new FrameLayout.LayoutParams(70, 70, 1)).build();
                    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                            .setRadius(80)
                            .setStartAngle(180)
                            .addSubActionView(joincombut)
                            .attachTo(fab)
                            .build();
                } else if(pref.getBoolean("ownCommunity",false) && pref.getBoolean("joinCommunity1",false) && pref.getBoolean("joinCommunity2",false)){
                    commintyShow.setText("You can view your communities");
                    show_comminuty_list(container, rootView);
                }

                return rootView;
            }
            else{


                if (pref.getBoolean("issignedup", false)) {
                    final LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.prayer, container, false);
                    int minus = pref.getInt("timespent",0);
                    ImageView im = (ImageView) rootView.findViewById(R.id.angel);
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
                else if (!pref.getBoolean("issignedup", false)) {
                    final LinearLayout rootView = (LinearLayout) inflater.inflate(
                            R.layout.welcome, container, false);
                    errorsign = (TextView) rootView.findViewById(R.id.errorsign);
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
                            if (isValidEmail(email.getText())) {
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

        private class LongOperation extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                return postData();            }


            @Override
            protected void onPostExecute(String result) {
                Log.i("sdfsf", result);
                errorsign.setText(result);
                final SharedPreferences pref = getActivity().getSharedPreferences("jy.jelou.candlelight.candlelight",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("issignedup", true);
                editor.commit();
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
                    arrayAdapter.add("Hardik");
                    arrayAdapter.add("Archit");
                    arrayAdapter.add("Jignesh");
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
    }
}
