# Introduction #
User manuals for a Siyapath User who submits a job to the framework.

Siyapath-user can submit jobs chunked into tasks to the volunteer computing system to process and send the results back.
Installing Siyapath User distribution
In Order to install Siyapath User application first download the Siyapath User distribution from the web site .
Extract the container and go to the bin directory then issue following command,

**Windows Users,
siyapath\_user.bat**

**Linux Users,
sh siyapath\_user.sh**

User has to adhere to SiyapathTask interface and implement a java class to be submitted in the .class file format. Samples are included in the source for reference as examples. (EdgeDetecterTask, SampleSiyapathTask are such examples.)
```
public interface SiyapathTask {
   void process();
   void setData(byte[] data);
   byte[] getResults();
}
```


Connect to the Siyapath System , then submit the job and related tasks by selecting the java compiled code for the program you have written implementing the org.siyapath.task.Task interface. Submit the data per each task. Described below is an example for a user submitting a task program to find prime numbers for a given data range.

![http://i46.tinypic.com/2s0y63r.png](http://i46.tinypic.com/2s0y63r.png)

![http://i48.tinypic.com/ie4dja.png](http://i48.tinypic.com/ie4dja.png)

If you need to verify the results set the number of replication nodes on which you want to run the same task on. You will be provided with a validated result.
Select a resource level from among Low, Medium and High depending on the severity of the tasks you submit. The job handling node will select a volunteer to process your task according to the resource level availability of the volunteer. Specification is  as follows.

**LOW - (ie- RAM free space  < 2GB)**MEDIUM (ie- 2GB < RAM free space < 6GB)
**HIGH (ie- RAM free space  > 6GB)**

![http://i45.tinypic.com/2j2w0hf.png](http://i45.tinypic.com/2j2w0hf.png)

![http://i50.tinypic.com/2f0btis.png](http://i50.tinypic.com/2f0btis.png)

![http://i48.tinypic.com/bf48xi.png](http://i48.tinypic.com/bf48xi.png)

After the whole job is complete, status of all tasks will be marked as done, and the final result will be visible.