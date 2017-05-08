package com.tiraza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class Splash extends AppCompatActivity
{
  Thread logoTimer = new Thread()
  {
    public void run()
    {
      int logoTimer = 0;
      int logoTimer2 = 0;
      while (logoTimer < 300000000)
      {
        while (logoTimer < 300000000)
        {
          logoTimer2 = logoTimer2++;
        }
        logoTimer = logoTimer++;
      }
    }
  };

    @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    logoTimer.start();

    Intent intent = new Intent(this, Login.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }
}