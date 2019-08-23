package com.web.cobra.xp.service;

import org.thymeleaf.context.Context;

public interface MailService {

	public void sendSimpleMail(String from, String to, String subject, String content);

	public void sendHtmlMail(String from, String to, String subject, String content);

	public void sendAttachmentsMail(String from, String to, String subject, String content, String filePath);

	public void sendInlineResourceMail(String from, String to, String subject, String content, String rscPath,
			String rscId);

	public void sendTemplateMail(String from, String to, String subject, String content, String templateName,Context context);
}
