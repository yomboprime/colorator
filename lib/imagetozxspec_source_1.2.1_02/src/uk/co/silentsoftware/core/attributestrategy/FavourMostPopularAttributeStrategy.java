package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * If either colour is from a different set the most
 * popular colour determines which colour set (bright/half bright)
 * the second most popular colour should be in
 */
public class FavourMostPopularAttributeStrategy implements AttributeStrategy {

	/*
	 * {@inheritDoc}
	 */
	@Override
	public int[] enforceAttributeRule(int[] mostPopularColour,
			int[] secondMostPopularColour) {	
		int mostPopRgb = ColourHelper.intToAlphaRgb(mostPopularColour);
		int secMostPopRgb = ColourHelper.intToAlphaRgb(secondMostPopularColour);
		boolean popIsBright = ColourHelper.isBrightSet(mostPopRgb);
		boolean secIsBright = ColourHelper.isBrightSet(secMostPopRgb);
		
		// Attributes are different
		if (popIsBright != secIsBright) {
			// Not black (identical in both sets - no need to do anything)
			if (mostPopularColour != secondMostPopularColour) {
				// Popular colour is bright so force secondary to be in the bright set
				if (popIsBright) {
					secMostPopRgb = ColourHelper.getClosestBrightSpectrumColour(secMostPopRgb, mostPopRgb);
				// Primary is actually dark (secondary bright), so make the secondary dark
				} else {
					secMostPopRgb = ColourHelper.getClosestHalfBrightSpectrumColour(secMostPopRgb, mostPopRgb);
				}
			}
		}
		return new int[]{mostPopRgb, secMostPopRgb};
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBrightSet(int mostPopularColour,
			int secondMostPopularColour) {
		boolean popIsBright = ColourHelper.isBrightSet(mostPopularColour);
		boolean secIsBright = ColourHelper.isBrightSet(secondMostPopularColour);
		// Attributes are different
		if (popIsBright != secIsBright) {
			// Not black (identical in both sets - no need to do anything)
			if (mostPopularColour != secondMostPopularColour) {
				return popIsBright;
			}
		}
		return popIsBright && secIsBright;
		
	}
	
	@Override 
	public String toString() {
		return "Favour most popular colours";
	}
}
