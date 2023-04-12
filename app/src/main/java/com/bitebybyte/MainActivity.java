package com.bitebybyte;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bitebybyte.backend.database.UserService;
import com.bitebybyte.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth        auth = FirebaseAuth.getInstance();
    private UserService userService = new UserService();
    private Toolbar             toolbar;
    private AppBarConfiguration appBarConfiguration;
    private NavController        navController;
    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;

    private NavigationView sideBar;
    private TextView sideBarEmail;
    private TextView sideBarUsername;

    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            bottomNavigationView = findViewById(R.id.bottom_nav_view);
            sideBar = findViewById(R.id.side_bar);
            drawerLayout = findViewById(R.id.drawer_layout);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            //setting the email on the sidebar
            sideBarEmail = sideBar.getHeaderView(0).findViewById(R.id.user_email_sidebar);
            sideBarEmail.setText(currentUser.getEmail());

            //setting the username on the sidebar
            sideBarUsername = sideBar.getHeaderView(0).findViewById(R.id.user_name_sidebar);
            sideBarUsername.setText(userService.getCurrentUsername());

            appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_create, R.id.navigation_saved).setOpenableLayout(drawerLayout).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(sideBar, navController);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Disable the bottom navigation bar according to the argument "showBottomBar"
            navController.addOnDestinationChangedListener((navController, destination, arguments) -> {
                boolean showBottomBar = true; // show bottomBar by default

                // Update showBottomBar if it is specified in the navigation graph
                if (arguments != null && arguments.containsKey("showBottomBar"))
                {
                    showBottomBar = arguments.getBoolean("showBottomBar");
                }
                bottomNavigationView.setVisibility(showBottomBar ? View.VISIBLE : View.GONE);
            });

            // Remove hamburger icon when on a top level fragment
            navController.addOnDestinationChangedListener(
                    (navController, destination, arguments) -> {
                        int id = destination.getId();

                        if (id == R.id.navigation_home ||
                            id == R.id.navigation_create ||
                            id == R.id.navigation_saved)
                        {
                            toolbar.setNavigationIcon(null);
                        }

                    });
            settingsButton =  sideBar.getHeaderView(0).findViewById(R.id.account_settings_button);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.app_bar_settings)
        {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pressLogoutButton(MenuItem item) {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}