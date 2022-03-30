package ch.epfl.sweng.hostme;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearLayoutManagerWrapper extends LinearLayoutManager {

    /**
     * Constructor for this personalised LinearLayoutManager
     *
     * @param context
     */
    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    /**
     * 2nd Constructor
     *
     * @param context
     * @param orientation
     * @param reverseLayout
     */
    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * 3rd constructor
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}