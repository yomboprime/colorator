package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;


/**
 * Interface to enforce an attribute choice strategy
 */
public interface AttributeStrategy {

	/**
	 * Enforce the rule by modifying the rgb component objects
	 * and changing the attribute colour set they are from *if
	 * necessary* (i.e. bright or half bright).
	 * 
	 * @param mostPopularColour
	 * @param secondMostPopularColour
	 */
	public int[] enforceAttributeRule(int[] mostPopularColour, int[] secondMostPopularColour);

	/**
	 * Similar to ColorHelper.isBrightSet but uses the strategy
	 * implementation to determine whether *both* colours should
	 * be in the bright or half bright set.
	 * @see ColourHelper#isBrightSet(int)
	 * 
	 * @param mostPopularColour
	 * @param secondMostPopularColour
	 * @return
	 */
	public boolean isBrightSet(int mostPopularColour, int secondMostPopularColour);
}
