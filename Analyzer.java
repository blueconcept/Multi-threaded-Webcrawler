import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Analyzer {
	private static final DecimalFormat WORD_FORMAT = new DecimalFormat("#.###");
	private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#.##");
	private int my_average_url_count;
	private int my_total_parse_time;
	private Map<String, Integer> my_word_count;

	public Analyzer() {
		my_word_count = new LinkedHashMap<String, Integer>();
		my_total_parse_time = 0;
	}

	public Analyzer(final Map<String, Integer> the_word_count) {
		my_word_count = the_word_count;
		my_total_parse_time = 0;
	}

	public void setWordCount(final Map<String, Integer> the_word_count) {
		my_word_count = the_word_count;
	}

	private int getTotalWordCount() {
		int count = 0;

		final Iterator<String> itr = my_word_count.keySet().iterator();
		String temp;

		while (itr.hasNext()) {
			temp = itr.next();
			count += my_word_count.get(temp);
		}

		return count;
	}

	private String getAverageHits(final String the_word) {
		double count = (double) my_word_count.get(the_word)
				/ (double) my_word_count.size();
		return WORD_FORMAT.format(count);
	}

	private String getAverageParseTime() {
		return TIME_FORMAT.format(my_total_parse_time / my_word_count.size());
	}

	public void setTotalRunningTime(final int the_total_time) {
		my_total_parse_time = the_total_time;
	}

	private String getTotalRunningTime() {
		return TIME_FORMAT.format(my_total_parse_time);
	}

	public int getAverageURLCount() {
		return my_average_url_count;
	}

	public String getResults() {
		StringBuilder str = new StringBuilder();

		str.append("\n").append("Pages Retrieved: ")
				.append(my_word_count.size());
		str.append("\n").append("Average words per page: ")
				.append(getTotalWordCount() / my_word_count.size());
		str.append("\n").append("Average URLs per page: ").append("");

		str.append("\n").append("Keyword\t").append("Avg. hits per page\t")
				.append("Total hits");

		Iterator<String> itr = my_word_count.keySet().iterator();
		String word = "";
		while (itr.hasNext()) {
			word = itr.next();
			str.append("\n").append(word).append("\t")
					.append(getAverageHits(word)).append("\t")
					.append(my_word_count.get(word));
		}

		str.append("\n").append("Average parse time per page: ")
				.append(getAverageParseTime()).append(" sec");
		str.append("\n").append("Total running time: ")
				.append(getTotalRunningTime()).append(" sec");

		return str.toString();
	}
}
