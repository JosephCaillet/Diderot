package plugin.exporter;

import org.w3c.dom.Document;
import plugin.PluginsSettings;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

/**
 * Created by joseph on 20/10/16.
 */
public class DefaultDiderotDocumentationExporter extends DefaultDiderotProjectExporter
{
	static private HashMap<String, String> availableOperations = new HashMap<>();

	static
	{
		availableOperations.put("Generate documentation", "generateHtmlDocumentation");
	}

	//method strongly inspired by kayz1's solution here: http://stackoverflow.com/questions/6214703/copy-entire-directory-contents-to-another-directory
	static public boolean copyDirectory(File source, File destination)
	{
		if (source.isDirectory())
		{
			if (!destination.exists())
			{
				destination.mkdirs();
			}

			String files[] = source.list();

			for (String file : files)
			{
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				if(!copyDirectory(srcFile, destFile))
				{
					return false;
				}
			}
		}
		else
		{
			InputStream in = null;
			OutputStream out = null;

			try
			{
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0)
				{
					out.write(buffer, 0, length);
				}
			}
			catch (Exception e)
			{
				try
				{
					in.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					return false;
				}

				try
				{
					out.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					return false;
				}
				return false;
			}
		}

		return true;
	}

	@Override
	public HashMap<String, String> getAvailableExportingOperations()
	{
		return availableOperations;
	}

	@Override
	public String getPluginName()
	{
		return "Diredot default documentation exporter";
	}

	@Override
	public String getPluginAuthor()
	{
		return "Joseph Caillet";
	}

	@Override
	public String getPluginContactInformation()
	{
		return "https://github.com/JosephCaillet/Diderot";
	}

	@Override
	public String getPluginVersion()
	{
		return "0.1";
	}

	@Override
	public String getPluginDescription()
	{
		return "Generate Html documentation of your routes";
	}

	public void generateHtmlDocumentation()
	{
		String fileName = PluginsSettings.getValue(getPluginName() + "documentationFolder");
		if(fileName == null)
		{
			fileName = PluginsSettings.getValue("Diderot default project exporter" + "projectFileName");
			if(fileName == null)
			{
				fileName = ".";
			}
		}


		JFileChooser fileChooser = new JFileChooser(fileName);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(parent))
		{
			return;
		}

		String docPath = fileChooser.getSelectedFile().getAbsolutePath();
		if(!docPath.endsWith("/"))
		{
			docPath = docPath + "/";
		}

		PluginsSettings.setValue(getPluginName() + "documentationFolder", docPath);

		try
		{
			Document xmlSaveDocument = createDocument();

			StreamSource streamSource = new StreamSource("generateDocumentation.xsl");

			Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer(streamSource);
			xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			copyDirectory(new File("documentationTemplate/"), new File(docPath));

			File f = new File(docPath + "index.html");
			xmlTransformer.transform(new DOMSource(xmlSaveDocument), new StreamResult(f));


			Desktop.getDesktop().browse(f.toURI());
		}
		catch(ParserConfigurationException | TransformerException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}