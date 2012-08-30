package org.siyapath;

/**
 * Global constants for the Siyapath project
 */
public final class SiyapathConstants {

    private SiyapathConstants(){
    }

    /**
     * Keyword siyapath *
     */
    public static final String SIYAPATH = "siyapath";
    /**
     * Keys for SystemInformation *
     */
    public static final String CPU_INFO = "CPU_INFO";
    public static final String MEMORY_INFO = "MEMORY_INFO";
    /**
     * Member limits for gossip lists *
     */
    public static final int MEMBER_SET_LIMIT = 10;
    public static final int RESOURCE_MEMBER_SET_LIMIT = 20;
    public static final int BOOSTRAPPER_MEMBER_SET_LIMIT = 50;

    public static final int TASK_QUEUE_CAPACITY = 500;
    public static final int TASK_COLLECTOR_POOL_SIZE = 200;
    public static final int TASK_DISPATCHER_POOL_SIZE = 100;

    public static final int PARALLEL_TASKS=3;

    public static final int MAX_TASK_UPDATE_INTERVAL_MILLIS = 30000;
    public static final int TASK_TRACKER_INTERVAL = 10000;      //milliseconds

    /**
     * Time interval in milliseconds which the backup node uses to check if
     * job processor is alive
     */
    public static final int BACKUP_STATUS_CHECK_INTERVAL = 20000;

    /**
     * Gossip frequency in milliseconds
     */
    public static final int GOSSIP_FREQUENCY_MILLIS = 3000;

    public static final int TASK_DISPATCH_FREQUENCY_MILLIS = 500;


    public static final boolean LOCAL_TEST = false;

    public static final String BOOTSRAPPER_IP = "127.0.0.1";
    public static final int BOOTSRAPPER_PORT = 9020;


}
