package com.example.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.study.utils.Utilities;

/***
 * 
 * @author Sergey Prilutsky
 * 
 */

public class MainActivity extends Activity {
	private Button mShowMapBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniViews();
		setListener();
	}

	private void iniViews() {
		mShowMapBtn = (Button) findViewById(R.id.show_map_btn);
	}

	private void setListener() {
		mShowMapBtn.setOnClickListener(new ShowMapListener());
	}

	private void showAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.error));
		builder.setMessage(getResources().getString(R.string.check_connect));
		builder.setPositiveButton(getResources().getString(R.string.ok), new AlertPositiveBtnListener());		
		builder.setNegativeButton(getResources().getString(R.string.network), new AlertNegativeBtnListner());
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private class AlertPositiveBtnListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();			
		}		
	}
	
	private class AlertNegativeBtnListner implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();	
			startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);			
		}		
	}
	
	private final class ShowMapListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(Utilities.hasConnection(MainActivity.this)){
				startActivity(MapActivity.getIntent(MainActivity.this));
				finish();
			}else{
				showAlert();
			}			
		}
	}
}