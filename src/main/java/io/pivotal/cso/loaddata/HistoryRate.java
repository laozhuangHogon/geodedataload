package io.pivotal.cso.loaddata;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HistoryRate implements Comparable<HistoryRate>,Serializable {
	private String report_date;
	private int ap_match;
	private int ap_resolution;
	private int payment_match;
	private int payment_resolution;
	private int book_mailroom;
	private int book_vendor;
	
	private int book_epayment;
	private int payment_mailroom;
	private int payment_vendor;
	private int payment_epayment;
	private String aff_company;

	public String getReport_date() {
		return report_date;
	}

	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}

	public int getAp_match() {
		return ap_match;
	}

	public void setAp_match(int ap_match) {
		this.ap_match = ap_match;
	}

	public int getAp_resolution() {
		return ap_resolution;
	}

	public void setAp_resolution(int ap_resolution) {
		this.ap_resolution = ap_resolution;
	}

	public int getPayment_match() {
		return payment_match;
	}

	public void setPayment_match(int payment_match) {
		this.payment_match = payment_match;
	}

	public int getPayment_resolution() {
		return payment_resolution;
	}

	public void setPayment_resolution(int payment_resolution) {
		this.payment_resolution = payment_resolution;
	}

	public String getAff_company() {
		return aff_company;
	}

	public void setAff_company(String aff_company) {
		this.aff_company = aff_company;
	}


	public int getBook_mailroom() {
		return book_mailroom;
	}

	public void setBook_mailroom(int book_mailroom) {
		this.book_mailroom = book_mailroom;
	}

	public int getBook_vendor() {
		return book_vendor;
	}

	public void setBook_vendor(int book_vendor) {
		this.book_vendor = book_vendor;
	}

	public int getBook_epayment() {
		return book_epayment;
	}

	public void setBook_epayment(int book_epayment) {
		this.book_epayment = book_epayment;
	}

	public int getPayment_mailroom() {
		return payment_mailroom;
	}

	public void setPayment_mailroom(int payment_mailroom) {
		this.payment_mailroom = payment_mailroom;
	}

	public int getPayment_vendor() {
		return payment_vendor;
	}

	public void setPayment_vendor(int payment_vendor) {
		this.payment_vendor = payment_vendor;
	}

	public int getPayment_epayment() {
		return payment_epayment;
	}

	public void setPayment_epayment(int payment_epayment) {
		this.payment_epayment = payment_epayment;
	}

	@Override
	public int compareTo(HistoryRate o) {
		HistoryRate another = (HistoryRate) o;
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date thisDate;
		try {
			thisDate = sdf2.parse(this.getReport_date());
			Date thatDate = sdf2.parse(another.getReport_date());
			return thisDate.compareTo(thatDate);
		} catch (ParseException e) {
			return 0;
		}
	}

}
