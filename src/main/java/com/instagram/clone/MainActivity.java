/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.instagram.clone;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;
//import static com.parse.starter.R.id.signupButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  boolean signupModeActive = true;

  TextView changeSignupModeTextView;

  Button buttonSignup;

  EditText passwordEditText;

  ImageView imageLogoImageView;

  RelativeLayout relativeLayout;

  public void showUserList(){
      Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
      startActivity(intent);

  }

  @Override

  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){

      signUp(view);

    }

    return false;
  }

  @Override
  public void onClick(View view) {

    if(view.getId()== R.id.changeSignupModeTextView) {

      if (signupModeActive) {

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        signupModeActive = false;

        buttonSignup.setText("Log In");

        changeSignupModeTextView.setText("Sign Up");


      } else {

        signupModeActive = true;

        buttonSignup.setText("Sign Up");

        changeSignupModeTextView.setText("Log In");

      }

    }else

    if (view.getId()==R.id.relativeLayout || view.getId()== R.id.imageLogoImageView){

      InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

      manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }

  }
  public void signUp (View view){

    EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);


    if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){

      Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();

    }

    else{

      if(signupModeActive) {

        ParseUser user = new ParseUser();

        user.setUsername(usernameEditText.getText().toString());

        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {

          @Override

          public void done(ParseException e) {

            if (e == null) {

              Log.i("SignUp", "successful");

                showUserList();

            } else {

              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
      else{

        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {

          @Override

          public void done(ParseUser user, ParseException e) {

           if(e==null && user!= null){

             Log.i("LogIn","Successful");

               showUserList();

           }else{

             Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
           }
          }
        });
      }
    }
  }

  @Override

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    passwordEditText = (EditText) findViewById(R.id.passwordEditText);


    changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

    changeSignupModeTextView.setOnClickListener(this);

    imageLogoImageView = (ImageView)findViewById(R.id.imageLogoImageView);

    relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);

    imageLogoImageView.setOnClickListener(this);

    relativeLayout.setOnClickListener(this);

    passwordEditText.setOnKeyListener(this);

      if(ParseUser.getCurrentUser()!= null){

          showUserList();

      }


    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }



}