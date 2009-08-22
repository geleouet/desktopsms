package fr.galize.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Server extends Activity {
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle icicle) {
      super.onCreate(icicle);
      try {

        // setup and start MyService
        {
          //MyService.setMainActivity(this);
          Intent svc = new Intent(this, MyService.class);
          startService(svc);
          moveTaskToBack(true);
        }
        Context context = getApplicationContext();
        CharSequence text = "Launching";
        int duration = Toast.LENGTH_SHORT;
        
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

      }
      catch (Exception e) {
        Log.e("gaetan","ui creation problem", e);
      }

    }
    
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.view.Menu.FIRST:
            {
                Intent svc = new Intent(this, MyService.class);
                stopService(svc);
            	System.exit(0);
                return true;
            }
        }

        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, android.view.Menu.FIRST, 0, "STOP");

        return true;
    }

    @Override protected void onDestroy() {
      super.onDestroy();
      Context context = getApplicationContext();
      CharSequence text = "Stopping";
      int duration = Toast.LENGTH_SHORT;
      
      Toast toast = Toast.makeText(context, text, duration);
      toast.show();
      // stop MyService
      {
        Intent svc = new Intent(this, MyService.class);
        stopService(svc);
      }
    }

}