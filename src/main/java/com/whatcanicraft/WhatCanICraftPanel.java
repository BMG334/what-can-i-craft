package com.whatcanicraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

/**
 * The plugin's side panel: one tab per skill, each with its own scan button and results.
 * All methods must be called on the Swing event thread.
 */
public class WhatCanICraftPanel extends PluginPanel
{
	private final Map<CraftSkill, SkillTab> tabs = new EnumMap<>(CraftSkill.class);

	public WhatCanICraftPanel(Consumer<CraftSkill> onScan)
	{
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
		tabbedPane.setFocusable(false);

		// Tab order follows the CraftSkill enum: Herblore, then Crafting
		for (CraftSkill skill : CraftSkill.values())
		{
			SkillTab tab = new SkillTab(skill.getCategories(), () -> onScan.accept(skill));
			tabs.put(skill, tab);
			tabbedPane.addTab(skill.getDisplayName(), tab);
		}

		add(tabbedPane, BorderLayout.CENTER);
	}

	public void showMessage(CraftSkill skill, String message)
	{
		tabs.get(skill).showMessage(message);
	}

	public void showResults(CraftSkill skill, List<Recipe.Match> matches)
	{
		tabs.get(skill).showResults(matches);
	}

	/**
	 * Content of one skill tab: a "Scan Bank" button (plus a category filter dropdown if the skill has
	 * categories), a status line, and the list of discovered recipes.
	 */
	private static class SkillTab extends JPanel
	{
		private static final String ALL_CATEGORIES = "All";
		private static final DecimalFormat XP_FORMAT = new DecimalFormat("#,##0.#");

		private final JLabel statusLabel = new JLabel();
		private final JPanel resultsContainer = new JPanel();
		private final JComboBox<String> categoryFilter;

		/** Results of the most recent scan, kept so changing the filter doesn't require rescanning. */
		private List<Recipe.Match> lastMatches;

		SkillTab(List<String> categories, Runnable onScan)
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

		void showMessage(String message)
		{
			lastMatches = null;
			display(message, List.of());
		}

		void showResults(List<Recipe.Match> matches)
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

			// Main text: recipe name and how many can be made; subtitle: ingredients
			entry.add(label(match.getRecipe().getName() + " (x" + match.getCount() + ")", Color.WHITE));
			entry.add(label(html(150, match.getIngredients()), ColorScheme.LIGHT_GRAY_COLOR));

			// Total XP for making them all (hidden when the recipe has no XP data)
			if (match.getXp() > 0)
			{
				entry.add(label(XP_FORMAT.format(match.getXp()) + " XP", ColorScheme.PROGRESS_COMPLETE_COLOR));
			}

			return entry;
		}

		private static JLabel label(String text, Color color)
		{
			JLabel label = new JLabel(text);
			label.setForeground(color);
			label.setAlignmentX(LEFT_ALIGNMENT);
			return label;
		}

		/** Wraps text in HTML so long lines wrap at the given pixel width. */
		private static String html(int width, String text)
		{
			return "<html><body style='width:" + width + "px'>" + text + "</body></html>";
		}
	}
}
