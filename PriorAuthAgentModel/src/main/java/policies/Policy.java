package policies;

public class Policy {
	// Important
	String policy_num = "123-456-789";
	double premium = 4000.0;
	String premium_mode = "quarterly";
	double deductable_amnt = 2500; // in network only
	int ded_max_persons = 2; // Max people required to meet deductable_amnt
	double coinsurance = 0.8; // coinsurance percentage
	double oop_max = 8550; // Out-of-pocket max
	int oop_max_person = 2; // Max people required to meet oop_max
	boolean referral_req = false;
	
	// Coverage and limitations
	String[] net_npis = {"0000", "0001", "0002", "0003", "0004", "0005", "0006", "0007", "0008", "0009"}; // Sample npis in network
	String[] oon_covered = {"emergency"}; // Policy only covers out of network if it is an emergency
	//String[] net_covered = {"immunization", "rating A", "rating B", "contraceptive"}; // These aren't ever really explicitly stated
	String[] net_not_covered = {"cosmetic", "ambulance", "sexual dysfunction", "biomedical device", "dental", "experimental service", "intoxicated injury", "preventative care"};
	
	public String getPolicy_num() {
		return policy_num;
	}
	public String[] getNet_npis() {
		return net_npis;
	}
	public void setNet_npis(String[] net_npis) {
		this.net_npis = net_npis;
	}
	public void setPolicy_num(String policy_num) {
		this.policy_num = policy_num;
	}
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
	}
	public String getPremium_mode() {
		return premium_mode;
	}
	public void setPremium_mode(String premium_mode) {
		this.premium_mode = premium_mode;
	}
	public double getDeductable_amnt() {
		return deductable_amnt;
	}
	public void setDeductable_amnt(double deductable_amnt) {
		this.deductable_amnt = deductable_amnt;
	}
	public int getDed_max_persons() {
		return ded_max_persons;
	}
	public void setDed_max_persons(int ded_max_persons) {
		this.ded_max_persons = ded_max_persons;
	}
	public double getCoinsurance() {
		return coinsurance;
	}
	public void setCoinsurance(double coinsurance) {
		this.coinsurance = coinsurance;
	}
	public double getOop_max() {
		return oop_max;
	}
	public void setOop_max(double oop_max) {
		this.oop_max = oop_max;
	}
	public int getOop_max_person() {
		return oop_max_person;
	}
	public void setOop_max_person(int oop_max_person) {
		this.oop_max_person = oop_max_person;
	}
	public boolean isReferral_req() {
		return referral_req;
	}
	public void setReferral_req(boolean referral_req) {
		this.referral_req = referral_req;
	}
	public String[] getOon_covered() {
		return oon_covered;
	}
	public void setOon_covered(String[] oon_covered) {
		this.oon_covered = oon_covered;
	}
	/*public String[] getNet_covered() {
		return net_covered;
	}
	public void setNet_covered(String[] net_covered) {
		this.net_covered = net_covered;
	}*/
	public String[] getNet_not_covered() {
		return net_not_covered;
	}
	public void setNet_not_covered(String[] net_not_covered) {
		this.net_not_covered = net_not_covered;
	}
	
}
