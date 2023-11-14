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
}
