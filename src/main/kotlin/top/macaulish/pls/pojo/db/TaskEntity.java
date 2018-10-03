package top.macaulish.pls.pojo.db;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author huyidong
 * @date 2018/10/3
 */
public class TaskEntity {
    private int id;
    private String guid;
    private String taskName;
    private String taskType;
    private Integer publish;
    private String state;
    private String modelGuid;
    private String modelName;
    private String userGuid;
    private Timestamp createTime;
    private String savePath;
    private String saveHost;
    private Long taskSize;
    private Integer taskNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getModelGuid() {
        return modelGuid;
    }

    public void setModelGuid(String modelGuid) {
        this.modelGuid = modelGuid;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveHost() {
        return saveHost;
    }

    public void setSaveHost(String saveHost) {
        this.saveHost = saveHost;
    }

    public Long getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Long taskSize) {
        this.taskSize = taskSize;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id == that.id &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(taskType, that.taskType) &&
                Objects.equals(publish, that.publish) &&
                Objects.equals(state, that.state) &&
                Objects.equals(modelGuid, that.modelGuid) &&
                Objects.equals(modelName, that.modelName) &&
                Objects.equals(userGuid, that.userGuid) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(savePath, that.savePath) &&
                Objects.equals(saveHost, that.saveHost) &&
                Objects.equals(taskSize, that.taskSize) &&
                Objects.equals(taskNumber, that.taskNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, guid, taskName, taskType, publish, state, modelGuid, modelName, userGuid, createTime, savePath, saveHost, taskSize, taskNumber);
    }
}
