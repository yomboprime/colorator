package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * If either colour is from a different set the bright
 * set colour is moved to the half bright set.
 */
public class FavourHalfBrightAttributeStrategy implements AttributeStrategy {

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
				// If the less popular colour is bright make it like the popular one (favour half bright)
				if (secIsBright) {
					secMostPopRgb = ColourHelper.getClosestHalfBrightSpectrumColour(secMostPopRgb, mostPopRgb);
				// Most popular colour is bright, we'll need to darken it (favour half bright)
				} else {
					mostPopRgb = ColourHelper.getClosestHalfBrightSpectrumColour(mostPopRgb, secMostPopRgb);	
				}
			}
		}
		return new int[]{mostPopRgb, secMostPopRgb};
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBrightSet(int mostPopularColour, int secondMostPopularColour) {
		boolean popIsBright = ColourHelper.isBrightSet(mostPopularColour);
		boolean secIsBright = ColourHelper.isBrightSet(secondMostPopularColour);
		if (popIsBright != secIsBright) {
			return false;	
		}
		return popIsBright && secIsBright;
	}
	
	@Override 
	public String toString() {
		return "Favour half brightness colours";
	}
}
