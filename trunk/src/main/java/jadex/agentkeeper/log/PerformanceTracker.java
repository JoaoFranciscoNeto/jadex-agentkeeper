package jadex.agentkeeper.log;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISO;
import jadex.extension.envsupport.observer.graphics.jmonkey.MonkeyApp;

public class PerformanceTracker {

	/*Performance Collection:*/
	private static int frameRateLogCounter = 100;
	private static int takeChartImageCounter = 1000;
	private static int rateCounter = 0;
	private static float secondCounter = 0.0f;
	private static int frameCounter = 0;
	private static int computedFrameRate = 0;
	
	/*Chart Creation:*/
	private static HashMap<Integer, List<Integer>> performanceLogEntrys = new HashMap<Integer, List<Integer>>();
	private static XYSeries frameRateSeries = new XYSeries("average Framerate");
	private static XYSeries claimedSectorSeries = new XYSeries("claimed Sectors");
	private static XYSeries impCountSeries = new XYSeries("Imps");
	private static XYSeries goblinCountSeries = new XYSeries("GOBLIN");
	private static XYSeries warlockCountSeries = new XYSeries("WARLOCK");
	private static XYSeries trollCountSeries = new XYSeries("TROLL");
	private static XYSeries taskSizeSeries = new XYSeries("TaskSize");
	
	
	private static final float CHART_STROKE_WEIGHT = 2f;
	

	public static void logframeRate(MonkeyApp app, SimplePlayerState playerState, SimpleCreatureState creatureState) {
		computeFrameRate(app);
		if (frameRateLogCounter == 0) {
			frameRateLogCounter = 40;
			rateCounter++;
			Auftragsverwalter auftragsverwalter = (Auftragsverwalter)app.getSpaceController().getProperty(ISO.Objects.TaskList);
//			System.out.println(rateCounter + ";" + timerFrameRate + ";" + playerState.getClaimedSectors() + ";" + creatureState.getCreatureCount(InitMapProcess.IMP) + ";"
//					+ creatureState.getCreatureCount(InitMapProcess.GOBLIN) + ";" + creatureState.getCreatureCount(InitMapProcess.WARLOCK) + ";" + creatureState.getCreatureCount(InitMapProcess.TROLL)
//					+ ";" + computedFrameRate);
			
			
			taskSizeSeries.add(rateCounter, auftragsverwalter.getTaskListSize() );
			frameRateSeries.add(rateCounter, computedFrameRate);
			claimedSectorSeries.add(rateCounter, playerState.getClaimedSectors());
			impCountSeries.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.IMP));
			goblinCountSeries.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.GOBLIN));
			warlockCountSeries.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.WARLOCK));
			trollCountSeries.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.TROLL));
			performanceLogEntrys.put(
					rateCounter,
					Arrays.asList(playerState.getClaimedSectors(), creatureState.getCreatureCount(InitMapProcess.IMP), creatureState.getCreatureCount(InitMapProcess.GOBLIN),
							creatureState.getCreatureCount(InitMapProcess.WARLOCK), creatureState.getCreatureCount(InitMapProcess.TROLL), computedFrameRate ));
			if(takeChartImageCounter <= 0){
				takeChartImageCounter = 1000;
				//printPerformanceChart();
			}
			
		}
		takeChartImageCounter--;
		frameRateLogCounter--;
	}

	private static void computeFrameRate(MonkeyApp app) {
		secondCounter += app.getTimer().getTimePerFrame();
		frameCounter++;
		if (secondCounter >= 1.0f) {
			int fps = (int) (frameCounter / secondCounter);
			computedFrameRate = fps;
			secondCounter = 0.0f;
			frameCounter = 0;
		}
	}
	
	
	/**
	 * Using JFreechart to create an Performance Chart over saved values.
	 * 
	 */
	public static void printPerformanceChart() {
		final XYSeriesCollection coll0 = new XYSeriesCollection(frameRateSeries);
		coll0.addSeries(claimedSectorSeries);
		coll0.addSeries(taskSizeSeries);
		final IntervalXYDataset data1 = coll0;
		JFreeChart agentPerformanceChart = ChartFactory.createTimeSeriesChart("agent keeper perfromance", "timeline", "average fps", data1, true, true, false);
		agentPerformanceChart.setBackgroundPaint(Color.WHITE);
		agentPerformanceChart.setTextAntiAlias(true);

		Plot plot = agentPerformanceChart.getPlot();
		XYPlot xyplot = agentPerformanceChart.getXYPlot();
		// Value Axis Y 1 in Steps of 1
//		NumberAxis domainAxis = (NumberAxis) xyplot.getRangeAxis();
//		domainAxis.setTickUnit(new NumberTickUnit(1));
		
		plot.setBackgroundAlpha(0);
		final NumberAxis axis2 = new NumberAxis("Creatures");
		
		axis2.setAutoRangeIncludesZero(false);
		xyplot.setRangeAxis(2, axis2);
		
		// Value Axis Y 2 in Steps of 1
		NumberAxis domainAxis2 = (NumberAxis) xyplot.getRangeAxis(2);
		domainAxis2.setTickUnit(new NumberTickUnit(1));
		
		// fill second y axis with life:
		final XYSeriesCollection coll1 = new XYSeriesCollection(impCountSeries);
		coll1.addSeries(goblinCountSeries);
		coll1.addSeries(warlockCountSeries);
		coll1.addSeries(trollCountSeries);
		final IntervalXYDataset data2 = coll1;
		xyplot.setDataset(1, data2);
		xyplot.mapDatasetToRangeAxis(1, 2);
		
		final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        xyplot.setRenderer(1, renderer2);
        
		// stroke weight
        XYItemRenderer renderer = xyplot.getRenderer();
		renderer.setBaseStroke(new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesStroke(0, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesStroke(1, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer2.setSeriesStroke(0, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer2.setSeriesStroke(1, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer2.setSeriesStroke(2, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		renderer2.setSeriesStroke(3, new BasicStroke(CHART_STROKE_WEIGHT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        
        saveChartToImageFile(agentPerformanceChart);
	}
	
	/**
	 * Print performance chart, for check the performance over time.
	 * Useful to get an impression of the performance.
	 * 
	 * 
	 * @param agentPerformanceChart JfFreeChart - final Chart for printing
	 */
	private static void saveChartToImageFile(JFreeChart agentPerformanceChart){
		try {
			DateFormat dfmt = new SimpleDateFormat("dd_MM_yy_hh-mm-ss");
			String path = PerformanceTracker.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = URLDecoder.decode(path, "UTF-8");
			if(path.endsWith(".jar")){
				path = path.substring(0, path.lastIndexOf("/"))+"/";
			}
			
			System.out.println("Chart file: "+path+"agentKeeperPerformanceChart" + dfmt.format(new Date()) + ".png" );
			ChartUtilities.saveChartAsPNG(new File(path+"agentKeeperPerformanceChart" + dfmt.format(new Date()) + ".png"), agentPerformanceChart, 1000, 700);
		} catch (IOException e) {
			System.out.println("Problem with performance chart output.");
			e.printStackTrace();
		}
	}

}
