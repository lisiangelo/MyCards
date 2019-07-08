package it.j940549.mycards;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {

  FragmentManager fragmentManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    Fragment fragment = Fragment_elenco_cards.newInstance();
    fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flContent_main, fragment).commit();

    BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
        switch (item.getItemId()) {
          case R.id.btn_cards:
            fragment= Fragment_elenco_cards.newInstance();
            setTitle("Le mie Carte");
            break;
          case R.id.btn_depliant:

              fragment=Fragment_depliant.newInstance();
              setTitle("I Depliants");
              break;

        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent_main, fragment).commit();

        return true;
      }

    });


  }


  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_search, menu);

    final MenuItem searchItem = menu.findItem(R.id.action_search);

    SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

    SearchView searchView = null;
    if (searchItem != null) {
      searchView = (SearchView) searchItem.getActionView();
    }
    if (searchView != null) {
      searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
    }


    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String query) {
        // Toast like print
        Log.i("SearchOnQueryTxtSubmit:", query);
        Fragment fragmentSearch = FragmentSearch.newInstance( query);
        //inserisci il fragment rimpiazzando i frgment esitente
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent_main, fragmentSearch).commit();

        // searchItem.collapseActionView();
        return false;
      }
      @Override
      public boolean onQueryTextChange(String s) {
        Log.i( "SearchOnQueryTxtChan:" , s);
        Fragment fragment=null;
        if (s.equals("")) {
          fragment = Fragment_elenco_cards.newInstance();
          //inserisci il fragment rimpiazzando i frgment esitente

        } else {

          fragment= FragmentSearch.newInstance(s);
          //inserisci il fragment rimpiazzando i frgment esitente

        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent_main, fragment).commit();
        return false;
      }
    });
    return true;
  }




  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    Fragment fragment=null;

    if (id == R.id.nav_elenco_cards) {
      fragment=Fragment_elenco_cards.newInstance();
      fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.flContent_main, fragment).commit();
      setTitle("Le mie Carte");

    } else if (id == R.id.nav_inserisci_card) {
//      fragment=Fragment_elenco_cards.newInstance();

    } else if (id == R.id.nav_elenco_depliants) {
      fragment=Fragment_depliant.newInstance();
      fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.flContent_main, fragment).commit();
      setTitle("I Depliants");

    } else if (id == R.id.esci) {
super.onBackPressed();
    }


        item.setChecked(true);

    //imposto il titolo dellìaction bar
      setTitle(item.getTitle());


      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
