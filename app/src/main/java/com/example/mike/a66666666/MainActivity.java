package com.example.mike.a66666666;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final String EMPTY = " ";

    private TextView textView[] = new TextView[4];

    private SparseIntArray mapOfButtons,  // map id to number
            mapOfFilledTextViews;         // map array index to value if TextView is filled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMaps();

        textView[0] = (TextView) findViewById(R.id.text_1);
        textView[1] = (TextView) findViewById(R.id.text_2);
        textView[2] = (TextView) findViewById(R.id.text_3);
        textView[3] = (TextView) findViewById(R.id.text_4);

        for (TextView tv : textView) {
            tv.setOnClickListener( this);
        }

        Button b1 = (Button) findViewById(R.id.button_1);
        Button b2 = (Button) findViewById(R.id.button_2);
        Button b3 = (Button) findViewById(R.id.button_3);
        Button b4 = (Button) findViewById(R.id.button_4);
        Button b5 = (Button) findViewById(R.id.button_5);
        Button b6 = (Button) findViewById(R.id.button_6);
        Button b7 = (Button) findViewById(R.id.button_7);
        Button b8 = (Button) findViewById(R.id.button_8);
        Button b9 = (Button) findViewById(R.id.button_9);
        Button b0 = (Button) findViewById(R.id.button_0);
        Button ent = (Button) findViewById(R.id.button_enter);
        Button clr = (Button) findViewById(R.id.button_clear);

        buttonEffect(b0);
        buttonEffect(b1);
        buttonEffect(b2);
        buttonEffect(b3);
        buttonEffect(b4);
        buttonEffect(b5);
        buttonEffect(b6);
        buttonEffect(b7);
        buttonEffect(b8);
        buttonEffect(b9);
        buttonEffect(clr);
        buttonEffect(ent);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (v instanceof Button) {
            if (mapOfButtons.indexOfKey(id) < 0) { // so this is no number button
                if (id == R.id.button_clear) {
                    if (mapOfFilledTextViews.size() == 0) {
                        return;
                    }
                    // get the key = the index for 'textView'
                    int indexToClear = indexOfRightmostFilledTextView();
                    // get the value
                    int numberToClear = mapOfFilledTextViews.get(indexToClear);
                    clearButton(numberToClear);
                    clearTextView(indexToClear);
                    bindText();
                } else if (id == R.id.button_enter) {
                    // do something
                }
            } else // this is a number button
            {
                // only accept clicks if there is a free TextView
                if (mapOfFilledTextViews.size() == textView.length) {
                   /* Toast.makeText(this,
                            "First clear one of the selected numbers", Toast.LENGTH_SHORT).show();*/
                    return;
                }
                Button b = (Button) v;
                b.setBackgroundResource(R.drawable.redww);
                b.setEnabled(false);

                int number = mapOfButtons.get(id);
                setText(number);
            }

        } else if (v instanceof TextView) {
            TextView tv = (TextView) v;
            int number = Integer.parseInt(tv.getText().toString());
            // 'indexOfValue()' will work because we don't have duplicate values
            int i = mapOfFilledTextViews.indexOfValue(number);
            int key = mapOfFilledTextViews.keyAt(i);
            clearButton(number);
            clearTextView(key);
            bindText();
        }
    }

    private static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0ffffff, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
    }


    private void initMaps() {
        // empty in the beginning:
        mapOfFilledTextViews = new SparseIntArray(4);

        mapOfButtons = new SparseIntArray(10);
        mapOfButtons.put(R.id.button_0, 0);
        mapOfButtons.put(R.id.button_1, 1);
        mapOfButtons.put(R.id.button_2, 2);
        mapOfButtons.put(R.id.button_3, 3);
        mapOfButtons.put(R.id.button_4, 4);
        mapOfButtons.put(R.id.button_5, 5);
        mapOfButtons.put(R.id.button_6, 6);
        mapOfButtons.put(R.id.button_7, 7);
        mapOfButtons.put(R.id.button_8, 8);
        mapOfButtons.put(R.id.button_9, 9);
    }

    private void bindText() {
        int value;
        int len = textView.length;
        for (int i = 0; i < len; i++) {
            value = mapOfFilledTextViews.get(i, -1);
            textView[i].setText(value >= 0 ? String.valueOf(value) : EMPTY);
        }
    }

    private void setText(int number) {
        if (mapOfFilledTextViews.size() >= textView.length) {
            // this should not happen if we check the number
            // of free TextViews in onClick()
            return;
        }

        int index = indexOfLowestFreeTextView();
        mapOfFilledTextViews.put(index, number);
        bindText();
    }

    private void clearButton(int number) {
        // get the first (in our case only) index with valueAt(index) == number
        int index = mapOfButtons.indexOfValue(number);

        if (index < 0) // this value does not exist
        {
            // this should not happen!
            // so maybe throw a RuntimeException
            return;
        }

        int id = mapOfButtons.keyAt(index);
        Button b = (Button) findViewById(id);
        b.setBackgroundResource(R.drawable.greenww);
        b.setEnabled(true);
    }

    private void clearTextView(int index) {
        // remove entry as this TextView is no longer filled
        mapOfFilledTextViews.delete(index);
    }

    /**
     * @return index (for use with 'textView') if there are filled TextViews, else -1
     */
    private int indexOfRightmostFilledTextView() {
        int size = mapOfFilledTextViews.size();
        int maxKey = -1;
        for (int i = 0; i < size; i++) {
            int iTemp = mapOfFilledTextViews.keyAt(i);
            if (iTemp > maxKey) {
                maxKey = iTemp;
            }
        }
        return maxKey;
    }

    /**
     * Get index of leftmost (lowest) free TextView
     *
     * @return index for use with 'textView'.
     * If all TextViews are filled, return textView.length
     */
    private int indexOfLowestFreeTextView() {
        int lowestIndex = textView.length;
        // this assumes the TextViews are sorted from left to right in the textView array
        for (int key = 0; key < textView.length; key++) {
            if (mapOfFilledTextViews.indexOfKey(key) < 0) {
                lowestIndex = key;
                break;
            }
        }
        return lowestIndex;
    }
}
