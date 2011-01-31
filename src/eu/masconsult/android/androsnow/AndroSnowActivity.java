package eu.masconsult.android.androsnow;

import android.app.Activity;
import android.os.Bundle;

public class AndroSnowActivity extends Activity {
	
	SnowView view;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       view = (SnowView) findViewById(R.id.snowView);
    }
    
    @Override
    protected void onPause() {
    	view.stopHandler();
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	view.startHandler();
    	super.onResume();
    }
    
    
}