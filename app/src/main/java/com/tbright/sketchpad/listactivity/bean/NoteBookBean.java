package com.tbright.sketchpad.listactivity.bean;

import java.util.List;

public class NoteBookBean {

    /**
     * clientVersion : 1.5.0
     * createTime : 1572939666109
     * currentPageIndex : 0
     * deviceId : JM02V00AE006VK8JU8GW
     * deviceType : AndroidPad
     * docType : 12
     * lastNotePageId : 712425682738614272
     * notebookDesc : null
     * notebookId : 712425681283190784
     * notebookServerId : 712425681283190784
     * notebookTitle : 11月5日作业-按题批分
     * notepages : [{"createTime":1572939671582,"hasContent":1,"notebookId":"712425681283190784","notepageId":"712425682738614272","notepageServerId":"712425682738614272","taskId":null,"updateTime":1573717988004},{"createTime":1572939689997,"hasContent":1,"notebookId":"712425681283190784","notepageId":"712425753983062016","notepageServerId":"712425753983062016","taskId":null,"updateTime":1572939689997},{"createTime":1572939692051,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425782042955776","notepageServerId":"712425782042955776","taskId":null,"updateTime":1572939692051},{"createTime":1572939696375,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425790750330880","notepageServerId":"712425790750330880","taskId":null,"updateTime":1572939696375},{"createTime":1572939699940,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823193272320","notepageServerId":"712425823193272320","taskId":null,"updateTime":1572939699940},{"createTime":1572939699967,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823306518528","notepageServerId":"712425823306518528","taskId":null,"updateTime":1572939699967},{"createTime":1572939699992,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823411376128","notepageServerId":"712425823411376128","taskId":null,"updateTime":1572939699992},{"createTime":1572939700016,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823512039424","notepageServerId":"712425823512039424","taskId":null,"updateTime":1572939700016},{"createTime":1572939700041,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823616897024","notepageServerId":"712425823616897024","taskId":null,"updateTime":1572939700041},{"createTime":1572939700067,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823725948928","notepageServerId":"712425823725948928","taskId":null,"updateTime":1572939700067},{"createTime":1572939700093,"hasContent":0,"notebookId":"712425681283190784","notepageId":"712425823835000832","notepageServerId":"712425823835000832","taskId":null,"updateTime":1572939700093}]
     * orientation : 2
     * osVersion : null
     * pageCount : 11
     * resolution : 822x1200
     * subjectId : XK_01
     * taskId : 712424997901045760
     * templateId :
     * templateType : 1
     * timetableSubjectId : null
     * updateTime : 1573717988023
     * userId : 707708297662173184
     * version : 1.0
     */

    private String clientVersion;
    private long createTime;
    private int currentPageIndex;
    private String deviceId;
    private String deviceType;
    private int docType;
    private String lastNotePageId;
    private Object notebookDesc;
    private String notebookId;
    private String notebookServerId;
    private String notebookTitle;
    private int orientation;
    private Object osVersion;
    private int pageCount;
    private String resolution;
    private String subjectId;
    private String taskId;
    private String templateId;
    private int templateType;
    private Object timetableSubjectId;
    private long updateTime;
    private String userId;
    private String version;
    private List<NotepagesBean> notepages;

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public String getLastNotePageId() {
        return lastNotePageId;
    }

    public void setLastNotePageId(String lastNotePageId) {
        this.lastNotePageId = lastNotePageId;
    }

    public Object getNotebookDesc() {
        return notebookDesc;
    }

    public void setNotebookDesc(Object notebookDesc) {
        this.notebookDesc = notebookDesc;
    }

    public String getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(String notebookId) {
        this.notebookId = notebookId;
    }

    public String getNotebookServerId() {
        return notebookServerId;
    }

    public void setNotebookServerId(String notebookServerId) {
        this.notebookServerId = notebookServerId;
    }

    public String getNotebookTitle() {
        return notebookTitle;
    }

    public void setNotebookTitle(String notebookTitle) {
        this.notebookTitle = notebookTitle;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Object getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Object osVersion) {
        this.osVersion = osVersion;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public Object getTimetableSubjectId() {
        return timetableSubjectId;
    }

    public void setTimetableSubjectId(Object timetableSubjectId) {
        this.timetableSubjectId = timetableSubjectId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<NotepagesBean> getNotepages() {
        return notepages;
    }

    public void setNotepages(List<NotepagesBean> notepages) {
        this.notepages = notepages;
    }

    public static class NotepagesBean {
        /**
         * createTime : 1572939671582
         * hasContent : 1
         * notebookId : 712425681283190784
         * notepageId : 712425682738614272
         * notepageServerId : 712425682738614272
         * taskId : null
         * updateTime : 1573717988004
         */

        private long createTime;
        private int hasContent;
        private String notebookId;
        private String notepageId;
        private String notepageServerId;
        private Object taskId;
        private long updateTime;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getHasContent() {
            return hasContent;
        }

        public void setHasContent(int hasContent) {
            this.hasContent = hasContent;
        }

        public String getNotebookId() {
            return notebookId;
        }

        public void setNotebookId(String notebookId) {
            this.notebookId = notebookId;
        }

        public String getNotepageId() {
            return notepageId;
        }

        public void setNotepageId(String notepageId) {
            this.notepageId = notepageId;
        }

        public String getNotepageServerId() {
            return notepageServerId;
        }

        public void setNotepageServerId(String notepageServerId) {
            this.notepageServerId = notepageServerId;
        }

        public Object getTaskId() {
            return taskId;
        }

        public void setTaskId(Object taskId) {
            this.taskId = taskId;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
