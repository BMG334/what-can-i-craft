package com.whatcanicraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

/**
 * Side panel: a "Scan Bank" button, a status line, and the list of discovered recipes.
 * All methods that touch Swing must be called on the Swing event thread.
 */
public class WhatCanICraftPanel extends PluginPanel
{
	private final JLabel statusLabel = new JLabel();
	private final JPanel resultsContainer = new JPanel();

	public WhatCanICraftPanel(Runnable onScan)
	{
		super();
		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JButton scanButton = new JButton("Scan Bank");
		scanButton.setFocusable(false);
		scanButton.addActionListener(e -> onScan.run());

		statusLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		setStatus("Open your bank, then press Scan Bank.");

		JPanel header = new JPanel(new BorderLayout(0, 8));
		header.setOpaque(false);
		header.add(scanButton, BorderLayout.NORTH);
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

	public void setStatus(String text)
	{
		statusLabel.setText("<html><body style='width:170px'>" + text + "</body></html>");
	}

	public void showMessage(String message)
	{
		resultsContainer.removeAll();
		setStatus(message);
		refresh();
	}

	public void showResults(List<HerbloreRecipes.Match> matches)
	{
		resultsContainer.removeAll();

		if (matches.isEmpty())
		{
			setStatus("No herblore recipes found in your bank.");
		}
		else
		{
			setStatus("Found " + matches.size() + " recipe" + (matches.size() == 1 ? "" : "s") + ":");
			for (HerbloreRecipes.Match match : matches)
			{
				resultsContainer.add(createEntry(match));
				resultsContainer.add(createGap());
			}
		}

		refresh();
	}

	private JPanel createEntry(HerbloreRecipes.Match match)
	{
		JPanel entry = new JPanel();
		entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));
		entry.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		entry.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

		// Main text: potion name
		JLabel title = new JLabel(match.getPotionName());
		title.setForeground(Color.WHITE);
		title.setAlignmentX(LEFT_ALIGNMENT);

		// Subtitle: ingredients
		JLabel subtitle = new JLabel("<html><body style='width:150px'>" + match.getIngredients() + "</body></html>");
		subtitle.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		subtitle.setAlignmentX(LEFT_ALIGNMENT);

		entry.add(title);
		entry.add(subtitle);
		return entry;
	}

	private static JPanel createGap()
	{
		JPanel gap = new JPanel();
		gap.setOpaque(false);
		gap.setPreferredSize(new Dimension(0, 5));
		gap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
		return gap;
	}

	private void refresh()
	{
		resultsContainer.revalidate();
		resultsContainer.repaint();
	}
}
