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
 * Panel listing loaded plugins by type.
 * @author joseph
 */
public class PluginConfigPanel extends JTabbedPane
{
	private Frame parent;

	/**
	 * @param parent Parent frame
	 * @param importPlugins Instance map of importing plugins
	 * @param exportPlugins Instance map of exporting plugins
	 * @param editPlugins Instance map of editing plugins
	 */
	public PluginConfigPanel(Frame parent, TreeMap<String, DiderotProjectImporter> importPlugins, TreeMap<String, DiderotProjectExporter> exportPlugins, TreeMap<String, DiderotProjectEditor> editPlugins)
	{
		super(TOP);
		this.parent = parent;

		addTab("Import", buildPluginPanel(importPlugins));
		addTab("Export", buildPluginPanel(exportPlugins));
		addTab("Edit", buildPluginPanel(editPlugins));
	}

	/**
	 * Create a panel listing plugins passed in argument.
	 * @param pluginTreeMap Plugin map to be displayed
	 * @return ScrollPane containing plugins list
	 */
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
				confBtn.addActionListener(new ConfPluginActionListener(plugin));
			}
			else
			{
				confBtn.setEnabled(false);
			}

			infoBtn.addActionListener(new InfoPluginActionListener(plugin));

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

	/**
	 * Listener for plugin configuration button.
	 */
	private class ConfPluginActionListener implements ActionListener
	{
		private DiderotPlugin plugin;

		/**
		 * @param plugin Plugin to configure
		 */
		public ConfPluginActionListener(DiderotPlugin plugin)
		{
			this.plugin = plugin;
		}

		/**
		 * Open the config dialog of the plugin.
		 * @param e {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			plugin.openConfigDialog();
		}
	}

	/**
	 * Listener for plugin information button.
	 */
	private class InfoPluginActionListener implements ActionListener
	{
		private DiderotPlugin plugin;

		/**
		 * @param plugin Plugin were information will be found.
		 */
		public InfoPluginActionListener(DiderotPlugin plugin)
		{
			this.plugin = plugin;
		}

		/**
		 * Open information dialog about plugin.
		 * @param e {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			PluginInfoDialog pluginInfoDialog = new PluginInfoDialog(parent, plugin);
			pluginInfoDialog.display();
		}
	}
}