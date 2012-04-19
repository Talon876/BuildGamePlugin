package ase;

import hudson.Extension;
import hudson.Launcher;
import hudson.maven.AbstractMavenProject;
import hudson.maven.ModuleName;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.DataBoundConstructor;

public class BuildGamePlugin extends Notifier
{
	private String sonarUrl = "";
	private String sonarUsername = "";
	private String sonarPassword = "";

	@DataBoundConstructor
	public BuildGamePlugin(String sonarUrl, String sonarUsername, String sonarPassword)
	{
		this.sonarUrl = sonarUrl;
		this.sonarUsername = sonarUsername;
		this.sonarPassword = sonarPassword;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
	{
		listener.getLogger().println("Executing BuildGame plugin...");

		String project = getProjectId(build.getProject());
		listener.getLogger().println("Project Id returned: " + project);
		listener.getLogger().println("Sonar URL from textfield: " + sonarUrl);
		listener.getLogger().println("Sonar Database Username from textfield: " + sonarUsername);
		listener.getLogger().println("Sonar Database Password from textfield: " + sonarPassword);

		int pointValue = ComputePoints.getPointValue(project, sonarUrl, sonarUsername, sonarPassword);
		listener.getLogger().println("That build was worth " + pointValue + " points.");
		listener.getLogger().println("Finished BuildGame plugin execution.");
		return true;
	}

	private String getProjectId(AbstractProject<?, ?> project)
	{
		// SonarInstallation sonarInstallation = getInstallation();
		String url = "";
		if (project instanceof AbstractMavenProject)
		{
			AbstractMavenProject mavenProject = (AbstractMavenProject) project;
			if (mavenProject.getRootProject() instanceof MavenModuleSet)
			{
				MavenModuleSet mms = (MavenModuleSet) mavenProject.getRootProject();
				MavenModule rootModule = mms.getRootModule();
				if (rootModule != null)
				{
					ModuleName moduleName = rootModule.getModuleName();
					url += moduleName.groupId + ":" + moduleName.artifactId;
				}
			}
		}
		return url;
	}

	@Extension(ordinal = 999)
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher>
	{
		public boolean isApplicable(Class<? extends AbstractProject> aClass)
		{
			return true;
		}

		// This human readable name is used in the configuration screen.
		public String getDisplayName()
		{
			return "Run BuildGame";
		}
	}

	public BuildStepMonitor getRequiredMonitorService()
	{
		return BuildStepMonitor.BUILD;
	}

	public String getSonarUrl()
	{
		return sonarUrl;
	}

	public String getSonarUsername()
	{
		return sonarUsername;
	}

	public String getSonarPassword()
	{
		return sonarPassword;
	}
}
