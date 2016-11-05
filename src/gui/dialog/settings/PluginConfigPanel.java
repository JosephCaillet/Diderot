package gui.dialog.settings;

import gui.ImageIconProxy;
import plugin.DiderotPlugin;
import plugin.editor.DiderotProjectEditor;
import plugin.exporter.DiderotProjectExporter;
import plugin.importer.DiderotProjectImporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by joseph on 28/09/16.
 */
public class PluginConfigPanel extends JTabbedPane
{
	private Frame parent;

	public PluginConfigPanel(Frame parent, TreeMap<String, DiderotProjectImporter> importPlugins, TreeMap<String, DiderotProjectExporter> exportPlugins, TreeMap<String, DiderotProjectEditor> editPlugins)
	{
		super(TOP);
		this.parent = parent;

		addTab("Import", buildPluginPanel(importPlugins));
		addTab("Export", buildPluginPanel(exportPlugins));
		addTab("Edit", buildPluginPanel(editPlugins));
	}

	private JScrollPane buildPluginPanel(TreeMap<String, ? extends DiderotPlugin> pluginTreeMap)
	{
		Box box = Box.createVerticalBox();
		for(Map.Entry<String, ? extends DiderotPlugin> pluginEntry : pluginTreeMap.entrySet())
		{
			JPanel nameContainer = new JPanel(new BorderLayout());
			JPanel buttonContainer = new JPanel();

			DiderotPlugin plugin = pluginEntry.getValue();

			JLabel pluginLbl = new JLabel(plugin.getPluginName());
			pluginLbl.setToolTipText(plugin.getPluginDescription());

			JButton confBtn = new JButton(ImageIconProxy.getIcon("conf2"));
			JButton infoBtn = new JButton(ImageIconProxy.getIcon("info"));

			if(plugin.isConfigurable())
			{
				confBtn.addActionListener(new confPluginActionListener(plugin));
			}
			else
			{
				confBtn.setEnabled(false);
			}

			infoBtn.addActionListener(new infoPluginActionListener(plugin));

			buttonContainer.add(confBtn);
			buttonContainer.add(infoBtn);

			nameContainer.add(pluginLbl, BorderLayout.WEST);
			nameContainer.add(buttonContainer, BorderLayout.EAST);
			nameContainer.setMaximumSize(new Dimension(9999999, nameContainer.getMinimumSize().height));

			box.add(nameContainer);
		}

		box.setOpaque(true);

		JScrollPane scrollPane = new JScrollPane(box);
		scrollPane.getViewport().setOpaque(false);

		return scrollPane;
	}

	private class confPluginActionListener implements ActionListener
	{
		private DiderotPlugin plugin;

		public confPluginActionListener(DiderotPlugin plugin)
		{
			this.plugin = plugin;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			plugin.openConfigDialog();
		}
	}

	private class infoPluginActionListener implements ActionListener
	{
		private DiderotPlugin plugin;

		public infoPluginActionListener(DiderotPlugin plugin)
		{
			this.plugin = plugin;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			PluginInfoDialog pluginInfoDialog = new PluginInfoDialog(parent, plugin);
			pluginInfoDialog.setVisible(true);
		}
	}
}