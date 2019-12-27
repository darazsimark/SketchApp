package com.dungo.sketchapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.net.Uri;
import android.widget.CompoundButton;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {
	
	
	private double showhidepass = 0;
	
	private LinearLayout linear16;
	private LinearLayout linear15;
	private TextView at;
	private TextView uu;
	private TextView textview3;
	private LinearLayout linear20;
	private LinearLayout linear22;
	private LinearLayout linear23;
	private LinearLayout linear21;
	private TextView forgotpass;
	private Switch switch1;
	private LinearLayout linear4;
	private ImageView imageview3;
	private TextView textview1;
	private EditText email;
	private Button emailtorles;
	private EditText pass;
	private Button jelszotorles;
	private LinearLayout linear13;
	private LinearLayout linear19;
	private Button signin;
	private Button signup;
	
	private FirebaseAuth fauth;
	private OnCompleteListener<AuthResult> _fauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
	private OnCompleteListener<Void> _fauth_reset_password_listener;
	private Intent i = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear16 = (LinearLayout) findViewById(R.id.linear16);
		linear15 = (LinearLayout) findViewById(R.id.linear15);
		at = (TextView) findViewById(R.id.at);
		uu = (TextView) findViewById(R.id.uu);
		textview3 = (TextView) findViewById(R.id.textview3);
		linear20 = (LinearLayout) findViewById(R.id.linear20);
		linear22 = (LinearLayout) findViewById(R.id.linear22);
		linear23 = (LinearLayout) findViewById(R.id.linear23);
		linear21 = (LinearLayout) findViewById(R.id.linear21);
		forgotpass = (TextView) findViewById(R.id.forgotpass);
		switch1 = (Switch) findViewById(R.id.switch1);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		imageview3 = (ImageView) findViewById(R.id.imageview3);
		textview1 = (TextView) findViewById(R.id.textview1);
		email = (EditText) findViewById(R.id.email);
		emailtorles = (Button) findViewById(R.id.emailtorles);
		pass = (EditText) findViewById(R.id.pass);
		jelszotorles = (Button) findViewById(R.id.jelszotorles);
		linear13 = (LinearLayout) findViewById(R.id.linear13);
		linear19 = (LinearLayout) findViewById(R.id.linear19);
		signin = (Button) findViewById(R.id.signin);
		signup = (Button) findViewById(R.id.signup);
		fauth = FirebaseAuth.getInstance();
		
		forgotpass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				fauth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(_fauth_reset_password_listener);
			}
		});
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (showhidepass == 0) {
					showhidepass++;
					pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
				}
				else {
					showhidepass = 0;
					pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		
		email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (email.getText().toString().equals("")) {
					emailtorles.setVisibility(View.INVISIBLE);
				}
				else {
					emailtorles.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		emailtorles.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				email.setText("");
				emailtorles.setVisibility(View.INVISIBLE);
			}
		});
		
		pass.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (pass.getText().toString().equals("")) {
					jelszotorles.setVisibility(View.INVISIBLE);
				}
				else {
					jelszotorles.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		jelszotorles.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				pass.setText("");
				jelszotorles.setVisibility(View.INVISIBLE);
			}
		});
		
		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "E-mail cím és/vagy jelszó hibás!");
				}
				else {
					fauth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(MainActivity.this, _fauth_sign_in_listener);
				}
			}
		});
		
		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "E-mail cím és/vagy jelszó hibás!");
				}
				else {
					fauth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(MainActivity.this, _fauth_create_user_listener);
					fauth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(MainActivity.this, _fauth_sign_in_listener);
				}
			}
		});
		
		_fauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					SketchwareUtil.showMessage(getApplicationContext(), "Kérlek lépj be!");
					i.setClass(getApplicationContext(), LoadingActivity.class);
					startActivity(i);
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
				}
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					i.setClass(getApplicationContext(), LoadingActivity.class);
					startActivity(i);
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
				}
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				if (_success) {
					SketchwareUtil.showMessage(getApplicationContext(), "Jelszó-helyreállító levél elküldve.");
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "A jelszó-helyreállító levelet nem sikerült elküldeni. Kérlek írd be az e-mail címed újra.");
				}
			}
		};
	}
	private void initializeLogic() {
		emailtorles.setVisibility(View.INVISIBLE);
		jelszotorles.setVisibility(View.INVISIBLE);
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			i.setClass(getApplicationContext(), LoadingActivity.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
