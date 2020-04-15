package com.squarefong.notisync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<ConfigItem> configList;

    public ConfigsManager configsManager = new ConfigsManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initConfigs();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ConfigAdapter adapter = new ConfigAdapter(this, configList);
        recyclerView.setAdapter(adapter);
        IntentFilter filter = new IntentFilter(ConfigFileActivity.action);
        registerReceiver(broadcastReceiver, filter);

    }

    private void initConfigs() {
        configList = configsManager.getConfigList();
        if (configList.size() == 0 ){
            ConfigItem item = new ConfigItem(
                    -1, 0,
                    "Untitled Configuration",
                    "127.0.0.1", 23445,"000-000",
                    WorkingMode.Receiver.getCode());
            configsManager.insert(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //启动Activity，手动输入配置
            case R.id.type_manually:
                Intent intent = new Intent(MainActivity.this,
                        ConfigFileActivity.class);
                intent.putExtra("isNew", true);
                startActivity(intent);
                break;
            case R.id.import_from_clip_board:
                Toast.makeText(MainActivity.this, "You Click Import",
                        Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub;
            unregisterReceiver(this);
            recreate();
        }
    };
}
