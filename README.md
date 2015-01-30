
### Weather Kata ###

This exercise can be used to demonstrate techniques in Functional Reactive Programming  
It follows on from the 'streamkata' which demonstrates functional programming using jdk 1.8 streams

There are two simple User Interfaces provided - a weather publisher, and a weather subscriber

From the controller you can change various aspects of the weather.  
The subscriber receives push notifications of these changes via a ZeroMQ sockets connection

The **rx-java** framework has been chosen as the reactive programming library.  
You need jdk 1.8+ with maven to build the project (or add the maven dependencies manually)

### Your Mission ###

The existing code provides observable streams of events (as RXJava Observable) within the subscriber  
These cover attributes such as temperature, wind strength. 

Your job is to hook up these event streams to drive various aspects of the subscriber UI  
There are various forms of transport pictured and your job is to enable them when weather conditions are suitable

You should only need to modify the class **com.od.weatherkata.subscriber.WeatherSubscriber**

* Run *com.od.weatherkata.subscriber.WeatherSubscriberUI* to start the subscriber UI
* Run *com.od.weatherkata.publisher.WeatherPublisherUI* to start the publisher UI
* Run *com.od.weatherkata.RunChorusTests* to run the tests - try to make them all pass

