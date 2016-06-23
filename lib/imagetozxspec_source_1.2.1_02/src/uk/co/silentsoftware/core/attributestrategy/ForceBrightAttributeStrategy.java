package uk.co.silentsoftware.core.attributestrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * Returns the full brightness variant of any colours passed
 * in regardless of the closest real colour in the spectrum palette
 * i.e. two colours that ordinarily would both be in the half bright
 * set would be converted to full bright.
 */
public class ForceBrightAttributeStrategy implements AttributeStrategy {

	/*
	 * {@inheritDoc}
	 */
	@Override
	public int[] enforceAttributeRule(int[] mostPopularColour,
			int[] secondMostPopularColour) {
		int mostPopRgb = ColourHelper.intToAlphaRgb(mostPopularColour);
		int secMostPopRgb = ColourHelper.intToAlphaRgb(secondMostPopularColour);
		
		// Get the closest bright colours.
		mostPopRgb = ColourHelper.getClosestBrightSpectrumColour(mostPopRgb);
		secMostPopRgb = ColourHelper.getClosestBrightSpectrumColour(secMostPopRgb, mostPopRgb);
		
		return new int[]{mostPopRgb, secMostPopRgb};
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBrightSet(int mostPopularColour,
			int secondMostPopularColour) {
		return true;
	}

	@Override
	public String toString() {
		return "Force full brightness colours" ;
	}
}
