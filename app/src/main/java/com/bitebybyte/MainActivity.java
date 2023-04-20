package com.bitebybyte;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Represents the main activity of the app.
 * All fragments are hosted in this activity.
 * This activity is the entry point of the app.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private UserService userService;
    private Toolbar toolbar;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;

    private NavigationView sideBar;

    /**
     * Initializes the activity and sets up the UI components.
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        //If the user is not logged in, redirect them to the login page
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        userService = new UserService();

        setupUI(currentUser);
        setupNavController();
        disableBottomBar(navController);
        removeHamburgerIcon(navController);
        setupSettingsButton();
    }

    /**
     * Sets up the UI components.
     */
    private void setupUI(FirebaseUser currentUser) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        sideBar = findViewById(R.id.side_bar);
        drawerLayout = findViewById(R.id.drawer_layout);

        //setting the email on the sidebar
        TextView sideBarEmail = sideBar.getHeaderView(0).findViewById(R.id.user_email_sidebar);
        sideBarEmail.setText(currentUser.getEmail());

        //setting the username on the sidebar
        TextView sideBarUsername = sideBar.getHeaderView(0).findViewById(R.id.user_name_sidebar);
        sideBarUsername.setText(userService.getCurrentUsername());

        ImageView sideBarProfilePicture = sideBar.getHeaderView(0).findViewById(R.id.profile_picture_sidebar);
        PostService postService = new PostService();
        postService.loadImage(sideBarProfilePicture, userService.getCurrentUserId(), "pfPictures/");
    }

    /**
     * Sets up the navigation controller.
     */
    private void setupNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_create, R.id.navigation_saved)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(sideBar, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    /**
     * Sets up the settings button.
     */
    private void setupSettingsButton() {
        View headerView = sideBar.getHeaderView(0);
        headerView.setOnClickListener(event -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }
    
    /**
     * Handle the search query from the user.
     * 
     * @post query gets passed to the backend
     */
    private void handleSearchQuery() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            // TODO: pass the query to the backend
           navController.navigate(R.id.action_navigation_home_to_navigation_search);

        }
    }

    /**
     * Disables the bottom navigation bar according to the argument "showBottomBar".
     *
     * @param navController Navigation controller for the activity
     */
    private void disableBottomBar(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean showBottomBar = true; // show bottomBar by default

            // Update showBottomBar if it is specified in the navigation graph
            if (arguments != null && arguments.containsKey("showBottomBar")) {
                showBottomBar = arguments.getBoolean("showBottomBar");
            }
            bottomNavigationView.setVisibility(showBottomBar ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * Removes the hamburger icon when on a top level fragment.
     *
     * @param navController Navigation controller for the activity
     */
    private void removeHamburgerIcon(NavController navController) {
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    int id = destination.getId();

                    if (id == R.id.navigation_home ||
                            id == R.id.navigation_create ||
                            id == R.id.navigation_saved) {
                        toolbar.setNavigationIcon(null);
                    }

                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView    searchView    = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_settings) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pressLogoutButton(MenuItem item) {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}