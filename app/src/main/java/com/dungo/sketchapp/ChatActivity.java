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
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.net.Uri;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;

public class ChatActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String UserName = "";
	private HashMap<String, Object> chatdatamap = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> chatmap = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private Button exit;
	private ListView listview1;
	private EditText message;
	private ImageView send;
	
	private DatabaseReference chatdata = _firebase.getReference("chatdata");
	private ChildEventListener _chatdata_child_listener;
	private FirebaseAuth fauth;
	private OnCompleteListener<AuthResult> _fauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
	private OnCompleteListener<Void> _fauth_reset_password_listener;
	private Intent i = new Intent();
	private Calendar cal = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		exit = (Button) findViewById(R.id.exit);
		listview1 = (ListView) findViewById(R.id.listview1);
		message = (EditText) findViewById(R.id.message);
		send = (ImageView) findViewById(R.id.send);
		fauth = FirebaseAuth.getInstance();
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				FirebaseAuth.getInstance().signOut();
				i.setClass(getApplicationContext(), LoadingActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				message.setText(message.getText().toString().replace("", ""));
				ChatActivity.this.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
				if (message.getText().toString().equals("") || message.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Írj be egy üzenetet...");
				}
				else {
					cal = Calendar.getInstance();
					chatdatamap.put("User", FirebaseAuth.getInstance().getCurrentUser().getEmail());
					chatdatamap.put("Msg", message.getText().toString());
					chatdatamap.put("Time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(cal.getTime()));
					chatdata.push().updateChildren(chatdatamap);
					SketchwareUtil.showMessage(getApplicationContext(), "Üzenet sikeresen elküldve.");
				}
				message.setText("");
			}
		});
		
		_chatdata_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				chatdata.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chatmap = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								chatmap.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						listview1.setAdapter(new Listview1Adapter(chatmap));
						listview1.post(new Runnable() { @Override public void run() { listview1.setSelection(chatmap.size());}});
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				chatdatamap.clear();
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		chatdata.addChildEventListener(_chatdata_child_listener);
		
		_fauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	private void initializeLogic() {
		ChatActivity.this.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
		listview1.setOverScrollMode(View.OVER_SCROLL_NEVER);listview1.setVerticalScrollBarEnabled(false);
		listview1.setDivider(null);listview1.setDividerHeight(0); listview1.setSelector(android.R.color.transparent);
		UserName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		FirebaseAuth.getInstance().signOut();
		i.setClass(getApplicationContext(), LoadingActivity.class);
		startActivity(i);
	}
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.custom, null);
			}
			
			final LinearLayout sender = (LinearLayout) _v.findViewById(R.id.sender);
			final LinearLayout receiver = (LinearLayout) _v.findViewById(R.id.receiver);
			final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			final TextView textview2 = (TextView) _v.findViewById(R.id.textview2);
			final TextView textview5 = (TextView) _v.findViewById(R.id.textview5);
			final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
			final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
			final TextView textview4 = (TextView) _v.findViewById(R.id.textview4);
			final TextView textview6 = (TextView) _v.findViewById(R.id.textview6);
			
			if (UserName.equals(chatmap.get((int)_position).get("User").toString())) {
				cal.set(Calendar.YEAR, (int)(Double.parseDouble(new SimpleDateFormat("yyyy").format(cal.getTime()))));
				cal.set(Calendar.MONTH, (int)(Double.parseDouble(new SimpleDateFormat("MM").format(cal.getTime()))));
				cal.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(new SimpleDateFormat("dd").format(cal.getTime()))));
				cal.set(Calendar.HOUR, (int)(Double.parseDouble(new SimpleDateFormat("HH").format(cal.getTime()))));
				cal.set(Calendar.MINUTE, (int)(Double.parseDouble(new SimpleDateFormat("mm").format(cal.getTime()))));
				cal.set(Calendar.SECOND, (int)(Double.parseDouble(new SimpleDateFormat("ss").format(cal.getTime()))));
				receiver.setVisibility(View.GONE);
				sender.setVisibility(View.VISIBLE);
				textview1.setText(chatmap.get((int)_position).get("User").toString());
				textview2.setText(chatmap.get((int)_position).get("Msg").toString());
				textview5.setText(chatmap.get((int)_position).get("Time").toString());
			}
			else {
				cal.set(Calendar.YEAR, (int)(Double.parseDouble(new SimpleDateFormat("yyyy").format(cal.getTime()))));
				cal.set(Calendar.MONTH, (int)(Double.parseDouble(new SimpleDateFormat("MM").format(cal.getTime()))));
				cal.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(new SimpleDateFormat("dd").format(cal.getTime()))));
				cal.set(Calendar.HOUR, (int)(Double.parseDouble(new SimpleDateFormat("HH").format(cal.getTime()))));
				cal.set(Calendar.MINUTE, (int)(Double.parseDouble(new SimpleDateFormat("mm").format(cal.getTime()))));
				cal.set(Calendar.SECOND, (int)(Double.parseDouble(new SimpleDateFormat("ss").format(cal.getTime()))));
				sender.setVisibility(View.GONE);
				receiver.setVisibility(View.VISIBLE);
				textview3.setText(chatmap.get((int)_position).get("User").toString());
				textview4.setText(chatmap.get((int)_position).get("Msg").toString());
				textview6.setText(chatmap.get((int)_position).get("Time").toString());
			}
			
			return _v;
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
