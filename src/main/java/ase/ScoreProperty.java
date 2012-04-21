package ase;

import hudson.model.UserProperty;
import hudson.model.User;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

public class ScoreProperty extends UserProperty
{
	private double score;
	
	public ScoreProperty()
	{
		score = 0;
	}
	
	@DataBoundConstructor
	public ScoreProperty(double score)
	{
		this.score = score;
	}
	
	@Exported
	public double getScore()
	{
		return score;
	}
	
	public void setScore(double score)
	{
		this.score = score;
	}
	
	@Exported
	public User getUser()
	{
		return user;
	}
	
}
