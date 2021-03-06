package fr.sii.ogham.html.inliner.impl.jsoup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.sii.ogham.core.id.generator.IdGenerator;
import fr.sii.ogham.core.resource.ByteResource;
import fr.sii.ogham.email.attachment.Attachment;
import fr.sii.ogham.email.attachment.ContentDisposition;
import fr.sii.ogham.html.inliner.ContentWithImages;
import fr.sii.ogham.html.inliner.ImageInliner;
import fr.sii.ogham.html.inliner.ImageResource;

/**
 * Image inliner that loads the image and attaches it to the mail. The image is
 * referenced using a content ID. The content ID is automatically generated.
 * <p>
 * If the <code>img</code> tag has the attribute skip-attach, then the image is
 * skipped (not attached using a content ID).
 * </p>
 * 
 * @author Aurélien Baudet
 *
 */
public class JsoupAttachImageInliner implements ImageInliner {
	private static final String CONTENT_ID = "<{0}>";
	private static final String SRC_ATTR = "src";
	private static final String SRC_VALUE = "cid:{0}";
	private static final String IMG_SELECTOR = "img[src=\"{0}\"]";
	private static final String ATTACH_MODE = "attach";
		
	private IdGenerator idGenerator;
	
	public JsoupAttachImageInliner(IdGenerator idGenerator) {
		super();
		this.idGenerator = idGenerator;
	}

	@Override
	public ContentWithImages inline(String htmlContent, List<ImageResource> images) {
		Document doc = Jsoup.parse(htmlContent);
		List<Attachment> attachments = new ArrayList<>(images.size());
		for (ImageResource image : images) {
			// search all images in the HTML with the provided path or URL that are not skipped
			Elements imgs = getImagesToAttach(doc, image);
			if(!imgs.isEmpty()) {
				String contentId = idGenerator.generate(image.getName());
				// generate attachment
				Attachment attachment = new Attachment(new ByteResource(image.getName(), image.getContent()), null, ContentDisposition.INLINE, MessageFormat.format(CONTENT_ID, contentId));
				// update the HTML to use the generated content id instead of the path or URL
				for(Element img : imgs) {
					img.attr(SRC_ATTR, MessageFormat.format(SRC_VALUE, contentId));
				}
				attachments.add(attachment);
			}
		}
		return new ContentWithImages(doc.outerHtml(), attachments);
	}

	private Elements getImagesToAttach(Document doc, ImageResource image) {
		Elements imgs = doc.select(MessageFormat.format(IMG_SELECTOR, image.getPath()));
		Elements found = new Elements();
		for(Element img : imgs) {
			// skip images that have skip-attach attribute
			if(JsoupUtils.isInlineModeAllowed(img, ATTACH_MODE)) {
				found.add(img);
			}
		}
		return found;
	}

}
