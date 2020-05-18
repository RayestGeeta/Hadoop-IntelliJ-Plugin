package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.ProjectRef;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.list.AbstractFiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableListImpl;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNodeBase;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.FileSystemBrowserTree;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionBundle
        extends FileSystemBrowserTreeNodeBase
{

    public static final Filter<ConnectionHandler> ACTIVE_CONNECTIONS_FILTER = new Filter<ConnectionHandler>()
    {
        public boolean accepts(ConnectionHandler connectionHandler)
        {
            return (connectionHandler != null) && (connectionHandler.isActive());
        }
    };
    private ProjectRef projectRef;
    private AbstractFiltrableList<ConnectionHandler> connectionHandlers = new FiltrableListImpl(ACTIVE_CONNECTIONS_FILTER);
    private List<ConnectionHandler> virtualConnections = new ArrayList();

    /**
     * 初始化
     * @param project
     */
    public ConnectionBundle(Project project)
    {
        this.projectRef = new ProjectRef(project);
        this.virtualConnections.add(new VirtualConnectionHandler("virtual-hdfs-fsconnection",
                                                                 "Virtual - hdfs 3.0",
                                                                  FileSystemType.HDFS,
                                                                  3.0, project));
    }

    /**
     * 添加连接到集合
     * @param connectionHandler
     */
    public void addConnection(ConnectionHandler connectionHandler)
    {
        this.connectionHandlers.add(connectionHandler);
        Disposer.register(this, connectionHandler);
    }
    public List<ConnectionHandler> getVirtualConnections()
    {
        return this.virtualConnections;
    }

    /**
     *
     * @param connectionHandlers
     */
    public void setConnectionHandlers(List<ConnectionHandler> connectionHandlers)
    {
        this.connectionHandlers = new FiltrableListImpl<ConnectionHandler>(connectionHandlers, ACTIVE_CONNECTIONS_FILTER);
    }

    public List<ConnectionHandler> getAllConnectionHandlers()
    {
        return this.connectionHandlers.getFullList();
    }

    /**
     *
     * @param id
     * @return
     */
    public ConnectionHandler getConnection(String id)
    {
        for (ConnectionHandler connectionHandler : connectionHandlers.getFullList())
        {
            if (connectionHandler.getId().equals(id)) return connectionHandler;
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public ConnectionHandler getVirtualConnection(String id)
    {
        for (ConnectionHandler virtualConnection : this.virtualConnections)
        {
            if (virtualConnection.getId().equals(id))
            {
                return virtualConnection;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    @Override
    public Icon getIcon(int flags)
    {
        return Icons.PROJECT;
    }

    @Override
    @NotNull
    public Project getProject()
    {
       return FailsafeUtil.get(this.projectRef.get());
    }


    @Override
    public boolean isDisposed()
    {
        return false;
    }

    @Override
    public void dispose()
    {
        super.dispose();
        this.connectionHandlers.clear();
        this.virtualConnections.clear();
    }

    @Override
    public void navigate(boolean requestFocus)
    {
    }

    @Override
    public boolean canNavigate()
    {
        return true;
    }

    @Override
    public boolean canNavigateToSource()
    {
        return false;
    }

    @Override
    public String getName()
    {
        return getPresentableText();
    }

    @Override
    public String getLocationString()
    {
        return null;
    }

    @Override
    public ItemPresentation getPresentation()
    {
        return this;
    }

    @Override
    public Icon getIcon(boolean open)
    {
        return getIcon(0);
    }

    @Override
    public boolean isTreeStructureLoaded()
    {
        return true;
    }

    @Override
    public void initTreeElement()
    {
    }

    @Override
    public boolean canExpand()
    {
        return true;
    }

    @Override
    @Nullable
    public FileSystemBrowserTreeNode getParent()
    {
        FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(getProject());
        FileSystemBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
        return activeBrowserTree == null ? null : browserManager.isTabbedMode() ? null : activeBrowserTree.getModel().getRoot();
    }

    @Override
    public List<? extends FileSystemBrowserTreeNode> getChildren()
    {
        return null;
    }

    @Override
    public void refreshTreeChildren()
    {
        for (ConnectionHandler connectionHandler : this.connectionHandlers)
        {
            connectionHandler.getObjectBundle().refreshTreeChildren();
        }
    }

    @Override
    public void rebuildTreeChildren()
    {
        for (ConnectionHandler connectionHandler : this.connectionHandlers)
        {
            connectionHandler.getObjectBundle().rebuildTreeChildren();
        }
    }

    @Override
    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return this.connectionHandlers.get(index).getObjectBundle();
    }

    @Override
    public int getChildCount()
    {
        return this.connectionHandlers.size();
    }

    @Override
    public boolean isLeaf()
    {
        return this.connectionHandlers.size() == 0;
    }

    @Override
    public int getIndex(FileSystemBrowserTreeNode child)
    {
        FileSystemObjectBundle objectBundle = (FileSystemObjectBundle) child;
        return this.connectionHandlers.indexOf(objectBundle.getConnectionHandler());
    }

    @Override
    public int getTreeDepth()
    {
        return 1;
    }

    @Override
    public String getPresentableText()
    {
        return "Hadoop connections";
    }

    @Override
    public String getPresentableTextDetails()
    {
        int size = this.connectionHandlers.size();
        return "(" + size + ')';
    }

    @Override
    public String getPresentableTextConditionalDetails()
    {
        return null;
    }

    @Override
    @Nullable
    public ConnectionHandler getConnectionHandler()
    {
        return null;
    }

    @Override
    public String getToolTip()
    {
        return "";
    }

    public boolean isEmpty()
    {
        return this.connectionHandlers.getFullList().isEmpty();
    }

    @Override
    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }
    @Override
    public GenericFileSystemElement getUndisposedElement()
    {
        return this;
    }

    public FiltrableList<ConnectionHandler> getConnectionHandlers()
    {
        return connectionHandlers;
    }
}
