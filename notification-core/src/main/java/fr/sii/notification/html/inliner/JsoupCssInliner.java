package fr.sii.notification.html.inliner;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class JsoupCssInliner implements CssInliner {
	private static final String SKIP_INLINE = "data-skip-inline";
	private static final String CSS_STYLE = "data-cssstyle";
	private static final String STYLE_ATTR = "style";
	private static final String STYLE_TAG = "style";
	private static final String CSS_LINK_TAG = "link";
	private static final String CSS_REL = "stylesheet";
	private static final String CSS_TYPE = "text/css";

	@Override
	public String inline(String htmlContent, List<ExternalCss> cssContents) {
		Document doc = Jsoup.parse(htmlContent);

		internStyles(doc, cssContents);
		String stylesheet = fetchStyles(doc);
		extractStyles(doc, stylesheet);
		applyStyles(doc);

		// TODO: really useful ?
//		doc.outputSettings(doc.outputSettings().prettyPrint(false).escapeMode(Entities.EscapeMode.xhtml));
//		return StringEscapeUtils.unescapeHtml3(doc.outerHtml());
		return doc.outerHtml();
	}

	/**
	 * Applies the styles to a <code>data-cssstyle</code> attribute. This is
	 * because the styles need to be applied sequentially, but before the
	 * <code>style</code> defined for the element inline.
	 *
	 * @param doc
	 *            the html document
	 */
	private void extractStyles(Document doc, String stylesheet) {
		String trimmedStylesheet = stylesheet.replaceAll("\n", "").replaceAll("/\\*.*?\\*/", "").replaceAll(" +", " ");
		String styleRules = trimmedStylesheet.trim(), delims = "{}";
		StringTokenizer st = new StringTokenizer(styleRules, delims);
		while (st.countTokens() > 1) {
			String selector = st.nextToken(), properties = st.nextToken();
			Elements selectedElements = doc.select(selector);
			for (Element selElem : selectedElements) {
				String oldProperties = selElem.attr(CSS_STYLE);
				selElem.attr(CSS_STYLE, oldProperties.length() > 0 ? concatenateProperties(oldProperties, properties) : properties);
			}
		}
	}

	/**
	 * Replace link tags with style tags in order to keep the same inlusion
	 * order
	 *
	 * @param doc
	 *            the html document
	 * @param cssContents
	 *            the list of external css files with their content
	 */
	private void internStyles(Document doc, List<ExternalCss> cssContents) {
		Elements els = doc.select(CSS_LINK_TAG);
		for (Element e : els) {
			if (!e.attr(SKIP_INLINE).equals("true")) {
				String path = e.attr("href");
				Element style = new Element(Tag.valueOf(STYLE_TAG), "");
				style.appendChild(new DataNode(getCss(cssContents, path), ""));
				e.replaceWith(style);
			}
		}
	}

	private String getCss(List<ExternalCss> cssContents, String path) {
		for (ExternalCss css : cssContents) {
			if (css.getPath().contains(path)) {
				return css.getContent();
			}
		}
		throw new IllegalStateException("The css with path " + path + " doesn't exist in the list of css contents");
	}

	/**
	 * Generates a stylesheet from an html document
	 *
	 * @param doc
	 *            the html document
	 * @return a string representing the stylesheet.
	 */
	private String fetchStyles(Document doc) {
		Elements els = doc.select(STYLE_TAG);
		StringBuilder styles = new StringBuilder();
		for (Element e : els) {
			if (!e.attr(SKIP_INLINE).equals("true")) {
				styles.append(e.data());
				e.remove();
			}
		}
		return styles.toString();
	}

	/**
	 * Transfers styles from the <code>data-cssstyle</code> attribute to the
	 * <code>style</code> attribute.
	 *
	 * @param doc
	 *            the html document
	 */
	private void applyStyles(Document doc) {
		Elements allStyledElements = doc.getElementsByAttribute(CSS_STYLE);

		for (Element e : allStyledElements) {
			String newStyle = e.attr(CSS_STYLE);
			String oldStyle = e.attr(STYLE_ATTR);
			e.attr(STYLE_ATTR, (newStyle + "; " + oldStyle).replace(";;", ";"));
			e.removeAttr(CSS_STYLE);
		}
	}

	private static String concatenateProperties(String oldProp, String newProp) {
		if (!oldProp.endsWith(";")) {
			oldProp += ";";
		}
		return oldProp.trim() + " " + newProp.trim() + ";";
	}
}
