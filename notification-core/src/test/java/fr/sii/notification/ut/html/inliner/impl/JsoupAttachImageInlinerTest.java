package fr.sii.notification.ut.html.inliner.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import fr.sii.notification.core.resource.ByteResource;
import fr.sii.notification.email.attachment.Attachment;
import fr.sii.notification.email.attachment.ContentDisposition;
import fr.sii.notification.helper.html.AssertHtml;
import fr.sii.notification.helper.rule.LoggingTestRule;
import fr.sii.notification.html.inliner.ContentWithImages;
import fr.sii.notification.html.inliner.ImageResource;
import fr.sii.notification.html.inliner.impl.jsoup.JsoupAttachImageInliner;
import fr.sii.notification.mock.html.inliner.PassThroughGenerator;

public class JsoupAttachImageInlinerTest {
	private static String FOLDER = "/inliner/images/jsoup/";
	private static String SOURCE_FOLDER = FOLDER+"source/";
	private static String EXPECTED_FOLDER = FOLDER+"expected/";
	
	@Rule
	public final LoggingTestRule loggingRule = new LoggingTestRule();
	
	private JsoupAttachImageInliner inliner;

	@Before
	public void setUp() {
		inliner = new JsoupAttachImageInliner(new PassThroughGenerator());
	}
	
	@Test
	public void withImages() throws IOException {
		// prepare html and associated images
		String source = IOUtils.toString(getClass().getResourceAsStream(SOURCE_FOLDER+"withImages.html"));
		List<ImageResource> images = loadImages("fb.gif", "h1.gif", "left.gif", "right.gif", "tw.gif");
		// do the job
		ContentWithImages inlined = inliner.inline(source, images);
		// prepare expected result for the html
		String expected = generateExpectedHtml("withImages.html", "fb.gif", "h1.gif", "left.gif", "right.gif", "tw.gif");
		// prepare expected attachments
		List<Attachment> expectedAttachments = getAttachments(images);
		// assertions
		AssertHtml.assertSimilar(expected, inlined.getContent());
		Assert.assertEquals("should have 5 attachments", 5, inlined.getAttachments().size());
		Assert.assertEquals("should have valid attachments", expectedAttachments, inlined.getAttachments());
	}
	
	@Test
	@Ignore("Not yet implemented")
	public void duplicatedImage() {
		// TODO: when the html contains the same image several times, it should generate only one attachment for it
		Assert.fail("Not implemented");
	}
	
	
	
	//---------------------------------------------------------------//
	//                           Utilities                           //
	//---------------------------------------------------------------//
	
	private static Attachment getAttachment(ImageResource image) {
		return new Attachment(new ByteResource(image.getName(), image.getContent()), null, ContentDisposition.INLINE, "<"+image.getName()+">");
	}
	
	private static List<Attachment> getAttachments(List<ImageResource> images) {
		List<Attachment> attachments = new ArrayList<>(images.size());
		for(ImageResource image : images) {
			attachments.add(getAttachment(image));
		}
		return attachments;
	}
	
	private static String generateExpectedHtml(String fileName, String... imageNames) throws IOException {
		String expected = IOUtils.toString(JsoupAttachImageInlinerTest.class.getResourceAsStream(EXPECTED_FOLDER+fileName));
		for(String imageName : imageNames) {
			expected = expected.replaceAll("images/"+imageName, "cid:"+imageName);
		}
		return expected;
	}
	
	private static List<ImageResource> loadImages(String... imageNames) throws IOException {
		List<ImageResource> resources = new ArrayList<>(imageNames.length);
		for(String imageName : imageNames) {
			resources.add(new ImageResource(imageName, "images/"+imageName, IOUtils.toByteArray(JsoupAttachImageInlinerTest.class.getResourceAsStream(SOURCE_FOLDER+"images/"+imageName)), "images/gif"));
		}
		return resources;
	}
}
