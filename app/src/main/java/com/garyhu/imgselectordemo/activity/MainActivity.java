package com.garyhu.imgselectordemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.garyhu.imgselectordemo.R;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;

    private LinearLayout mImgContainer;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mImgContainer = (LinearLayout) findViewById(R.id.img_container);
        btn = ((Button) findViewById(R.id.btn));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMG:
                showContent(data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void showContent(Intent data) {
        Bitmap bm = null;
        ImageView iv = null;
        List<String> paths = data == null ? Collections.<String>emptyList() : data.getStringArrayListExtra("data");
        if (paths == null || paths.isEmpty()) {
            return;
        }
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            bm = file2Bitmap(path);
            iv = createImageView();
            iv.setImageBitmap(bm);
            mImgContainer.addView(iv);
        }

    }

    /**
     * 转换图片
     * @param path 路径
     * @return
     */
    public Bitmap file2Bitmap(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 创建ImageView
     * @return
     */
    public ImageView createImageView(){
        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);
        return iv;
    }

    /**
     * 点击按钮进行图片选择
     * @param v
     */
    public void selectImg(View v) {
        ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
    }
}
