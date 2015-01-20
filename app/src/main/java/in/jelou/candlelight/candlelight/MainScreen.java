package in.jelou.candlelight.candlelight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


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
                    LinearLayout rootView = (LinearLayout) inflater.inflate(
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
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("issignedup", true);
                            editor.putString("username", username.getText()
                                    .toString());
                            editor.putString("email", email.getText()
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
    }
}
