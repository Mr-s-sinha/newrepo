package android.com.chatbox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FloatingActionButton addPost;
    private String current_user_id;
    private FirebaseFirestore firebaseFirestore;

    private BottomNavigationView mainbottomNav;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private NotificationFragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainbottomNav = findViewById(R.id.mainBottomNav);

        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        notificationFragment = new NotificationFragment();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Blog");
            FloatingActionButton fab1 ;

            fab1 =  findViewById(R.id.fab);
            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, NewpostActivity.class));
                }
            });


        if(auth.getCurrentUser() != null) {
            mainbottomNav = findViewById(R.id.mainBottomNav);

            // FRAGMENTS
            homeFragment = new HomeFragment();
            notificationFragment = new NotificationFragment();
            accountFragment = new AccountFragment();

            initializeFragment();

            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);

                    switch (item.getItemId()) {

                        case R.id.home:

                            replaceFragment(homeFragment, currentFragment);
                            return true;

                        case R.id.account:

                            replaceFragment(accountFragment, currentFragment);
                            return true;

                        case R.id.notification:

                            replaceFragment(notificationFragment, currentFragment);
                            return true;

                        default:
                            return false;
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getInstance().getCurrentUser();
        if(currentUser == null) {
            sendToLgin();
        }
        else {

            current_user_id = auth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {

                        }
                    } else {
                        String errormsg = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "error" + errormsg , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void sendToLgin() {
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }

            case R.id.sign_out_menu: {
                logout();
                return true;
            }

            case R.id.profile: {
                startActivity(new Intent(MainActivity.this, SeutpActivity.class));
                return true;
            }
        }
        return true;
    }

    private void logout() {
        auth.signOut();
        sendToLgin();
    }

    private void initializeFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, homeFragment);
        fragmentTransaction.commit();

    }


    private void replaceFragment(HomeFragment fragment, android.support.v4.app.Fragment currentFragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == homeFragment) {

            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);

        }

        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(AccountFragment fragment, android.support.v4.app.Fragment currentFragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (fragment == accountFragment) {

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(NotificationFragment fragment, android.support.v4.app.Fragment currentFragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if(fragment == notificationFragment){

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();

    }
}
