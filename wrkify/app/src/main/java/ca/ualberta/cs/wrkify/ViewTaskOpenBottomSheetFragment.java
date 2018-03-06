package ca.ualberta.cs.wrkify;

import android.view.View;
import android.widget.FrameLayout;


public class ViewTaskOpenBottomSheetFragment extends ViewTaskBottomSheetFragment {
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
        return new FrameLayout(getActivity());
    }
}
