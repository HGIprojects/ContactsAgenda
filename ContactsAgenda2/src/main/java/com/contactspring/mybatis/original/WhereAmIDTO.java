package com.contactspring.mybatis.original;

public class WhereAmIDTO {

	String lastPageVisited;

	public WhereAmIDTO() {
	}
	
	public WhereAmIDTO(String lastPageVisited) {
		this.lastPageVisited = lastPageVisited;
	}

	public String getLastPageVisited() {
		return lastPageVisited;
	}

	public void setLastPageVisited(String lastPageVisited) {
		this.lastPageVisited = lastPageVisited;
	}
	
	
}
