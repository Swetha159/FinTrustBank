package com.bank.fintrustbank.util;

import javax.sql.DataSource;

public class DataSourceHolder {

    private static class Holder {
        private static final DataSourceHolder INSTANCE = new DataSourceHolder();
    }

    public static DataSourceHolder getInstance() {
        return Holder.INSTANCE;
    }

    private DataSource dataSource;

    private DataSourceHolder() {}

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized.");
        }
        return dataSource;
    }
}
