package com.tiraza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements OnClickListener,
    GoogleApiClient.OnConnectionFailedListener
{
  private static final String TAG = "LoginEmailGoogle";
  private static final int RC_SIGN_IN = 9001;

  private EditText mEmailField;
  private EditText mPasswordField;
  private TextView mUID;
  private TextView mName;

  // [START declare_auth]
  private FirebaseAuth mGoogleAuth;
  private FirebaseAuth mEmailAuth;
  public  FirebaseAuth.AuthStateListener mAuthListener;
  private GoogleApiClient mGoogleApiClient;
  // [END declare_auth]

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Views
    mEmailField = (EditText) findViewById(R.id.fEmailLogin);
    mPasswordField = (EditText) findViewById(R.id.fPasswordLogin);
    mUID = (TextView) findViewById(R.id.debugUID);
    mName = (TextView) findViewById(R.id.debugName);

    // Buttons
    findViewById(R.id.bLogin).setOnClickListener(this);
    findViewById(R.id.bSignInGoogle).setOnClickListener(this);

    // [START config_signin]
    // Configure Google Sign In
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    // [END config_signin]

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    // [START initialize_auth]
    mGoogleAuth = FirebaseAuth.getInstance();
    mEmailAuth = FirebaseAuth.getInstance();
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

    switch (i)
    {
      case R.id.bLogin:
        // Sign-out first, then login to clear default account caching
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        signInEmail(mEmailField.getText().toString(), mPasswordField.getText().toString());

        // Call next activity

        break;

      case R.id.bSignInGoogle:
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        // Call next activity
        
        break;

      default:

    }
  }

  // LISTENER FOR ACTIVITY RESULT (RC_SIGN_IN) FROM GOOGLE SIGN-IN
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN)
    {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess())
      {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = result.getSignInAccount();
        signInGoogle(account);
      }
    }
  }

  // SIGN IN WITH EMAIL
  private void signInEmail(String email, String password)
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
        String name = "";
        String uid = "";

        if (task.isSuccessful())
        {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithEmail:success");
          Toast.makeText(Login.this, "Email authentication success.", Toast.LENGTH_SHORT).show();
          FirebaseUser user = mEmailAuth.getCurrentUser();

          try
          {
            name = user.getDisplayName();
            uid = user.getUid();
          }

          catch (NullPointerException e)
          {
            Log.e(TAG, "NULL POINTER EXCEPTION ON USER DETAILS");
          }

          mUID.setText("UserID: " + uid);
          mName.setText("DisplayName: " + name);
        }

        else
        {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "signInWithEmail:failure", task.getException());
          Toast.makeText(Login.this, "Email authentication failed.", Toast.LENGTH_SHORT).show();

          mUID.setText("");
          mName.setText("");
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
    mEmailAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, onCompleteListener);
    // [END sign_in_with_email]
  }

  // SIGN IN WITH GOOGLE ACCOUNT
  private void signInGoogle(GoogleSignInAccount acct)
  {
    Log.d(TAG, "signInGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mGoogleAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
          String name = "";
          String uid = "";

          @Override
          public void onComplete(@NonNull Task<AuthResult> task)
          {
            if (task.isSuccessful())
            {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "signInWithCredential:success");
              FirebaseUser user = mGoogleAuth.getCurrentUser();
              Toast.makeText(Login.this, "Google authentication success.", Toast.LENGTH_SHORT).show();

              try
              {
                name = user.getDisplayName();
                uid = user.getUid();
              }

              catch (NullPointerException e)
              {
                Log.e(TAG, "NULL POINTER EXCEPTION ON USER DETAILS");
              }

              mUID.setText("UserID: " + uid);
              mName.setText("DisplayName: " + name);
            }

            else
            {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithCredential:failure", task.getException());
              Toast.makeText(Login.this, "Google authentication failed.", Toast.LENGTH_SHORT).show();

              mUID.setText("");
              mName.setText("");
              //updateUI(null);
            }

            // [START_EXCLUDE]
            //hideProgressDialog();
            // [END_EXCLUDE]
          }
        });
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

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
  {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
    Toast.makeText(Login.this, "Connection error.", Toast.LENGTH_LONG).show();
  }
}

