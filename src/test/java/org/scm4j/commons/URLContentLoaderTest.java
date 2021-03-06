package org.scm4j.commons;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class URLContentLoaderTest {
	
	private static final String SEQ_BOM = "regexconfig/sequence-bom.yml";
	private static final String SEQ_OMAP = "regexconfig/sequence-omap.yml";
	private static final String SEQ = "regexconfig/sequence.yml";

	@Test
	public void testGetContentFromUrls() throws Exception {
		URLContentLoader loader = new URLContentLoader();
		URL url1 = this.getClass().getResource(SEQ_OMAP);
		URL url2 = this.getClass().getResource(SEQ_BOM);
		URL url3 = this.getClass().getResource(SEQ);
		String content1 = FileUtils.readFileToString(new File(url1.toURI()), StandardCharsets.UTF_8);
		String content2 = FileUtils.readFileToString(new File(url2.toURI()), StandardCharsets.UTF_8);
		String content3 = FileUtils.readFileToString(new File(url3.toURI()), StandardCharsets.UTF_8);
		List<String> contents = loader.getContentsFromUrls(url1.toString(), url2.toString() + URLContentLoader.URL_SEPARATOR + url3.toString());
		assertTrue(contents.containsAll(Arrays.asList(content1, content2, content3)));
		assertEquals(3, contents.size());

		contents = loader.getContentsFromUrls(Arrays.asList(url1, url2));
		assertTrue(contents.containsAll(Arrays.asList(content1, content2)));
		assertEquals(2, contents.size());

		contents = loader.getContentsFromUrls("", URLContentLoader.URL_SEPARATOR + url1.toString());
		assertEquals(content1, contents.get(0));
		assertEquals(1, contents.size());
	}
	
	@Test
	public void testFileProtocolOmitting() throws Exception {
		URLContentLoader loader = new URLContentLoader();
		URL url1 = this.getClass().getResource(SEQ_OMAP);
		URL url2 = this.getClass().getResource(SEQ_BOM);
		File file1 = new File(url1.toURI());
		File file2 = new File(url2.toURI());
		String content1 = FileUtils.readFileToString(file1, StandardCharsets.UTF_8);
		String content2 = FileUtils.readFileToString(file2, StandardCharsets.UTF_8);
		List<String> contents = loader.getContentsFromUrls(file1.toString(), file2.toString());
		assertTrue(contents.containsAll(Arrays.asList(content1, content2)));
		assertEquals(2, contents.size());
	}
}

