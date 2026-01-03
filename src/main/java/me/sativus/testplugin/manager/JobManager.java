package me.sativus.testplugin.manager;

public class JobManager {
    private static JobManager instance;
    

    private JobManager() {
    }

    public JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }


}
