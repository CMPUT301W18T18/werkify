package ca.ualberta.cs.wrkify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class ViewTaskOpenBottomSheet extends ViewTaskBottomSheet {
    public ViewTaskOpenBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTaskOpenBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTaskOpenBottomSheet(Context context) {
        super(context);
    }

    @Override
    protected String getStatusString() {
        return "Open";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    protected View getContentLayout() {
        TextView view = new TextView(getContext());
        view.setText("test");
        return view;
    }
}
