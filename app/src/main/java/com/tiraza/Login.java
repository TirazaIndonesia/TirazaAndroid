package com.tiraza;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements OnClickListener
{
  private static final String TAG = "EmailPassword";

  private EditText mEmailField;
  private EditText mPasswordField;

  // [START declare_auth]
  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  // [END declare_auth]

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Views
    mEmailField = (EditText) findViewById(R.id.fEmailLogin);
    mPasswordField = (EditText) findViewById(R.id.fPasswordLogin);

    // Buttons
    findViewById(R.id.bLogin).setOnClickListener(this);

    // [START initialize_auth]
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]

    mAuthListener = new FirebaseAuth.AuthStateListener()
    {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
      {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
          // User is signed in
          Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else
        {
          // User is signed out
          Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...
      }
    };
  }

  @Override
  public void onClick(View v)
  {
    int i = v.getId();
    if (i == R.id.bLogin)
    {
      signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }
  }

  private void signIn(String email, String password)
  {
    Log.d(TAG, "signIn:" + email);
    if (!validateForm())
    {
      return;
    }

    OnCompleteListener onCompleteListener = new OnCompleteListener()
    {
      public void onComplete(@NonNull Task task)
      {
        if (task.isSuccessful())
        {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithEmail:success");
          FirebaseUser user = mAuth.getCurrentUser();

          try
          {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
          }

          catch (NullPointerException e)
          {
            Log.e(TAG, "NULL POINTER EXCEPTION ON USER DETAILS");
          }
        }

        else
        {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "signInWithEmail:failure", task.getException());
          Toast.makeText(Login.this, "Authentication failed.",
              Toast.LENGTH_SHORT).show();
        }

        // [START_EXCLUDE]
        if (!task.isSuccessful())
        {
          Log.w(TAG, "signInWithEmail:failure", task.getException());
        }
        // [END_EXCLUDE]
      }
    };

    // [START sign_in_with_email]
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, onCompleteListener);
    // [END sign_in_with_email]
  }

  private boolean validateForm()
  {
    boolean valid = true;

    String email = mEmailField.getText().toString();
    if (TextUtils.isEmpty(email))
    {
      mEmailField.setError("Required.");
      valid = false;
    } else
    {
      mEmailField.setError(null);
    }

    String password = mPasswordField.getText().toString();
    if (TextUtils.isEmpty(password))
    {
      mPasswordField.setError("Required.");
      valid = false;
    } else
    {
      mPasswordField.setError(null);
    }

    return valid;
  }
}

