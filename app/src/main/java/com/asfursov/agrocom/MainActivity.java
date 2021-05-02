package com.asfursov.agrocom;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.state.AppData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @BindView(R.id.userNameHeader)
    TextView username;
    @BindView(R.id.userRolesHeader)
    TextView roles;
    @BindView(R.id.credentialsView)
    TextView creds;

    MenuItem menuHome;
    MenuItem menuLogin;
    MenuItem menuLogout;

    public NavController getNavController() {
        return navController;
    }

    NavController navController;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_settings) {
            getNavController().navigate(R.id.settingsFragment);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppData.getInstance().setContext(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login, R.id.nav_home, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        ButterKnife.bind(this, header);

        Menu menu = navigationView.getMenu();
        menuLogin = menu.findItem(R.id.nav_login);
        menuLogout = menu.findItem(R.id.nav_logout);
        menuHome = menu.findItem(R.id.nav_home);
        creds.setText(R.string.copyrightCreds);
        creds.setText(String.format(creds.getText().toString(), BuildConfig.VERSION_NAME));
        UpdateUser();
        UpdateMenu();
    }

    public void UpdateMenu() {

        UserData user = AppData.getInstance().getUser();

        if(user==null)
        {
            menuLogin.setEnabled(true);
            menuLogout.setEnabled(false);
            menuHome.setEnabled(false);
            menuLogin.setChecked(true);
            navController.navigate(R.id.nav_login);
        }
        else{
            menuLogin.setEnabled(false);
            menuLogout.setEnabled(true);
            menuHome.setEnabled(true);
            menuHome.setChecked(true);

        }
    }

    public void UpdateUser() {
        UserData user = AppData.getInstance().getUser();
        if(user!=null) {
            username.setText(user.getName());
            roles.setText(user.getRolesAsText());
        }
        else
        {
            username.setText("Пользователь не авторизован");
            roles.setText("Роли не определены");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}