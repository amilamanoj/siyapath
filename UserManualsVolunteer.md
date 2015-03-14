# Introduction #

Siyapath can be considered as a standalone product with two distributions ,Volunteer application and User application.


## Siyapath Volunteer Distribution ##

Siyapath volunteer distribution can be installed on a single machine and when creating a network of siyapath nodes, it should be installed on each physical machine separately. To connect to a siyapath volunteer computing system each machine should be configured with the details of the specific Siyapath system that is deployed. Each machine could be considered as components of the distributed Siyapath volunteer computing system. Configuration file will be shipped Siyapath Volunteer distribution which contains following default information.

  * PARALLEL\_TASKS = 3
  * GOSSIP\_FREQUENCY\_MILLIS = 3000
  * TASK\_DISPATCH\_FREQUENCY\_MILLIS = 500
  * BOOTSRAPPER\_IP = "10.8.108.97"
  * BOOTSRAPPER\_PORT = 9020

Installing Siyapath Volunteer distribution

In Order to install Siyapath Volunteer application first download the Siyapath Volunteer distribution from siyapath website .
Extract the container and go to the bin directory then  issue following command,

Windows Users,
  * siyapath\_volunteer.bat

Linux Users,
  * sh siyapath\_volunteer.sh

This will popup the following GUI for Siyapath  Volunteer

![http://siyapath.org/images/Vol_Dis1.png](http://siyapath.org/images/Vol_Dis1.png)

This UI shows following the details related to Volunteer statistics.
  * PeerListener,PeerWorker Status
  * Number of members known to the volunteer
  * Job Processing Status (IDLE/PROCESSING/DISTRIBUTER)