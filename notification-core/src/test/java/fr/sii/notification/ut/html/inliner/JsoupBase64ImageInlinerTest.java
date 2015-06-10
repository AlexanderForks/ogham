package fr.sii.notification.ut.html.inliner;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import fr.sii.notification.helper.html.AssertHtml;
import fr.sii.notification.html.inliner.ContentWithImages;
import fr.sii.notification.html.inliner.ImageResource;
import fr.sii.notification.html.inliner.JsoupBase64ImageInliner;

public class JsoupBase64ImageInlinerTest {
	private static String FOLDER = "/inliner/images/jsoup/";
	private static String SOURCE_FOLDER = FOLDER+"source/";
	private static String EXPECTED_FOLDER = FOLDER+"expected/";
	
	private JsoupBase64ImageInliner inliner;

	@Before
	public void setUp() {
		inliner = new JsoupBase64ImageInliner();
	}
	
	@Test
	public void withImages() throws IOException {
		String source = IOUtils.toString(getClass().getResourceAsStream(SOURCE_FOLDER+"withImages.html"));
		ImageResource image1 = new ImageResource("fb.gif", "images/fb.gif", IOUtils.toByteArray(getClass().getResourceAsStream(SOURCE_FOLDER+"images/fb.gif")), "images/gif");
		ImageResource image2 = new ImageResource("h1.gif", "images/h1.gif", IOUtils.toByteArray(getClass().getResourceAsStream(SOURCE_FOLDER+"images/h1.gif")), "images/gif");
		ImageResource image3 = new ImageResource("left.gif", "images/left.gif", IOUtils.toByteArray(getClass().getResourceAsStream(SOURCE_FOLDER+"images/left.gif")), "images/gif");
		ImageResource image4 = new ImageResource("right.gif", "images/right.gif", IOUtils.toByteArray(getClass().getResourceAsStream(SOURCE_FOLDER+"images/right.gif")), "images/gif");
		ImageResource image5 = new ImageResource("tw.gif", "images/tw.gif", IOUtils.toByteArray(getClass().getResourceAsStream(SOURCE_FOLDER+"images/tw.gif")), "images/gif");
		ContentWithImages inlined = inliner.inline(source, Arrays.asList(image1, image2, image3, image4, image5));
		String expected = IOUtils.toString(getClass().getResourceAsStream(EXPECTED_FOLDER+"withImages.html"));
		expected = expected.replaceAll("images/fb.gif", "data:images/gif;base64,"+new Base64().encodeToString(image1.getContent()));
		expected = expected.replaceAll("images/h1.gif", "data:images/gif;base64,"+new Base64().encodeToString(image2.getContent()));
		expected = expected.replaceAll("images/left.gif", "data:images/gif;base64,"+new Base64().encodeToString(image3.getContent()));
		expected = expected.replaceAll("images/right.gif", "data:images/gif;base64,"+new Base64().encodeToString(image4.getContent()));
		expected = expected.replaceAll("images/tw.gif", "data:images/gif;base64,"+new Base64().encodeToString(image5.getContent()));
		AssertHtml.assertSimilar(expected, inlined.getContent());
	}
}
