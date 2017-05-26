package com.exam.mybatis;

public class BlossomTO {
	private String usernumber;
	private String id;
	private String nickname;
	private String height;
	private String weight;
	private String registerdate;
	private double tdistance;
	private String stime;
	
	public BlossomTO(java.math.BigDecimal usernumber, String id, String nickname, String height, String weight, java.sql.Timestamp registerdate) {
		this.usernumber = String.valueOf(usernumber);
		this.id = id;
		this.nickname = nickname;
		this.height = height;
		this.weight = weight;
		this.registerdate = String.valueOf(registerdate);
	}

	
	
	public BlossomTO(String stime, double tdistance) {
		super();
		this.tdistance = tdistance;
		this.stime = stime;
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}

	public double getTdistance() {
		return tdistance;
	}

	public void setTdistance(double tdistance) {
		this.tdistance = tdistance;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}
	
	
}
