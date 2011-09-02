/**
 * The ItemStock class.
 * 
 * 
 */
package caseStudy;

public class ItemStock {

	public final static String[] itemChoiceType = { "Blouse", "Skirt", "Shirt",
			"Pants", "Shorts" };
	public final static String[] itemChoiceColor = { "Red", "Orange", "Yellow",
			"Green", "Blue" };
	public final static String[] itemChoiceBrand = { "Lacoste", "Bench",
			"Guess", "Lee" };

	protected String itemType;
	protected String itemColor;
	protected String itemBrand;

	protected int indxType;
	protected int indxColor;
	protected int indxBrand;
	protected int itemNo;
	protected double itemPrice;

	public ItemStock(int iType, int iColor, int iBrand, int iNo, double iPrice) {

		setItemStock(iType, iColor, iBrand, iNo, iPrice);
		setIndexEquivalent(iType, iColor, iBrand);
	}

	private void setItemStock(int iType, int iColor, int iBrand, int iNo,
			double iPrice) {
		indxType = iType;
		indxColor = iColor;
		indxBrand = iBrand;

		itemNo = iNo;
		itemPrice = iPrice;
	}

	private void setIndexEquivalent(int iType, int iColor, int iBrand) {

		itemType = itemChoiceType[iType];
		itemColor = itemChoiceColor[iColor];
		itemBrand = itemChoiceBrand[iBrand];
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return itemType + " " + itemColor + " " + itemBrand + " " + itemNo
				+ " " + itemPrice;
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
		itemBrand = itemChoiceBrand[iB];
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
