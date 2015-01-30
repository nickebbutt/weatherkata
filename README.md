
#### Weather Kata ####

This exercise can be used to demonstrate techniques in Functional Reactive Programming

There are two simple User Interfaces provided - a weather publisher, and a weather subscriber

From the controller you can change various aspects of the weather.
The subscriber receives push notifications of these changes via a ZeroMQ sockets connection

The **rx-java** framework has been chosen to provide an observable stream of events within the subscriber. Your job is to hook up these event streams to drive various aspects of the subscriber UI


