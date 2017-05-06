package com.tiraza;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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
  public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
  {
    super.onCreate(savedInstanceState, persistentState);
  }

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
