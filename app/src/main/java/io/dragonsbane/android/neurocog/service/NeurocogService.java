package io.dragonsbane.android.neurocog.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * Service functionality for Neurocog app.
 * </p>
 */
public class NeurocogService extends IntentService {

    private static final String ACTION_CHECK_EYE_BLINKS = "io.dragonsbane.neurocog.android.service.action.CHECK_EYE_BLINKS";
    private static final String ACTION_SUBMIT_TEST_RESULTS = "io.dragonsbane.neurocog.android.service.action.SUBMIT_TEST_RESULTS";
    private static final String ACTION_SEND_TEST_HISTORY = "io.dragonsbane.neurocog.android.service.action.SEND_TEST_HISTORY";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "io.dragonsbane.neurocog.android.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "io.dragonsbane.neurocog.android.service.extra.PARAM2";

    public NeurocogService() {
        super("NeurocogService");
    }

    /**
     * Starts this service to perform action CHECK_EYE_BLINKS with the given parameters. If
     * the service is already performing a task, this action will be queued.
     *
     * @see IntentService
     */
    public static void checkEyeBlinks(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NeurocogService.class);
        intent.setAction(ACTION_CHECK_EYE_BLINKS);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action SUBMIT_TEST_RESULTS with the given parameters. If
     * the service is already performing a task, this action will be queued.
     *
     * @see IntentService
     */
    public static void submitTestResults(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NeurocogService.class);
        intent.setAction(ACTION_SUBMIT_TEST_RESULTS);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action SEND_TEST_HISTORY with the given parameters. If
     * the service is already performing a task, this action will be queued.
     *
     * @see IntentService
     */
    public static void sendTestHistory(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NeurocogService.class);
        intent.setAction(ACTION_SEND_TEST_HISTORY);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(action != null) {
                switch (action) {
                    case ACTION_CHECK_EYE_BLINKS: {
                        final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                        final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                        handleCheckEyeBlinks(param1, param2);
                        break;
                    }
                    case ACTION_SUBMIT_TEST_RESULTS: {
                        final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                        final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                        handleSubmitTestResults(param1, param2);
                        break;
                    }
                    case ACTION_SEND_TEST_HISTORY: {
                        final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                        final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                        handleSendTestHistory(param1, param2);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Handle action CHECK_EYE_BLINKS in the provided background thread with the provided
     * parameters.
     */
    private void handleCheckEyeBlinks(String param1, String param2) {
        // TODO: Handle action CHECK_EYE_BLINKS
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action SUBMIT_TEST_RESULTS in the provided background thread with the provided
     * parameters.
     */
    private void handleSubmitTestResults(String param1, String param2) {
        // TODO: Handle action SUBMIT_TEST_RESULTS
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action SEND_TEST_HISTORY in the provided background thread with the provided
     * parameters.
     */
    private void handleSendTestHistory(String param1, String param2) {
        // TODO: Handle action SEND_TEST_HISTORY
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
