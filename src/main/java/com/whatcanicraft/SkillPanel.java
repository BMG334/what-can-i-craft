package com.whatcanicraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;

/**
 * Content of one skill tab: a "Scan Bank" button (plus an optional category filter dropdown),
 * a status line, and the list of discovered recipes.
 * All methods must be called on the Swing event thread.
 */
public class SkillPanel extends JPanel
{
	private static final String ALL_CATEGORIES = "All";

	private final JLabel statusLabel = new JLabel();
	private final JPanel resultsContainer = new JPanel();
	private final JComboBox<String> categoryFilter;

	/** Results of the most recent scan, kept so changing the filter doesn't require rescanning. */
	private List<Recipe.Match> lastMatches;

	/**
	 * @param categories filter categories; if empty, no dropdown is shown
	 */
	public SkillPanel(List<String> categories, Runnable onScan)
	{
		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JButton scanButton = new JButton("Scan Bank");
		scanButton.setFocusable(false);
		scanButton.addActionListener(e -> onScan.run());

		JPanel controls = new JPanel(new BorderLayout(5, 0));
		controls.setOpaque(false);
		controls.add(scanButton, BorderLayout.CENTER);

		if (categories.isEmpty())
		{
			categoryFilter = null;
		}
		else
		{
			List<String> items = new ArrayList<>(List.of(ALL_CATEGORIES));
			items.addAll(categories);

			categoryFilter = new JComboBox<>(items.toArray(new String[0]));
			categoryFilter.setFocusable(false);
			categoryFilter.setToolTipText("Filter results by category");
			categoryFilter.addActionListener(e -> render());
			controls.add(categoryFilter, BorderLayout.EAST);
		}

		statusLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		statusLabel.setText(html(170, "Open your bank, then press Scan Bank."));

		JPanel header = new JPanel(new BorderLayout(0, 8));
		header.setOpaque(false);
		header.add(controls, BorderLayout.NORTH);
		header.add(statusLabel, BorderLayout.CENTER);

		resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
		resultsContainer.setOpaque(false);

		// Wrapper keeps entries packed at the top instead of stretching to fill the panel
		JPanel resultsWrapper = new JPanel(new BorderLayout());
		resultsWrapper.setOpaque(false);
		resultsWrapper.add(resultsContainer, BorderLayout.NORTH);

		add(header, BorderLayout.NORTH);
		add(resultsWrapper, BorderLayout.CENTER);
	}

	public void showMessage(String message)
	{
		lastMatches = null;
		display(message, List.of());
	}

	public void showResults(List<Recipe.Match> matches)
	{
		lastMatches = matches;
		render();
	}

	/** Shows lastMatches, applying the currently selected category filter. */
	private void render()
	{
		if (lastMatches == null)
		{
			return;
		}

		String selected = categoryFilter == null ? ALL_CATEGORIES : (String) categoryFilter.getSelectedItem();
		boolean filtering = !ALL_CATEGORIES.equals(selected);

		List<Recipe.Match> visible = lastMatches.stream()
			.filter(match -> !filtering || selected.equals(match.getRecipe().getCategory()))
			.collect(Collectors.toList());

		String status;
		if (visible.isEmpty())
		{
			status = "No " + (filtering ? selected + " recipes" : "recipes") + " found in your bank.";
		}
		else
		{
			status = "Found " + visible.size() + " recipe" + (visible.size() == 1 ? "" : "s") + ":";
		}

		display(status, visible);
	}

	/** Replaces the status line and result entries. */
	private void display(String status, List<Recipe.Match> entries)
	{
		statusLabel.setText(html(170, status));

		resultsContainer.removeAll();
		for (Recipe.Match match : entries)
		{
			resultsContainer.add(createEntry(match));
			resultsContainer.add(Box.createVerticalStrut(5));
		}
		resultsContainer.revalidate();
		resultsContainer.repaint();
	}

	private static JPanel createEntry(Recipe.Match match)
	{
		JPanel entry = new JPanel();
		entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));
		entry.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		entry.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

		// Main text: recipe name
		JLabel title = new JLabel(match.getRecipe().getName());
		title.setForeground(Color.WHITE);
		title.setAlignmentX(LEFT_ALIGNMENT);

		// Subtitle: ingredients
		JLabel subtitle = new JLabel(html(150, match.getIngredients()));
		subtitle.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		subtitle.setAlignmentX(LEFT_ALIGNMENT);

		entry.add(title);
		entry.add(subtitle);
		return entry;
	}

	/** Wraps text in HTML so long lines wrap at the given pixel width. */
	private static String html(int width, String text)
	{
		return "<html><body style='width:" + width + "px'>" + text + "</body></html>";
	}
}
