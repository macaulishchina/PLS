module PLS{
    interface Model{
        /*
        Get all model information.
        parameter : none
        return : json data of all models
        return example:
        [{
            "guid": "bb5b178c-de11-437f-9e78-25760c1d16b9",
            "name": "net-101",
            "location": "192.168.9.15",
            "state": "offline|starting|ready|busy",
            "description": "more information about the model."
        },
        {
            "guid": "d2f2ab74-9331-420b-a0e5-04689a32af27",
            "name": "net-50",
            "location": "192.168.9.15",
            "state": "offline|starting|ready|busy",
            "description": "more information about the model."
        }]
        */
        string getAll();
        /*
        Get a specific model information.
        parameter : the unique guid for the model
        return : json data of one model
        return example : 
        {
            "guid":"bb5b178c-de11-437f-9e78-25760c1d16b9",
            "name": "net-101",
            "location": "192.168.9.15",
            "state": "offline|starting|ready|busy",
            "description": "more information about the model."
        }
        */
        string getSpecific(string modelGuid);
        /*
        Startup the model with guid[modelGuid].
        parameter : the unique guid for the model
        return : json data 
        return example : 
        {
            "modelGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "startup",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string startup(string modelGuid);
        /*
        Shutdown the model with guid[modelGuid].
        parameter : the unique guid for the model
        return : json data 
        return example : 
        {
            "modelGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "shutdown",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string shutdown(string modelGuid);
        /*
        Re-startup the model with guid[modelGuid].
        parameter : the unique guid for the model
        return : json data 
        return example : 
        {
            "modelGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "reStartup",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string reStartup(string modelGuid);
        /*
        Similar to the "pv operation",the return value is the signal which shows how many tasks 
        the model can handle now.Recommend max value is 1. 
        parameter : the unique guid for the model
        return : the number of the available resource,if max value is 1,if the model is working 
        return 0,if free return 1.
        */
        int consumeAbilicy(string modelGuid);
    }
    interface Task{
        /*
        Create a task bound to the specified model.You can specify the type of task.
        parameter : [modelGuid] -> specify the model
                    [taskGuid] -> create the task bound to the guid
                    [taskType] -> default jpg,of cource,other type can be supported.
        return : the information of the created task
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "create",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string create(string modelGuid,string taskGuid,string taskTpye);
        /*
        Start a created task with the specific guid.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the information of the start action
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "create",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string start(string taskGuid);
        /*
        Pause a working task with the specific guid.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the information of the pause action
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "pause",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string pause(string taskGuid);
        /*
        Resume a paused task with the specific guid.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the information of the resume action
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "resume",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string resume(string taskGuid);
        /*
        Stop a working or paused task with the specific guid.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the information of the stop action
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "stop",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string stop(string taskGuid);
        /*
        Delete a Existing task with the specific guid.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the information of the delete action
        return example :
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "action": "delete",
            "result": "success|fail",
            "reason": "reason if fail"
        }
        */
        string delete(string taskGuid);
        /*
        Query the information of a specific task
        parameter : [taskGuid] -> the unique guid for the task 
        return : the json data of the task
        return example : 
        {
            "guid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "taskTpye": "jpg|avi|...",
            "state": "ready|handling|pause|finish|stop",
            "modelGuid": "bb5b178c-de11-437f-9e78-25760c1d16b9",
            "modelName": "net-101",
            "totalSize": "8458455150",
            "fileNumber": "1000"
        }
        */
        string query(string taskGuid);
        /*
        Unlike the query function,it only get the task's timely state and progress data.
        parameter : [taskGuid] -> the unique guid for the task 
        return : the state and progress data of the task
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "taskState": "ready|handling|pause|interrupt|finish|stop",
            "progress": "89"
        }
        */
        string getProgress(string taskGuid);
        /*
        Get the upload dir of the specific task.
        parameter : [taskGuid] -> the unique guid for the task 
        return ： the information of the upload dir
        return example : 
        {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "dir": "/tmp/pls/source/5c64ee48-f81c-41ef-8c93-151aa745e88f"
        }
        */
        string getUploadDir(string taskGuid);
        /*
        Get the download dir of the specific task.
        parameter : [taskGuid] -> the unique guid for the task 
        return ： the information of the download dir
        return example : 
       {
            "taskGuid": "5c64ee48-f81c-41ef-8c93-151aa745e88f",
            "dir": "/tmp/pls/result/5c64ee48-f81c-41ef-8c93-151aa745e88f"
        }
        */
        string getDownloadDir(string taskGuid);
        /*
        If remote connection,use this function to get the necessary information to connect by ftp.
        return : the host,port,username and password to for ftp connection
        return example : 
        {
            "host": "192.168.9.19",
            "port": "22",
            "username": "user",
            "password": "pw123"
        }
        */
        string getFTPInfo();
    }
}
