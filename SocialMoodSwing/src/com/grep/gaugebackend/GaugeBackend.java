/**
 * GaugeBackend.java
 * 
 * @author Gresham, Ryan, Everett, Pierce
 */

package com.grep.gaugebackend;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * public class GaugeBackend
 */
public class GaugeBackend {
	
	// threads for each module
	static protected Thread m_getterThread;
	static protected Thread m_weighterThread;
	static protected Thread m_sentimenterThread;
	static protected Thread m_aggregatorThread;

	public static void start(String[] keywords, BlockingQueue<Tweet> popularTweets, BlockingQueue<Gauge> gaugeValues, long duration) {
		// interprocess communication structures
		BlockingQueue<Tweet> fetchQueue = new ArrayBlockingQueue<Tweet>(5);
		BlockingQueue<Tweet> weightQueue = new ArrayBlockingQueue<Tweet>(5);
		BlockingQueue<Tweet> sentimentQueue = new ArrayBlockingQueue<Tweet>(5);
		
		// create the threads
		GetTweets getter = new GetTweets(fetchQueue, keywords);
		GetWeight weighter = new GetWeight(fetchQueue, weightQueue, keywords);
		GetSentiment sentimenter = new GetSentiment(weightQueue, sentimentQueue);
		Aggregate aggregator = new Aggregate(sentimentQueue, popularTweets, gaugeValues);
		
		m_getterThread = new Thread(getter);
		m_weighterThread = new Thread(weighter);
		m_sentimenterThread = new Thread(sentimenter);
		m_aggregatorThread = new Thread(aggregator);
		
		// start the threads
		m_getterThread.start();
		m_weighterThread.start();
		m_sentimenterThread.start();
		m_aggregatorThread.start();
		
		// the analysis duration timer
		Timer t = new Timer();
		t.schedule(new TimerTask() {          
			@Override
			public void run() {
				stop();
			}
		}, duration*1000);
	}
	
	public static void stop() {
		System.out.println("trying to stop the threads...");
		
		try {
			// send interrupt signals
			m_getterThread.interrupt();
			m_weighterThread.interrupt();
			m_sentimenterThread.interrupt();
			m_aggregatorThread.interrupt();
			// wait for threads to finish
			m_getterThread.join();
			m_weighterThread.join();
			m_sentimenterThread.join();
			m_aggregatorThread.join();
			
			
		} catch (InterruptedException ex) {
			System.out.println("something went wrong while killing the threads");
		}
	}

}

