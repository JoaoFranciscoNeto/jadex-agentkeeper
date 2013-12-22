package jadex.agentkeeper.log;

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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.general.Series;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.extension.envsupport.observer.graphics.jmonkey.MonkeyApp;

public class PerformanceTracker {

	private static int frameRateLogCounter = 100;
	private static int takeChartImageCounter = 1000;
	
	private static int rateCounter = 0;
	private static float secondCounter = 0.0f;
	private static int frameCounter = 0;
	private static int computedFrameRate = 0;
	private static HashMap<Integer, List<Integer>> performanceLogEntrys = new HashMap<Integer, List<Integer>>();
	private static XYSeries series0 = new XYSeries("average Framerate");
	private static XYSeries series1 = new XYSeries("claimed Sectors");
	private static XYSeries series2 = new XYSeries("Imps");
	private static XYSeries series3 = new XYSeries("GOBLIN");
	private static XYSeries series4 = new XYSeries("WARLOCK");
	private static XYSeries series5 = new XYSeries("TROLL");
	

	public static void logframeRate(MonkeyApp app, SimplePlayerState playerState, SimpleCreatureState creatureState) {
		float timerFrameRate = app.getTimer().getFrameRate();
		computeFrameRate(app);
		if (frameRateLogCounter == 0) {
			frameRateLogCounter = 40;
			rateCounter++;
//			System.out.println(rateCounter + ";" + timerFrameRate + ";" + playerState.getClaimedSectors() + ";" + creatureState.getCreatureCount(InitMapProcess.IMP) + ";"
//					+ creatureState.getCreatureCount(InitMapProcess.GOBLIN) + ";" + creatureState.getCreatureCount(InitMapProcess.WARLOCK) + ";" + creatureState.getCreatureCount(InitMapProcess.TROLL)
//					+ ";" + computedFrameRate);
			series0.add(rateCounter, computedFrameRate);
			series1.add(rateCounter, playerState.getClaimedSectors());
			series2.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.IMP));
			series3.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.GOBLIN));
			series4.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.WARLOCK));
			series5.add(rateCounter, creatureState.getCreatureCount(InitMapProcess.TROLL));
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
	
	
	
	public static void printPerformanceChart() {
		final XYSeriesCollection coll0 = new XYSeriesCollection(series0);
		coll0.addSeries(series1);
		final IntervalXYDataset data1 = coll0;
		JFreeChart agentPerformanceChart = ChartFactory.createTimeSeriesChart("agent keeper perfromance", "timeline", "average fps", data1, true, true, false);
		agentPerformanceChart.setBackgroundPaint(Color.WHITE);
		agentPerformanceChart.setTextAntiAlias(true);

		Plot plot = agentPerformanceChart.getPlot();
		XYPlot xyplot = agentPerformanceChart.getXYPlot();

		plot.setBackgroundAlpha(0);
		final NumberAxis axis2 = new NumberAxis("Creatures");
		
		axis2.setAutoRangeIncludesZero(false);
		xyplot.setRangeAxis(2, axis2);
		final XYSeriesCollection coll1 = new XYSeriesCollection(series2);
		
		
		coll1.addSeries(series3);
		coll1.addSeries(series4);
		coll1.addSeries(series5);
		final IntervalXYDataset data2 = coll1;
		xyplot.setDataset(1, data2);
		xyplot.mapDatasetToRangeAxis(1, 2);
		
		series2.getKey();
		
		final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        xyplot.setRenderer(1, renderer2);
		
		try {
			DateFormat dfmt = new SimpleDateFormat("dd_MM_yy_hh-mm-ss");
			String path = PerformanceTracker.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = URLDecoder.decode(path, "UTF-8");
			System.out.println("ChartFile: "+path+"agentKeeperPerformanceChart" + dfmt.format(new Date()) + ".png" );
			ChartUtilities.saveChartAsPNG(new File(path+"agentKeeperPerformanceChart" + dfmt.format(new Date()) + ".png"), agentPerformanceChart, 1000, 700);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
