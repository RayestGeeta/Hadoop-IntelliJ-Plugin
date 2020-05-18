package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.intellij.openapi.components.NamedComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.impl.ProjectLifecycleListener;
//add by YUDOUFU123 on 2020/5/16
import com.intellij.openapi.project.ProjectManagerListener;

import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 定义HDFS连接的缓存类，Application级别插件，IDEA系统启动进行初始化
 * Created by fangyuzhong on 17-7-15.
 * Updated by YUDOUFU123 on 2020/5/16
 */
public class ConnectionCache implements NamedComponent
{
    /*
     使用HashMap 来存储Connection的缓存
     */
    private static Map<String, ConnectionHandler> CACHE = new THashMap();

    /**
     * 根据connection的ID查找ConnectionHandler
     * @param connectionId
     * @return
     */
    @Nullable
    public static ConnectionHandler findConnectionHandler(String connectionId)
    {
        ConnectionHandler connectionHandler =  CACHE.get(connectionId);
        ProjectManager projectManager = ProjectManager.getInstance();
        if ((connectionHandler == null) && (projectManager != null))
        {
            synchronized (ConnectionCache.class)
            {
                connectionHandler = CACHE.get(connectionId);
                if (connectionHandler == null)
                {
                    for (Project project : projectManager.getOpenProjects())
                    {
                        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
                        connectionHandler = connectionManager.getConnectionHandler(connectionId);
                        if ((connectionHandler != null) && (!connectionHandler.isDisposed()))
                        {
                            CACHE.put(connectionId, connectionHandler);
                            return connectionHandler;
                        }
                    }
                }
            }
        }
        return (connectionHandler == null) || (connectionHandler.isDisposed()) ? null : connectionHandler;
    }
    /**
     * 初始化插件组件
     */
    public void initComponent()
    {
        //注册项目生命周期通知
        EventUtil.subscribe(null, ProjectLifecycleListener.TOPIC, this.projectLifecycleListener);
    }
    public void disposeComponent()
    {
    }

    @Override
    @NotNull
    public String getComponentName()
    {
        return  "HadoopNavigator.ConnectionCache";

    }


    //定义新的生命周期处理事件接口
    interface NewProjectLifecycleListener extends ProjectManagerListener, ProjectLifecycleListener {

    }

    /**
     * 定义工程项目的生命周期处理事件类
     */
    private NewProjectLifecycleListener projectLifecycleListener = new NewProjectLifecycleListener()
    {
        /**
         * 工程Project组件初始化后处理
         * @param project
         * @deprecated
         * Updated by YUDOUFU123 on 2020/5/16
         */
        /*@Deprecated
        public void projectComponentsInitialized(@NotNull Project project)
        {
            ConnectionManager connectionManager = ConnectionManager.getInstance(project);
            if (connectionManager == null) {return;}
            List<ConnectionHandler> connectionHandlers = connectionManager.getConnectionHandlers();
            for (ConnectionHandler connectionHandler : connectionHandlers)
            {
                ConnectionCache.CACHE.put(connectionHandler.getId(), connectionHandler);
            }
        }*/

        @Override
        public void beforeProjectLoaded(@NotNull Project project) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(project);
            if (connectionManager == null) {return;}
            List<ConnectionHandler> connectionHandlers = connectionManager.getConnectionHandlers();
            for (ConnectionHandler connectionHandler : connectionHandlers)
            {
                ConnectionCache.CACHE.put(connectionHandler.getId(), connectionHandler);
            }
        }

        /**
         * 关闭工程Project后处理
         * @param project
         * @deprecated
         * Updated by YUDOUFU123 on 2020/5/16
         */
        /*@Deprecated
        public void afterProjectClosed(@NotNull Project project)
        {
            Iterator<String> connectionIds = ConnectionCache.CACHE.keySet().iterator();
            while (connectionIds.hasNext())
            {
                String connectionId = (String) connectionIds.next();
                ConnectionHandler connectionHandler = (ConnectionHandler) ConnectionCache.CACHE.get(connectionId);
                if ((connectionHandler.isDisposed()) || (connectionHandler.getProject() == project))
                {
                    connectionIds.remove();
                }
            }
        }*/

        /**
         * added by YUDOUFU123 on 2020/5/16
         * @param project
         */
        @Override
        public void projectOpened(@NotNull Project project) {
        }

        @Override
        public void projectClosing(@NotNull Project project) {
        }

        @Override
        public void projectClosed(@NotNull Project project) {
            Iterator<String> connectionIds = ConnectionCache.CACHE.keySet().iterator();
            while (connectionIds.hasNext())
            {
                String connectionId = (String) connectionIds.next();
                ConnectionHandler connectionHandler = (ConnectionHandler) ConnectionCache.CACHE.get(connectionId);
                if ((connectionHandler.isDisposed()) || (connectionHandler.getProject() == project))
                {
                    connectionIds.remove();
                }
            }
        }


        @Override
        public void projectClosingBeforeSave(@NotNull Project project) {
        }

    };
}
