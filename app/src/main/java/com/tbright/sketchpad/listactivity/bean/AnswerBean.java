package com.tbright.sketchpad.listactivity.bean;

import java.util.List;
import java.util.Map;

public class AnswerBean {

    /**
     * answers : [{"answerContent":"B","examId":"708533209343725568","examInputId":"708533209377280000","rectIds":["708533209427611648"]}]
     * commitStatus : 2
     * costTime : 31
     * exampaperId : 708532379576176640
     * extInfo : {"pageMap":{"9":{"device":{"height":1200,"width":822},"originalPageNum":-1,"paper":{"height":1330,"left":0,"top":18,"width":940,"zoom":1.143552311435523}}}}
     * remedyFlag : false
     * saveDatetime : 1572939700152
     * taskResultId : 712424998190452736
     */

    private int commitStatus;
    private int costTime;
    private String exampaperId;
    private ExtInfoBean extInfo;
    private boolean remedyFlag;
    private long saveDatetime;
    private String taskResultId;
    private List<AnswersBean> answers;

    public int getCommitStatus() {
        return commitStatus;
    }

    public void setCommitStatus(int commitStatus) {
        this.commitStatus = commitStatus;
    }

    public int getCostTime() {
        return costTime;
    }

    public void setCostTime(int costTime) {
        this.costTime = costTime;
    }

    public String getExampaperId() {
        return exampaperId;
    }

    public void setExampaperId(String exampaperId) {
        this.exampaperId = exampaperId;
    }

    public ExtInfoBean getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(ExtInfoBean extInfo) {
        this.extInfo = extInfo;
    }

    public boolean isRemedyFlag() {
        return remedyFlag;
    }

    public void setRemedyFlag(boolean remedyFlag) {
        this.remedyFlag = remedyFlag;
    }

    public long getSaveDatetime() {
        return saveDatetime;
    }

    public void setSaveDatetime(long saveDatetime) {
        this.saveDatetime = saveDatetime;
    }

    public String getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(String taskResultId) {
        this.taskResultId = taskResultId;
    }

    public List<AnswersBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersBean> answers) {
        this.answers = answers;
    }

    public static class ExtInfoBean {
        /**
         * pageMap : {"9":{"device":{"height":1200,"width":822},"originalPageNum":-1,"paper":{"height":1330,"left":0,"top":18,"width":940,"zoom":1.143552311435523}}}
         */

        private Map<String ,PageMapBean> pageMap;

        public Map<String, PageMapBean> getPageMap() {
            return pageMap;
        }

        public void setPageMap(Map<String, PageMapBean> pageMap) {
            this.pageMap = pageMap;
        }

        public static class PageMapBean {
            /**
             * device : {"height":1200,"width":822}
             * originalPageNum : -1
             * paper : {"height":1330,"left":0,"top":18,"width":940,"zoom":1.143552311435523}
             */

            private DeviceBean device;
            private int originalPageNum;
            private PaperBean paper;

            public DeviceBean getDevice() {
                return device;
            }

            public void setDevice(DeviceBean device) {
                this.device = device;
            }

            public int getOriginalPageNum() {
                return originalPageNum;
            }

            public void setOriginalPageNum(int originalPageNum) {
                this.originalPageNum = originalPageNum;
            }

            public PaperBean getPaper() {
                return paper;
            }

            public void setPaper(PaperBean paper) {
                this.paper = paper;
            }

            public static class DeviceBean {
                /**
                 * height : 1200
                 * width : 822
                 */

                private int height;
                private int width;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }
            }

            public static class PaperBean {
                /**
                 * height : 1330
                 * left : 0
                 * top : 18
                 * width : 940
                 * zoom : 1.143552311435523
                 */

                private int height;
                private int left;
                private int top;
                private int width;
                private double zoom;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public double getZoom() {
                    return zoom;
                }

                public void setZoom(double zoom) {
                    this.zoom = zoom;
                }
            }
        }
    }

    public static class AnswersBean {
        /**
         * answerContent : B
         * examId : 708533209343725568
         * examInputId : 708533209377280000
         * rectIds : ["708533209427611648"]
         */

        private String answerContent;
        private String examId;
        private String examInputId;
        private List<String> rectIds;

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getExamInputId() {
            return examInputId;
        }

        public void setExamInputId(String examInputId) {
            this.examInputId = examInputId;
        }

        public List<String> getRectIds() {
            return rectIds;
        }

        public void setRectIds(List<String> rectIds) {
            this.rectIds = rectIds;
        }
    }
}
