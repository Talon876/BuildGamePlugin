package ase;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

public class ComputePoints
{
	private static final String[] METRICS = { "comment_lines_density", "public_documented_api_density",
		"class_complexity", "function_complexity", "file_complexity", "skipped_tests", "test_success_density", "coverage",
		"it_coverage", "duplicated_blocks", "duplicated_files", "duplicated_lines_density", "violations_density",
		"suspect_lcom4_density", "package_tangle_index", "file_tangle_index", "business_value", "technical_debt_ratio" };

	
	private static final double[] DEFAULT_WEIGHTS = { 1.0, 1.0, -1.0, -1.0, -1.0, -1.0, 0.1, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0, -1.0, -1.0, -1.0, 0.0, -1.0 };


	/**
	 * Calculates the point value to be awarded for the current build. 
	 * @param projectName The name of the project to analyze in the form: groupId:artifactId
	 * @param sonarUrl The http url to sonar
	 * @param sonarUsername The username to the sonar database (default = sonar)
	 * @param sonarPassword The password to the sonar database (default = sonar)
	 * @return The score to assign to the developer
	 */
	public static double getPointValue(String projectName, String sonarUrl, String sonarUsername, String sonarPassword)
	{
		double pointValue = 0;
		pointValue = ComputePoints.getPointValue(projectName, sonarUrl, sonarUsername, sonarPassword, ComputePoints.DEFAULT_WEIGHTS);		
		return pointValue;
	}
	
	/**
	 * Calculates the point value to be awarded for the current build. 
	 * @paramprojectName The name of the project to analyze in the form: groupId:artifactId
	 * @param sonarUrl The http url to sonar
	 * @param sonarUsername The username to the sonar database (default = sonar)
	 * @param sonarPassword The password to the sonar database (default = sonar)
	 * @param weights A 52-element array of weights corresponding to the metrics.
	 * @return The score to assign to the developer
	 */
	public static double getPointValue(String projectName, String sonarUrl, String sonarUsername, String sonarPassword, double[] weights)
	{
		double pointValue = 0.0;
		
		Sonar sonar = Sonar.create(sonarUrl, sonarUsername, sonarPassword);
		ResourceQuery query = ResourceQuery.createForMetrics(projectName, ComputePoints.METRICS);
		query.setIncludeTrends(true);
		System.out.println("URL: " + query.getUrl());
		Resource struts = sonar.find(query);
		
		for(int i = 0; i < ComputePoints.METRICS.length; i++)
		{
			Measure currentMeasure = struts.getMeasure(ComputePoints.METRICS[i]);
			if(currentMeasure != null)
			{
				try
				{
					pointValue += currentMeasure.getVariation1() * weights[i];
					System.out.println(ComputePoints.METRICS[i] + " was worth " + currentMeasure.getVariation1() * weights[i]);
				}
				catch(Exception ex)
				{
					System.out.println(ComputePoints.METRICS[i] + " does not have a previous value.\nThis can be ignored if this is the first build.");
				}
			}
		}
		int roundedPointValue = (int) Math.round(pointValue);
		return roundedPointValue;
	}
	
	
}
