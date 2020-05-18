package com.fangyuzhong.intelliJ.hadoop.options;


import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.options.CompositeProjectConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettings;
import com.fangyuzhong.intelliJ.hadoop.options.general.GeneralProjectSettings;
import com.fangyuzhong.intelliJ.hadoop.options.ui.ProjectSettingsEditorForm;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 定义工程设置类
 */
public class ProjectSettings
        extends CompositeProjectConfiguration<ProjectSettingsEditorForm>
        implements SearchableConfigurable.Parent
{
    /*
    HDFS连接类配置
     */
    private ConnectionBundleSettings connectionSettings;
    /*
    一般配置类
     */
    private GeneralProjectSettings generalSettings;

    /**
     * 初始化
     * @param project
     */
    public ProjectSettings(Project project)
    {
        super(project);
        connectionSettings = new ConnectionBundleSettings(project);
        generalSettings = new GeneralProjectSettings(project);

        final boolean isDefaultProject = project.isDefault();
        if ((isDefaultProject && ApplicationManager.getApplication().isActive()) || (!isDefaultProject && project.isInitialized()))
        {
            ProjectSettings projectSettings = ProjectSettingsManager.getSettings(project);
            Element settings = new Element("settings");
            projectSettings.writeConfiguration(settings);
            readConfiguration(settings);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getHelpTopic()
    {
        ProjectSettingsEditorForm settingsEditor = getSettingsEditor();
        if (settingsEditor == null)
        {
            return super.getHelpTopic();
        } else
        {
            Configuration selectedConfiguration = settingsEditor.getActiveSettings();
            return selectedConfiguration.getHelpTopic();
        }
    }

    /**
     *
     */
    @Override
    public JComponent createComponent()
    {
        return null;
    }

    /**
     *
     * @return
     */
    public JComponent createCustomComponent()
    {
        return super.createComponent();
    }


    /*********************************************************
     *                         Custom                        *
     *********************************************************/

    /**
     *
     * @return
     */
    public ConnectionBundleSettings getConnectionSettings()
    {
        return connectionSettings;
    }

    /**
     *
     * @return
     */
    public GeneralProjectSettings getGeneralSettings()
    {
        return generalSettings;
    }

    /**
     *
     * @return
     */
    @Override
    @Nls
    public String getDisplayName()
    {
        return "Hadoop Navigator";
    }

    /**
     *
     * @return
     */
    @Override
    @Nullable
    public Icon getIcon()
    {
        return Icons.DATABASE_NAVIGATOR;
    }

    /**
     * 获取配置集合
     * @return
     */
    @Override
    public Configurable[] getConfigurables()
    {
        return getConfigurations();
    }

    /**
     * 通过配置的ID获取配置
     * @param settingsId
     * @return
     */
    @Nullable
    public Configuration getConfiguration(ConfigId settingsId)
    {
        for (Configurable configurable : getConfigurables())
        {
            TopLevelConfig topLevelConfig = (TopLevelConfig) configurable;
            if (topLevelConfig.getConfigId() == settingsId)
            {
                return (Configuration) configurable;
            }
        }
        return null;
    }


    /*********************************************************
     *                    Configuration                      *
     *********************************************************/
    @Override
    public ProjectSettingsEditorForm createConfigurationEditor()
    {
        return new ProjectSettingsEditorForm(this);
    }

    @Override
    protected Configuration[] createConfigurations()
    {
        return new Configuration[]{connectionSettings,generalSettings};
    }

    /*********************************************************
     *              SearchableConfigurable.Parent             *
     *********************************************************/
    @Override
    public boolean hasOwnContent()
    {
        return false;
    }

    //Updated by YUDOUFU123 on 2020/5/18
    @Deprecated
    @Override
    public boolean isVisible()
    {
        return true;
    }

    @Override
    @NotNull
    public String getId()
    {
        return "HadoopNavigator.Project.Settings";
    }

    @Override
    public Runnable enableSearch(String option)
    {
        return null;
    }
}
