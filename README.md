# Discrete-Event Simulation of Ride Sharing

Project 3 for CECS 552: Modeling and Simulations with Dr. Ebert as CSULB.

For this project, we have Lynx, a ride-sharing service (similar to Uber or Lyft) functioning in a perfectly square 20 by 20 city. The basic rules are follows:

1. Lynx requests require:
   * a pick-up coordinate
   * a drop-off coordinate,
   * the # of passengers
   * whether or not to allow carpooling / ride-sharing.
2. Intermediate stops for carpooling must be along the way towards the final destination.
2. Any party picked up later than 15 minutes after the reservation is deemed free.

The goal is then to answer the following question: Assuming a fixed number of drivers over a given two-hour time period, what is the least number of drivers needed so that the probability p that "no more than 5% of passengers ride free" is at least 0.90 (where 0.90 is the left endpoint of a 95% confidence interval for p)?

Additional details are included in **des.pdf**.

## Prerequisites
* Java 9.0

## Usage
The console:

![alt text](../media/media/console.PNG?raw=true)

### 1. Single Vehicle Simulation
As this option suggests, a single vehicle is simulated to provide visual feedback and ensure that the simulation is running as expected. You will be prompted to fill in relevant details:

![alt text](../media/media/1-1.PNG?raw=true)

A single state snapshot is shown. At the top, we have the event type and time stamp. On the map, 'V' represents the vehicle and 'P' represents a passenger. State values and the capacity of the vehicle are listed below.

![alt text](../media/media/1-2.PNG?raw=true)

The empty vehicle arrives at its first set of passengers and picks them up.

![alt text](../media/media/1-3.PNG?raw=true)

As the new passengers are along the route, the vehicle heads toward it and picks them up.

![alt text](../media/media/1-4.PNG?raw=true)

The vehicle then drops off the passengers at their final destination.

![alt text](../media/media/1-5.PNG?raw=true)

### 2. 2 Hour Simulation

Given the number of drivers and reservations, this option will calculate how many passengers rode free across a 2 hour period.

![alt text](../media/media/2.PNG?raw=true)

### 3. Solution to Lynx Question
To addresses the project question, several simulations are run with varying number of drivers. Reservations are randomly made at a rate of 2 reservations per minute following an independent exponential distribution. Based on this simulation, in order for no more than 5% of passengers to ride free, **48 drivers** are required for the 2 hour time span.

![alt text](../media/media/3.PNG?raw=true)
