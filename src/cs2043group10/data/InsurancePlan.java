package cs2043group10.data;

public class InsurancePlan {
	public final int deductible;
	public final int outOfPocketMaximum;
	public final int costSharePercentage;
	
	public InsurancePlan(int deductible, int outOfPocketMaximum, int costSharePercentage) {
		this.deductible = deductible;
		this.outOfPocketMaximum = outOfPocketMaximum;
		this.costSharePercentage = costSharePercentage;
	}
	
	public int amountPaidForPayment(int amount) {
		int accum = amount - deductible;
		if (accum <= 0) {
			return amount;
		}
		accum = (100 - costSharePercentage) * accum / 100 + deductible;
		if (accum > outOfPocketMaximum) {
			return outOfPocketMaximum;
		} else {
			return accum;
		}
	}
	
	@Override
	public String toString() {
		return String.format("Deductible: $%.2f, Out of pocket maximum: %.2f, Cost share percentage: %d%%", deductible / 100.0, outOfPocketMaximum / 100.0, costSharePercentage);
	}
}
