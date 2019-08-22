package com.web.cobra.xp.entity;

import java.io.Serializable;

public class MailMessage implements Serializable {

	private static final long serialVersionUID = 8193219250555548937L;
	private String to;
	private String subject;
	private String content;
	private String rscPath;
	private String rscId;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRscPath() {
		return rscPath;
	}

	public void setRscPath(String rscPath) {
		this.rscPath = rscPath;
	}

	public String getRscId() {
		return rscId;
	}

	public void setRscId(String rscId) {
		this.rscId = rscId;
	}

}
