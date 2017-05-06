package com.tiraza;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by agustio on 06/05/17.
 */

public class Search extends AppCompatActivity implements View.OnClickListener
{
  @Override
  public void onClick(View v)
  {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);

    return true;
  }
}
