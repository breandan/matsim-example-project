package org.matsim.tutorial.class2017.extensions.parkingsearch;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.contrib.parking.parkingsearch.evaluation.ParkingSlotVisualiser;
import org.matsim.contrib.parking.parkingsearch.sim.SetupParking;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup.SnapshotStyle;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

import com.google.inject.Binder;

/**
 * @author jbischoff An example how to use parking search in MATSim.
 *         Technically, all you need as extra input is a facilities file
 *         containing "car interaction" locations.
 *
 *
 */

public class RunParkingSearchExample {

	public static void main(String[] args) {
		
		Config config = ConfigUtils.loadConfig("parkingsearch/config.xml");
		//all further input files are set in the config.
		
		// set to false, if you don't require visualisation, then the example will run for 11 iterations, with OTFVis, only one iteration is performed. 
		boolean otfvis = false;
		if (otfvis) {
			config.controler().setLastIteration(0);
		} else {
			config.controler().setLastIteration(10);
		}
		new RunParkingSearchExample().run(config,otfvis);

	}

	/**
	 * @param config
	 * 			a standard MATSim config
	 * @param otfvis
	 *            turns otfvis visualisation on or off
	 */
	public void run(Config config, boolean otfvis) {
		final Scenario scenario = ScenarioUtils.loadScenario(config);
		Controler controler = new Controler(scenario);
		config.qsim().setSnapshotStyle(SnapshotStyle.withHoles);

		
		controler.addOverridingModule(new AbstractModule() {
			
			@Override
			public void install() {
				ParkingSlotVisualiser visualiser = new ParkingSlotVisualiser(scenario);
				addEventHandlerBinding().toInstance(visualiser);
				addControlerListenerBinding().toInstance(visualiser);
			}
		});
		
		if (otfvis) {
			controler.addOverridingModule(new OTFVisLiveModule());
		}
		SetupParking.installParkingModules(controler);
		controler.run();
	}

}