package com.deu.istatistik;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class estatXmlParser {
	private static final String ns = null;

	public List<Entry> parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {


			XmlPullParserFactory we = XmlPullParserFactory.newInstance();

			XmlPullParser parser = we.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		    //parser.setInput(in, null);
		    parser.setInput(in, "ISO-8859-9");


			parser.nextTag();
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Entry> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Entry> entries = new ArrayList<Entry>();

		parser.require(XmlPullParser.START_TAG, ns, "channel");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag

			if (name.equals("item")) {
				entries.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	public static class Entry implements Serializable {

		private static final long serialVersionUID = 1L;
		private String title;
		private String link;
		private String description;
		private String html_title;
		private String html_description;

		// public final String title;
		// public final String link;
		// public final String description;
		public Entry() {
			this.title = "title";
			this.description = "description";
			this.link = "link";
			this.html_title = "html_title";
			this.html_description = "html_description";

		}

		public Entry(String title, String description, String link) {
			this.title = title;
			this.description = description;
			this.link = link;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getHtml_title() {
			return html_title;
		}

		public void setHtml_title(String html_title) {
			this.html_title = html_title;
		}

		public String getHtml_description() {
			return html_description;
		}

		public void setHtml_description(String html_description) {
			this.html_description = html_description;
		}
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them
	// off
	// to their respective &quot;read&quot; methods for processing. Otherwise,
	// skips the tag.
	private Entry readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "item");
		String title = null;
		String description = null;
		String link = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readTitle(parser);
			} else if (name.equals("description")) {
				description = readSummary(parser);
			} else if (name.equals("link")) {
				link = readLink(parser);
			} else {
				skip(parser);
			}
		}
		return new Entry(title, description, link);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	// Processes link tags in the feed.
	private String readLink(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String link = "";
		parser.require(XmlPullParser.START_TAG, ns, "link");

		link = readText(parser);
		// String tag = parser.getName();
		// String relType = parser.getAttributeValue(null, "rel");
		// if (tag.equals("link")) {
		// if (relType.equals("alternate")) {
		// link = parser.getAttributeValue(null, "href");
		// parser.nextTag();
		// }
		// }
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}

	// Processes summary tags in the feed.
	private String readSummary(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// Skips tags the parser isn't interested in. Uses depth to handle nested
	// tags. i.e.,
	// if the next tag after a START_TAG isn't a matching END_TAG, it keeps
	// going until it
	// finds the matching END_TAG (as indicated by the value of "depth" being
	// 0).
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
