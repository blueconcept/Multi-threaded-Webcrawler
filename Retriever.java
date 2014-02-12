import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Retriever {
	
	private List<Document> my_webpages;
	private Parser my_parser;


	public Retriever(final String the_initial_page,
			final List<String> the_keywords, final int the_max) {
		my_webpages = new LinkedList<Document>();
		my_parser = new Parser(this, the_initial_page, the_keywords, the_max);
	}

	public Document addPage(final String the_page) {
		Document doc = null;
		if (!my_webpages.contains(the_page)) {
			try {
				doc = Jsoup.connect(the_page).get();
				my_webpages.add(doc);

				my_parser.getAllText();
				my_parser.getAllLinks();

			} catch (final IOException the_exception) {
				System.out.println("Either the file doesn't exist, or you "
						+ "do not have permission to access the file :(");
			}
		} else {
			// For testing
			System.out.println("THat page already exists!!!");
		}

		return doc;
	}
}
