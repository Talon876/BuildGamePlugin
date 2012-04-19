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

	@DataBoundConstructor
	public BuildGamePlugin()
	{
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
	{
		listener.getLogger().println("Executing BuildGame plugin...");
		
		String url = getSonarUrl(build.getProject());
		listener.getLogger().println("getSonarUrl returned: " + url);
		//do stuff
		int pointValue = ComputePoints.getPointValue("com.deflexicon.bot:SkypeBot", "http://192.168.1.128:9000", "sonar", "sonar");
		listener.getLogger().println("That build was worth " + pointValue + " points.");
		
		listener.getLogger().println("Finished BuildGame plugin execution.");
		return true;
	}
	
	private String getSonarUrl(AbstractProject<?,?> project)
	{
		//SonarInstallation sonarInstallation = getInstallation();
		String url = "";
		if(project instanceof AbstractMavenProject)
		{
			 AbstractMavenProject mavenProject = (AbstractMavenProject) project;
			 if(mavenProject.getRootProject() instanceof MavenModuleSet)
			 {
				 MavenModuleSet mms = (MavenModuleSet) mavenProject.getRootProject();
				 MavenModule rootModule = mms.getRootModule();
				 if(rootModule != null)
				 {
					 ModuleName moduleName = rootModule.getModuleName();
					 url += moduleName.groupId + ":" + moduleName.artifactId;
				 }
			 }
		}
		return url;
	}

	@Extension(ordinal=999)
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
}
