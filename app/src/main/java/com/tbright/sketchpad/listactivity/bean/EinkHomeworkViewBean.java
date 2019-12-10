package com.tbright.sketchpad.listactivity.bean;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.List;

public class EinkHomeworkViewBean {

    /**
     * paperList : [{"exampaperImagePath":"","inputRects":[{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209129816064","height":56,"left":619,"pageIndex":1,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":6,"top":359,"width":176},{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209159176192","height":48,"left":796,"pageIndex":1,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":7,"top":359,"width":48}],"pageIndex":1,"studentAnswerImagePath":"","teacherCorrectImagePath":""},{"exampaperImagePath":"","inputRects":[{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209129816064","height":56,"left":619,"pageIndex":2,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":6,"top":359,"width":176},{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209159176192","height":48,"left":796,"pageIndex":2,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":7,"top":359,"width":48}],"pageIndex":2,"studentAnswerImagePath":"","teacherCorrectImagePath":""},{"exampaperImagePath":"","inputRects":[{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209129816064","height":56,"left":619,"pageIndex":3,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":6,"top":359,"width":176},{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209159176192","height":48,"left":796,"pageIndex":3,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":7,"top":359,"width":48}],"pageIndex":3,"studentAnswerImagePath":"","teacherCorrectImagePath":""}]
     * totalPageNum : 6
     * totalScore : 32.5
     */

    private int totalPageNum;
    private String totalScore;
    private List<PaperListBean> paperList;

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public List<PaperListBean> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<PaperListBean> paperList) {
        this.paperList = paperList;
    }

    public static class PaperListBean {
        /**
         * exampaperImagePath :
         * inputRects : [{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209129816064","height":56,"left":619,"pageIndex":1,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":6,"top":359,"width":176},{"examId":"708533208924295168","examInputId":"708533209113038848","exampaperId":"708532379576176640","exampaperRectId":"708533209159176192","height":48,"left":796,"pageIndex":1,"parentRectId":0,"rectContent":"","rectSequence":0,"rectStyle":"","rectStyleData":"","rectType":7,"top":359,"width":48}]
         * pageIndex : 1
         * studentAnswerImagePath :
         * teacherCorrectImagePath :
         */
        private Bitmap teacherCorrectBitmapTemp;
        private String exampaperImagePath;
        private int pageIndex;
        private String studentAnswerImagePath;
        private String teacherCorrectImagePath;
        private List<InputRectsBean> inputRects;
        private double zoom;//缩放比例
        private int studentAnswerDeviceWidth;//学生作答的设备宽度
        private int studentAnswerDeviceHeight;//学生作答的设备高度

        public double getZoom() {
            return zoom;
        }

        public void setZoom(double zoom) {
            this.zoom = zoom;
        }

        public int getStudentAnswerDeviceWidth() {
            return studentAnswerDeviceWidth;
        }

        public void setStudentAnswerDeviceWidth(int studentAnswerDeviceWidth) {
            this.studentAnswerDeviceWidth = studentAnswerDeviceWidth;
        }

        public int getStudentAnswerDeviceHeight() {
            return studentAnswerDeviceHeight;
        }

        public void setStudentAnswerDeviceHeight(int studentAnswerDeviceHeight) {
            this.studentAnswerDeviceHeight = studentAnswerDeviceHeight;
        }

        public Bitmap getTeacherCorrectBitmap() {
            return teacherCorrectBitmapTemp;
        }

        public void setTeacherCorrectBitmap(Bitmap teacherCorrectBitmap) {
            this.teacherCorrectBitmapTemp = teacherCorrectBitmap;
        }

        public String getExampaperImagePath() {
            return exampaperImagePath;
        }

        public void setExampaperImagePath(String exampaperImagePath) {
            this.exampaperImagePath = exampaperImagePath;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public String getStudentAnswerImagePath() {
            return studentAnswerImagePath;
        }

        public void setStudentAnswerImagePath(String studentAnswerImagePath) {
            this.studentAnswerImagePath = studentAnswerImagePath;
        }

        public String getTeacherCorrectImagePath() {
            return teacherCorrectImagePath;
        }

        public void setTeacherCorrectImagePath(String teacherCorrectImagePath) {
            this.teacherCorrectImagePath = teacherCorrectImagePath;
        }

        public List<InputRectsBean> getInputRects() {
            return inputRects;
        }

        public void setInputRects(List<InputRectsBean> inputRects) {
            this.inputRects = inputRects;
        }

        public static class InputRectsBean {
            /**
             * examId : 708533208924295168
             * examInputId : 708533209113038848
             * exampaperId : 708532379576176640
             * exampaperRectId : 708533209129816064
             * height : 56
             * left : 619
             * pageIndex : 1
             * parentRectId : 0
             * rectContent :
             * rectSequence : 0
             * rectStyle :
             * rectStyleData :
             * rectType : 6
             * top : 359
             * width : 176
             */
            private String examId;
            private String examInputId;
            private String exampaperId;
            private String exampaperRectId;
            private int height;
            private int left;
            private int pageIndex;
            private int parentRectId;
            private String rectContent;
            private int rectSequence;
            private String rectStyle;
            private String rectStyleData;
            private int rectType;
            private int top;
            private int width;

            private RectF examInputRectF;//老师批阅框的坐标
            private RectF examInputImageRectF;//老师批阅对错图片的位置
            private int isRight;//有没有批阅的标识
            private String score;//当前得分
            private String fullScore;//满分

            public int getIsRight() {
                return isRight;
            }

            public void setIsRight(int isRight) {
                this.isRight = isRight;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getFullScore() {
                return fullScore;
            }

            public void setFullScore(String fullScore) {
                this.fullScore = fullScore;
            }

            public RectF getExamInputImageRectF() {
                return examInputImageRectF;
            }

            public void setExamInputImageRectF(RectF examInputImageRectF) {
                this.examInputImageRectF = examInputImageRectF;
            }

            public RectF getExamInputRectF() {
                return examInputRectF;
            }

            public void setExamInputRectF(RectF examInputRectF) {
                this.examInputRectF = examInputRectF;
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

            public String getExampaperId() {
                return exampaperId;
            }

            public void setExampaperId(String exampaperId) {
                this.exampaperId = exampaperId;
            }

            public String getExampaperRectId() {
                return exampaperRectId;
            }

            public void setExampaperRectId(String exampaperRectId) {
                this.exampaperRectId = exampaperRectId;
            }

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

            public int getPageIndex() {
                return pageIndex;
            }

            public void setPageIndex(int pageIndex) {
                this.pageIndex = pageIndex;
            }

            public int getParentRectId() {
                return parentRectId;
            }

            public void setParentRectId(int parentRectId) {
                this.parentRectId = parentRectId;
            }

            public String getRectContent() {
                return rectContent;
            }

            public void setRectContent(String rectContent) {
                this.rectContent = rectContent;
            }

            public int getRectSequence() {
                return rectSequence;
            }

            public void setRectSequence(int rectSequence) {
                this.rectSequence = rectSequence;
            }

            public String getRectStyle() {
                return rectStyle;
            }

            public void setRectStyle(String rectStyle) {
                this.rectStyle = rectStyle;
            }

            public String getRectStyleData() {
                return rectStyleData;
            }

            public void setRectStyleData(String rectStyleData) {
                this.rectStyleData = rectStyleData;
            }

            public int getRectType() {
                return rectType;
            }

            public void setRectType(int rectType) {
                this.rectType = rectType;
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
        }
    }
}
