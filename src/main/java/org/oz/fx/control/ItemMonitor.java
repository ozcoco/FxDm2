package org.oz.fx.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;

public class ItemMonitor extends FlowPane {

    private String[] urls;

    private ObservableList<MonitorView> monitors = FXCollections.observableArrayList();

    public ItemMonitor() {
        this(null);
    }

    public ItemMonitor(String... urls) {
        super();

        this.urls = urls;

        initialize();

    }


    public void initialize() {

        initData();

        initView();

    }

    private void initView() {

        playUrls(this.urls);


    }

    private void initData() {

    }


    private void playUrls(String... urls) {

        assert urls != null;

        int len = urls.length;

        for (String url : urls) {

            final MonitorView monitorView = new MonitorView(4, url);

            getChildren().add(monitorView);

            monitors.add(monitorView);

        }

    }


    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String... urls) {
        this.urls = urls;

        playUrls(urls);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        assert monitors != null;

        monitors.clear();


    }
}
