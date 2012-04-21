package ase;

import hudson.Extension;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.model.User;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public class ScorePropertyDescriptor extends UserPropertyDescriptor
{
	
	public ScorePropertyDescriptor()
	{
		super(ScoreProperty.class);
	}

	@Override
	public UserProperty newInstance(User user)
	{
		return null;
	}

	@Override
	public String getDisplayName()
	{
		return "BuildGame Plugin";
	}
	
	@Override
	public ScoreProperty newInstance(StaplerRequest request, JSONObject data) throws FormException
	{
		if(data.has("score"))
		{
			return request.bindJSON(ScoreProperty.class, data);
		}
		
		return new ScoreProperty();
		
	}
	
}
