package ase;

import java.util.ArrayList;

import hudson.Launcher;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class BuildGamePlugin extends Builder
{
	private final String name;
	public final boolean isSelected;
	public ArrayList<Checkbox> testlist;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public BuildGamePlugin(String name, boolean isSelected,
			ArrayList<Checkbox> testlist)
	{
		this.name = name;
		this.isSelected = isSelected;
		this.testlist = testlist;
	}

	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener)
	{
		// This is what will be executed when the job is build.
		// This also shows how you can use listner and build.

		// Will be seen in the jenkins Console output
		listener.getLogger().println("The name of the test is: " + name);

		if (isSelected)
			listener.getLogger().println("Test report is created.");

		listener.getLogger().println(
				"This is job number: " + build.getDisplayName());
		return true;
	}

	// We'll use this from the config.jelly
	public String getName()
	{
		return name;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public ArrayList<Checkbox> getTestlist()
	{
		return testlist;
	}

	// Overridden for better type safety.
	@Override
	public DescriptorImpl getDescriptor()
	{
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	// This indicates to Jenkins that this is an implementation of an extension
	// point.
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder>
	{
		public boolean isApplicable(Class<? extends AbstractProject> aClass)
		{
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		public ArrayList<Checkbox> getDefaultTestlist()
		{
			ArrayList<Checkbox> testlist = new ArrayList<Checkbox>();

			Checkbox test1 = new Checkbox("Test1", false);
			Checkbox test2 = new Checkbox("Test2", false);

			testlist.add(test1);
			testlist.add(test2);

			return testlist;
		}

		// This human readable name is used in the configuration screen.
		public String getDisplayName()
		{
			return "Choose tests...";
		}
	}
}
