package ca.ualberta.cs.wrkify;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import static android.view.View.inflate;


/**
 * Bottom sheet for a task provider viewing a task that is
 * open or bidded, and that they have not bidded on themselves.
 * Contains controls for placing a bid.
 */
public class ViewTaskOpenBottomSheetFragment extends ViewTaskBottomSheetFragment {

    private Task task;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        EditText bidField = view.findViewById(R.id.taskViewBottomSheetBidField);
        Button button = view.findViewById(R.id.taskViewBottomSheetButtonBid);

        // Confirm and submit bid on button press or text field submit
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                confirmAndSubmitBid(view);
            }
        });
        bidField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    confirmAndSubmitBid(view);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    /**
     * Raises a confirmation dialog. If confirmed, submits the entered bid.
     * @param view View corresponding to the sheet
     */
    private void confirmAndSubmitBid(View view) {
        final EditText bidField = view.findViewById(R.id.taskViewBottomSheetBidField);
        final Task tsk = this.task;
        final Context ctx = getContext();
        ConfirmationDialogFragment dialog = ConfirmationDialogFragment.makeDialog(
                    String.format(Locale.US, "Bid $%s on this task?", bidField.getText()),
                    "Cancel", "Bid",
                    new ConfirmationDialogFragment.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            tsk.addBid(new Bid(
                                    new Price(bidField.getText().toString()),
                                    Session.getInstance(ctx).getUser()
                            ));
                            WrkifyClient.getInstance().upload(tsk);
                        }
                    }
        );
        dialog.show(getActivity().getFragmentManager(), null);
    }

    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        this.task = task;
        Integer bidCount = task.getBidList().size();
        if (bidCount == 0) {
            setDetailString(container,
                    "No bids yet");
        }
        else {
            setDetailString(container,
                    String.format(Locale.US, "%d bids so far", bidCount));
            Bid lowestBid = task.getBidList().get(0);
            setRightStatusString(container, lowestBid.toString());
        }
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
        return inflate(getActivity(), R.layout.activity_view_task_bottom_sheet_bid, null);
    }
}
