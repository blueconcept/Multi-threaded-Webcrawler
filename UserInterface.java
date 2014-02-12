import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserInterface extends JFrame {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private static final int FRAME_HEIGHT = 500;
	private static final int FRAME_WIDTH = 500;
	private static final int TEXT_HEIGHT = 200;
	private static final int TEXT_WIDTH = 20;
	private static final int INITIAL_TEXT_SIZE = 10;

	private List<String> my_keywords_list;
	private JPanel my_keyword_panel;

	private Retriever my_retriever;
	private Analyzer my_analyzer;
	private long my_page_count;


	private JTextField my_url_field;
	private JTextField my_keyword_field;
	private JTextField my_page_count_field;
	private JTextArea my_text_area;

	public UserInterface() {
		super("WEBCRAWLER");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setResizable(false);

		my_keywords_list = new ArrayList<String>();
	}

	private final JPanel createNorthPanel() {
		final JPanel north_panel = new JPanel(new GridLayout(0, 2));

		final JLabel url_label = new JLabel("Enter the URL: ");
		my_url_field = new JTextField(INITIAL_TEXT_SIZE);

		north_panel.add(url_label);
		north_panel.add(my_url_field);

		final JLabel keyword_label = new JLabel("Enter the Keywords: ");
		my_keyword_field = new JTextField(INITIAL_TEXT_SIZE);

		north_panel.add(keyword_label);
		north_panel.add(my_keyword_field);

		final JLabel page_count_label = new JLabel(
				"Enter the number of pages: ");
		my_page_count_field = new JTextField(INITIAL_TEXT_SIZE);

		north_panel.add(page_count_label);
		north_panel.add(my_page_count_field);

		return north_panel;
	}

	private final JPanel createCenterPanel() {
		final JPanel center_panel = new JPanel();

		my_text_area = new JTextArea("... ...");
		center_panel.add(my_text_area);

		return center_panel;
	}

	private final JPanel createSouthPanel() {
		final JPanel south_panel = new JPanel(new FlowLayout());

		final JButton go_button = new JButton("GO!");

		go_button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {

				// my_retriever.setInitialPage(my_initial_page);
				// my_retriever.getPage(my_initial_page);
				storeKeywords();
				my_page_count = Long.parseLong(my_page_count_field.getText());

				final long start_time = System.currentTimeMillis();
				startWebCrawler();
				final long end_time = System.currentTimeMillis();
				printTimeResults(start_time, end_time);
			}
		});

		south_panel.add(go_button);

		return south_panel;
	}

	public void printTimeResults(final long the_start_time,
			final long the_end_time) {
		final long total_time = (the_end_time - the_start_time);

		System.out.println("The total time was " + total_time + " sec");
		System.out.println("The average time per page was: " + total_time
				/ my_page_count + " sec");
	}

	private void storeKeywords() {
		final String words = my_keyword_field.getText();
		final String[] delimited_words = words.split(" ");

		for (int i = 0; i < delimited_words.length; i++) {
			my_keywords_list.add(delimited_words[i]);
		}
	}

	private void startWebCrawler() {
		my_retriever = new Retriever(my_url_field.getText(), my_keywords_list,
				Integer.parseInt(my_page_count_field.getText()));
		my_retriever.addPage(my_url_field.getText());
		my_analyzer = new Analyzer();
	}

	public void run2() {
		add(createNorthPanel(), BorderLayout.NORTH);
		add(createCenterPanel(), BorderLayout.CENTER);
		add(createSouthPanel(), BorderLayout.SOUTH);

		layoutFrame();
	}

	public void addKeyword() {
		JTextField new_keyword = new JTextField();
		new_keyword.setPreferredSize(new Dimension(TEXT_HEIGHT, TEXT_WIDTH));
		new_keyword.setAlignmentX(Component.CENTER_ALIGNMENT);
		my_keyword_panel.add(new_keyword);

		System.out.println("Button Pressed");
	}

	public void layoutFrame() {
		pack();
		setVisible(true);
	}
}
