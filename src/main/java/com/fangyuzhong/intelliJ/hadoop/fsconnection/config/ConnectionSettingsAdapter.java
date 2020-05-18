package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class ConnectionSettingsAdapter
        implements ConnectionSettingsListener
{
    @Override
    public void connectionsChanged()
    {
    }

    @Override
    public void connectionChanged(String connectionId)
    {
    }

    @Override
    public void connectionNameChanged(String connectionId)
    {
    }
}

