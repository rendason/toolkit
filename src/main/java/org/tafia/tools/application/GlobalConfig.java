package org.tafia.tools.application;

import java.util.List;

/**
 * 全局配置
 */
public class GlobalConfig {

    private String dataFile;

    private String originPidKey;

    private String originJarKey;

    private Application application;

    private Commander commander;

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getOriginPidKey() {
        return originPidKey;
    }

    public void setOriginPidKey(String originPidKey) {
        this.originPidKey = originPidKey;
    }

    public String getOriginJarKey() {
        return originJarKey;
    }

    public void setOriginJarKey(String originJarKey) {
        this.originJarKey = originJarKey;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    /**
     * 应用
     */
    public static class Application {
        private String title;
        private Integer width;
        private Integer height;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

    public static class Commander {

        private Side side;

        private Item item;

        private List<Option> options;

        public Side getSide() {
            return side;
        }

        public void setSide(Side side) {
            this.side = side;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

        public static class Side {

            private Integer width;

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }
        }

        public static class Item {

            private Integer width;

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }
        }

        public static class Option {

            private String name;

            private String impl;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImpl() {
                return impl;
            }

            public void setImpl(String impl) {
                this.impl = impl;
            }
        }
    }
}
