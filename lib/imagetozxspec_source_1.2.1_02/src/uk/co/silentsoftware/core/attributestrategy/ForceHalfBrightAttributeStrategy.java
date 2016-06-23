package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * Returns the half brightness variant of any colours passed
 * in regardless of the closest real colour in the spectrum palette
 * i.e. two colours that ordinarily would both be in the bright
 * set would be converted to half bright.
 */
public class ForceHalfBrightAttributeStrategy implements AttributeStrategy {

	/*
	 * {@inheritDoc}
	 */
	@Override
	public int[] enforceAttributeRule(int[] mostPopularColour,
			int[] secondMostPopularColour) {
		int mostPopRgb = ColourHelper.intToAlphaRgb(mostPopularColour);
		int secMostPopRgb = ColourHelper.intToAlphaRgb(secondMostPopularColour);
		
		// Get the closest half bright colours.
		mostPopRgb = ColourHelper.getClosestHalfBrightSpectrumColour(mostPopRgb);
		secMostPopRgb = ColourHelper.getClosestHalfBrightSpectrumColour(secMostPopRgb, mostPopRgb);
		
		return new int[]{mostPopRgb, secMostPopRgb};
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBrightSet(int mostPopularColour,
			int secondMostPopularColour) {
		return false;
	}
	
	@Override
	public String toString() {
		return "Force half brightness colours" ;
	}
}
