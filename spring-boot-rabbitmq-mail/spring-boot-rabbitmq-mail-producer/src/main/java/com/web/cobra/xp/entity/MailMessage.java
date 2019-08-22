package com.web.cobra.xp.entity;

import java.io.Serializable;

public class MailMessage implements Serializable {

	private static final long serialVersionUID = 8193219250555548937L;
	private String from;
	private String[] to;
	private String subject;
	private String content;
	private String attachment;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
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

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
