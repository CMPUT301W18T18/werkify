package ca.ualberta.cs.wrkify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;


/**
 * Bottom sheet for a task provider viewing a task that is
 * open or bidded, and that they have not bidded on themselves.
 * Contains controls for placing a bid.
 */
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
    public ViewTaskBottomSheet initializeWithTask(Task task) {
        Integer bidCount = task.getBidList().size();
        if (bidCount == 0) {
            setDetailString("No bids yet");
        }
        else {
            setDetailString(String.format(Locale.US, "%d bids so far", bidCount));
            Bid lowestBid = task.getLowestBid();
            setRightStatusString(String.format(Locale.US, "$%.2f", lowestBid.getValue()));
        }

        return super.initializeWithTask(task);
    }

    @Override
    protected String getStatusString() {
        return "Open";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusRequested;
    }

    @Override
    protected View getContentLayout(ViewGroup root) {
        return inflate(getContext(), R.layout.activity_view_task_bottom_sheet_bid, null);
    }
}
