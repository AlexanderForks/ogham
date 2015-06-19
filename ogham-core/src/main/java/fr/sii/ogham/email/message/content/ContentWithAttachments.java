package fr.sii.ogham.email.message.content;

import java.util.List;

import fr.sii.ogham.core.message.content.Content;
import fr.sii.ogham.email.attachment.Attachment;

/**
 * Decorator that embeds attachments with the decorated content.
 * 
 * @author Aurélien Baudet
 *
 */
public class ContentWithAttachments implements Content {
	/**
	 * The decorated content
	 */
	private Content content;
	
	/**
	 * The attachments
	 */
	private List<Attachment> attachments;

	public ContentWithAttachments(Content content, List<Attachment> attachments) {
		super();
		this.content = content;
		this.attachments = attachments;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
