/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Fragment to ask for confirmation.
 */
public class ConfirmationDialogFragment extends DialogFragment {
    public static String ARGUMENT_CONFIRM_MESSAGE = "ca.ualberta.cs.wrkify.ARGUMENT_CONFIRM_MESSAGE";
    public static String ARGUMENT_CANCEL_MESSAGE = "ca.ualberta.cs.wrkify.ARGUMENT_CANCEL_MESSAGE";
    public static String ARGUMENT_BODY_MESSAGE = "ca.ualberta.cs.wrkify.ARGUMENT_BODY_MESSAGE";

    /**
     * Factory method to produce a ConfirmationDialogFragment easily.
     * @param body Text to display in the dialog body
     * @param cancel Text to display as the label of the cancel button
     * @param confirm Text to display as the label of the confirm button
     * @return ConfirmationDialogFragment instantiated with given parameters
     */
    public static ConfirmationDialogFragment makeDialog(String body, String cancel, String confirm, OnConfirmListener listener) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_BODY_MESSAGE, body);
        arguments.putString(ARGUMENT_CANCEL_MESSAGE, cancel);
        arguments.putString(ARGUMENT_CONFIRM_MESSAGE, confirm);
        fragment.setArguments(arguments);
        fragment.setOnConfirmListener(listener);

        return fragment;
    }

    private OnConfirmListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(arguments.getString(ARGUMENT_BODY_MESSAGE));

        // Pressing the Positive button fires the guarded event
        builder.setPositiveButton(arguments.getString(ARGUMENT_CONFIRM_MESSAGE), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onConfirm();
            }
        });

        // Pressing the Neutral button cancels the dialog
        builder.setNeutralButton(arguments.getString(ARGUMENT_CANCEL_MESSAGE), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    /**
     * Interface defining a callback from the ConfirmationDialog.
     */
    public interface OnConfirmListener {
        /**
         * Guarded action to be called when the dialog is confirmed.
         */
        void onConfirm();
    }
}
