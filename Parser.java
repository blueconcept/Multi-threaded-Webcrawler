import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * The parser class. It parses the webpage for words and links.
 * 
 * @author Group
 * 
 */
public class Parser {

	private Document my_initial_page;
	private File my_current_file;
	private List<String> my_keywords_list;
	private Map<String, Integer> my_keyword_map;
	private int my_max;

	private Stack<String> my_document_stack;
	private Retriever my_retriever;
	private Analyzer my_analyzer;

	/**
	 * The parser contructor.
	 * 
	 * 
	 * @param the_retriever
	 * @param the_initial_page
	 * @param the_keywords_list
	 * @param the_max
	 */
	public Parser(final Retriever the_retriever, final String the_initial_page,
			final List<String> the_keywords_list, final int the_max) {
		my_retriever = the_retriever;
		my_keywords_list = the_keywords_list;
		my_keyword_map = new HashMap<String, Integer>();
		my_max = the_max;

		setInitialPage(the_initial_page);

		my_document_stack = new Stack<String>();
		ArrayList<String> history = new ArrayList<String>();

		int i = the_max;
		while (!my_document_stack.isEmpty() && i > 0) {
			if (history.contains(my_document_stack.peek())) {
				my_document_stack.pop();
			} else {
				my_initial_page = my_retriever
						.addPage(my_document_stack.peek());
				history.add(my_document_stack.pop());
				getAllLinks();
				getAllText();
				i--;
			}
		}
	}

	/**
	 * This sets the initial page to parse. The initial page is inputed from the
	 * GUI.
	 * 
	 * @param the_initial_page
	 */
	private final void setInitialPage(final String the_initial_page) {
		try {
			// final File input = new File(the_initial_page);
			// my_initial_page = Jsoup.parse(input, "UTF-8",
			// "http://example.com");

			my_initial_page = Jsoup.connect(the_initial_page).get(); // for
																		// online
																		// examples.
		} catch (final IOException the_exception) {
			the_exception.printStackTrace();
		}
	}

	/**
	 * Gets all the links in the current webpage.
	 * 
	 * @return links, a collection of links.
	 */
	public Elements getAllLinks() {
		Elements links = null;
		String title = my_initial_page.title();

		System.out.println("title : " + title);
		links = my_initial_page.select("a[href]");

		System.out.printf("Number of links: %d \n", links.size());

		for (Element x : links) {
			System.out.println(x.attr("abs:href"));
			my_document_stack.push(x.attr("abs:href"));
			my_retriever.addPage(x.attr("abs:href"));
		}

		return links;
	}

	/**
	 * Gets all the text
	 * 
	 * @return map of all keywords parsed plus their frequencies throughout the
	 *         duration of the program.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getAllText() {
		List<String> strings = new ArrayList<String>();
		String text = my_initial_page.text();
		StringTokenizer tokens = new StringTokenizer(text, " ");

		while (tokens.hasMoreTokens()) {
			String temp = tokens.nextToken();
			strings.add(temp);
		}

		HashMap<String, Integer> map = (HashMap<String, Integer>) sort(hashMapping(strings));
		Iterator<String> it = map.keySet().iterator();

		System.out.println(map.size());

		int i = 0;

		while (i != map.size()) {
			final String key = it.next().toString();
			final String value = map.get(key).toString();

			System.out.println(key + "\t\t\t" + value);
			i++;
		}

		return map;
	}

	/**
	 * A helper method used to store all the parsed text and counts the
	 * frequenncies of those texts.
	 * 
	 * @param the_list
	 * @return a hashed map.
	 */
	private HashMap<String, Integer> hashMapping(List<String> the_list) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

		for (String x : the_list) {
			final String new_string = x;
			final Integer count = hashMap.get(new_string);

			if (count == null) {
				hashMap.put(new_string, 1);
			} else {
				hashMap.put(new_string, count + 1);
			}
		}

		return hashMap;
	}

	/**
	 * A helper method to sort a hash map.
	 * 
	 * @param unsortMap
	 * @return a sorted hash map.
	 */
	private static Map<String, Integer> sort(Map unsortMap) {

		LinkedList list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
