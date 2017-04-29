package com.tiraza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class Splash extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    Thread logoTimer = new Thread()
    {
      public void run()
      {
        try
        {
          int logoTimer = 0;
          while (logoTimer < 3000)
          {
            sleep(100);
            logoTimer = logoTimer + 100;
          }
        }

        catch (InterruptedException e)
        {
          e.printStackTrace();
        }

        finally
        {
          finish();
        }
      }
    };

    logoTimer.start();

    startActivity(new Intent(this, Login.class));
  }
}