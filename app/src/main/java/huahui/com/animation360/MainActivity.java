package huahui.com.animation360;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import huahui.com.animation360.service.MyFloatService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void StartService(View view) {
        Intent intent = new Intent(this, MyFloatService.class);
        startService(intent);
        finish();
    }
}
