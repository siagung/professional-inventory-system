/**
 * The ItemStock class.
 * 
 * 
 */
package caseStudyII;

public class ItemStock {

	public final static String[] itemChoiceType = { "Blouse", "Skirt", "Shirt",
			"Pants", "Shorts" };
	public final static String[] itemChoiceColor = { "Red", "Orange", "Yellow",
			"Green", "Blue" };
	public final static String[] itemChoiceSize = { "Small", "Medium", "Large",
			"Xtra Large!" };

	protected static String itemType;
	protected static String itemColor;
	protected static String itemSize;

	protected int indxType;
	protected int indxColor;
	protected int indxBrand;
	protected int itemNo;
	protected double itemPrice;

	public ItemStock(int iType, int iColor, int iSize, int iNo, double iPrice) {

		setItemStock(iType, iColor, iSize, iNo, iPrice);
		setIndexEquivalent(iType, iColor, iSize);
	}

	private void setItemStock(int iType, int iColor, int iBrand, int iNo,
			double iPrice) {
		indxType = iType;
		indxColor = iColor;
		indxBrand = iBrand;

		itemNo = iNo;
		itemPrice = iPrice;
	}

	private void setIndexEquivalent(int iType, int iColor, int iSize) {
		itemType = itemChoiceType[iType];
		itemColor = itemChoiceColor[iColor];
		itemSize = itemChoiceSize[iSize];
	}

	public static String setIndexEquivalentType(int iType) {
		itemType = itemChoiceType[iType];
		return itemType;
	}

	public static String setIndexEquivalentColor(int iColor) {
		itemColor = itemChoiceColor[iColor];
		return itemColor;
	}

	public static String setIndexEquivalentSize(int iSize) {
		itemSize = itemChoiceSize[iSize];
		return itemSize;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return itemType + " " + itemColor + " " + itemSize + " " + itemNo + " "
				+ itemPrice;
	}

	public void incrementNo() {
		itemNo = itemNo + 1;
	}

	public void addNo(int iNo) {
		itemNo = itemNo + iNo;
	}

	public void setType(int iT) {
		itemType = itemChoiceType[iT];
	}

	public void setBrand(int iB) {
		itemSize = itemChoiceSize[iB];
	}

	public void setColor(int iC) {
		itemColor = itemChoiceColor[iC];
	}

	public void setQuantity(int iN) {
		itemNo = iN;
	}

	public void setPrice(double iP) {
		itemPrice = iP;
	}

	public int getType() {
		return indxType;
	}

	public int getColor() {
		return indxColor;
	}

	public int getBrand() {
		return indxBrand;
	}

	public int getItemNo() {
		return itemNo;
	}

	public double getItemPrice() {
		return itemPrice;
	}

}
