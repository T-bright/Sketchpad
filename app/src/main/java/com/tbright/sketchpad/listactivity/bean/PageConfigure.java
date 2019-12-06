package com.tbright.sketchpad.listactivity.bean;

import java.util.List;

public class PageConfigure {

    private List<LayersBean> layers;

    public List<LayersBean> getLayers() {
        return layers;
    }

    public void setLayers(List<LayersBean> layers) {
        this.layers = layers;
    }

    public static class LayersBean {
        /**
         * background : {"name":"","type":1}
         * createTime : 1572939671582
         * layerId : layer_1572939666453
         * layerName : layer_1572939666453
         * updateTime : 1572939671582
         */

        private BackgroundBean background;
        private long createTime;
        private String layerId;
        private String layerName;
        private long updateTime;

        public BackgroundBean getBackground() {
            return background;
        }

        public void setBackground(BackgroundBean background) {
            this.background = background;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getLayerId() {
            return layerId;
        }

        public void setLayerId(String layerId) {
            this.layerId = layerId;
        }

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public static class BackgroundBean {
            /**
             * name :
             * type : 1
             */

            private String name;
            private int type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
