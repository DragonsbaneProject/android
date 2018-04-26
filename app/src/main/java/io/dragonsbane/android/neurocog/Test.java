package io.dragonsbane.android.neurocog;

import java.io.Serializable;
import java.util.List;

/**
 * TODO: Add Definition
 *
 * @author Brian on 4/26/18
 */

public interface Test extends Serializable {
    List<Integer> cardsUsed();
    TestResult getTestResult();
}
