package com.rabobank.model;


/**
 * model class for line item in the files
 * @author Akbar Hussain
 *
 */


public class BSLineItem {
	
	
	long referenceId;
	String ibanNo;
	double startBalance;
	double mutation;
	String description;
	double endBalance;
	RecordStatus recordStatus=RecordStatus.VALID;
		
	
	public BSLineItem() {
		
	}
	public BSLineItem(long referenceId, String ibanNo, double startBalance, double mutation,
			String description, double endBalance) {
		
		this.referenceId = referenceId;
		this.ibanNo = ibanNo;
		this.startBalance = startBalance;
		this.mutation = mutation;
		this.description = description;
		this.endBalance = endBalance;
	}
	
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public String getIbanNo() {
		return ibanNo;
	}
	public void setIbanNo(String ibanNo) {
		this.ibanNo = ibanNo;
	}
	public double getStartBalance() {
		return startBalance;
	}
	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}
	public double getMutation() {
		return mutation;
	}
	public void setMutation(double mutation) {
		this.mutation = mutation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getEndBalance() {
		return endBalance;
	}
	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}
	@Override
	public String toString() {
		return "CustomerStatementLineItem [referenceId=" + referenceId + ", ibanNo=" + ibanNo + ", startBalance="
				+ startBalance + ", mutation=" + mutation + ", description=" + description + ", endBalance="
				+ endBalance + "]";
	}
	
	@Override
	public int hashCode() {
	   return	new Long(this.referenceId).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BSLineItem other = (BSLineItem) obj;
		if (referenceId != other.referenceId)
			return false;
		return true;
	}
	public RecordStatus getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	
}
