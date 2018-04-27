package io.dragonsbane.android.neurocog.models;

/**
 * Blood Alcohol Content (BAC) Impairment Model
 *
 * BAC is used for training the model due to the ability to easily measure it.
 *
 * Widely expected levels of impairment based solely on BAC:
 * Male Female BAC% Effects
 *   1    1    .020 Light to moderate drinkers begin to feel some effects
 *             .040 Most people begin to feel relaxed
 *  2-3  1-2   .050 Though, judgment and restraint more lax. Steering errors increase. Vision impaired.
 *             .060 Judgment is somewhat impaired, people are less able to make rational decisions about their capabilities (for example, driving)
 *  3-4  2-4   .080 There is a definite impairment of muscle coordination and driving skills; this is legal level for intoxication in some states
 *  3-5  2-5   .100 There is clear deterioration of reaction time and control; this is legally drunk in most states
 *             .120 Vomiting usually occurs. Unless this level is reached slowly or a person has developed a tolerance to alcohol
 *  4-7  3-7   .150 Balance and movement are impaired. This blood-alcohol level means the equivalent of 1/2 pint of whiskey is circulating in the blood stream
 *             .300 Many people lose consciousness
 *             .400 Most people lose consciousness; some die
 *             .450 Breathing stops; this is a fatal dose for most people
 */

public abstract class BACImpairmentModel extends ImpairmentModelBase {


}
