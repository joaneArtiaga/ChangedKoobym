package com.example.joane14.myapplication.Model;

import java.io.Serializable;
import java.util.Set;


public class Author implements Serializable{

	private long authorId;

	private String authorFName;

	private String authorLName;

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public String getAuthorFName() {
		return authorFName;
	}

	public void setAuthorFName(String authorFName) {
		this.authorFName = authorFName;
	}

	public String getAuthorLName() {
		return authorLName;
	}

	public void setAuthorLName(String authorLName) {
		this.authorLName = authorLName;
	}
	
}
