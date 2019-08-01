package cc.runa.runatextview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cc.runa.richtextview.RichTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String string = getString(R.string.app_name);

        RichTextView tempTv = findViewById(R.id.tv_temp);
        tempTv.loadOrigin(string)
                .setLabel(true, "标签")
                .insertImage(2, R.mipmap.ic_launcher, false)
                .insertImage(7, R.mipmap.ic_launcher, true)
                .setForeground(8, 10, Color.RED)
                .setStrikeLine(7, 10)
                .setLink(3, 5)
                .setFontScale(0, 2, 0.5f)
                .setLinkListener(new RichTextView.OnLinkClickListener() {
                    @Override
                    public void clickLink(int startIndex) {
                        Toast.makeText(MainActivity.this, "当前点击链接起始位置为" + startIndex, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
