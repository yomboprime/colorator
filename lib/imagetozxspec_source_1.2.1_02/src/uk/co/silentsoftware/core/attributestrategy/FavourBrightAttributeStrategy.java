package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * If either colour is from a different set the half bright
 * set colour is moved to the bright set.
 */
public class FavourBrightAttributeStrategy implements AttributeStrategy {

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
		if (popIsBright != secIsBright) {
			// Not black (identical in both sets - no need to do anything)
			if (mostPopularColour != secondMostPopularColour) {
				// Less popular colour is already bright so change to bright set for popular colour
				if (secIsBright) {
					mostPopRgb = ColourHelper.getClosestBrightSpectrumColour(mostPopRgb, secMostPopRgb);	
				// Most popular colour is bright so change to bright set for second most popular colour 
				} else {
					secMostPopRgb = ColourHelper.getClosestBrightSpectrumColour(secMostPopRgb, mostPopRgb);	
				}
			}
		}
		return new int[] {mostPopRgb, secMostPopRgb};
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBrightSet(int mostPopularColour, int secondMostPopularColour) {
		boolean popIsBright = ColourHelper.isBrightSet(mostPopularColour);
		boolean secIsBright = ColourHelper.isBrightSet(secondMostPopularColour);
		if (popIsBright != secIsBright) {
			// Not black (identical in both sets - no need to do anything)
			if (mostPopularColour != secondMostPopularColour) {
				return true;	
			}
		}
		return popIsBright && secIsBright;
	}
	
	@Override 
	public String toString() {
		return "Favour full brightness colours";
	}
}
