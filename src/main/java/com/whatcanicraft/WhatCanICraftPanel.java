package com.whatcanicraft;

import java.awt.BorderLayout;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

/**
 * The plugin's side panel: one tab per skill, each containing its own scan button and results.
 * All methods must be called on the Swing event thread.
 */
public class WhatCanICraftPanel extends PluginPanel
{
	private final Map<CraftSkill, SkillPanel> skillPanels = new EnumMap<>(CraftSkill.class);

	public WhatCanICraftPanel(Consumer<CraftSkill> onScan)
	{
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JTabbedPane tabs = new JTabbedPane();
		tabs.setBackground(ColorScheme.DARK_GRAY_COLOR);
		tabs.setFocusable(false);

		// Tab order follows the CraftSkill enum: Herblore, then Crafting
		for (CraftSkill skill : CraftSkill.values())
		{
			SkillPanel skillPanel = new SkillPanel(() -> onScan.accept(skill));
			skillPanels.put(skill, skillPanel);
			tabs.addTab(skill.getDisplayName(), skillPanel);
		}

		add(tabs, BorderLayout.CENTER);
	}

	public void showMessage(CraftSkill skill, String message)
	{
		skillPanels.get(skill).showMessage(message);
	}

	public void showResults(CraftSkill skill, List<RecipeMatch> matches)
	{
		skillPanels.get(skill).showResults(matches);
	}
}
