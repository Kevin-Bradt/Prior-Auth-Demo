package model;

public class MedicalInfo {
	
	private String medication;
	private String strength;
	private String directions;
	private String diagnosis;
	private String HIVorAIDS;
	private String icd9Codes;
	private String isPregnant;
	private String explanation;
	private PreviousMeds previousMeds;
	public String getMedication() {
		return medication;
	}
	public void setMedication(String medication) {
		this.medication = medication;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getDirections() {
		return directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getHIVorAIDS() {
		return HIVorAIDS;
	}
	public void setHIVorAIDS(String hIVorAIDS) {
		HIVorAIDS = hIVorAIDS;
	}
	public String getIcd9Codes() {
		return icd9Codes;
	}
	public void setIcd9Codes(String icd9Codes) {
		this.icd9Codes = icd9Codes;
	}
	public String getIsPregnant() {
		return isPregnant;
	}
	public void setIsPregnant(String isPregnant) {
		this.isPregnant = isPregnant;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public PreviousMeds getPreviousMeds() {
		return previousMeds;
	}
	public void setPreviousMeds(PreviousMeds previousMeds) {
		this.previousMeds = previousMeds;
	}
	
	
}

class PreviousMeds {
	
	private String name;
	private String strength;
	private String dateRange;
	private String description;
	private String reason;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getDateRange() {
		return dateRange;
	}
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}