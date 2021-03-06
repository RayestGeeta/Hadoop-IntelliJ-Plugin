package com.fangyuzhong.intelliJ.hadoop.core.options.setting;

import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;

import javax.swing.text.JTextComponent;

/**
 * 定义持久化枚举类型配置设置
 * Created by fangyuzhong on 17-7-21.
 */
public class EnumSetting
        extends Setting<String, JTextComponent>
        implements PersistentConfiguration
{
    public EnumSetting(String name, String value)
    {
        super(name, value);
    }

    @Override
    public void readConfiguration(Element parent)
    {
        setValue(SettingsUtil.getString(parent, getName(), (String) value()));
    }

    @Override
    public void writeConfiguration(Element parent)
    {
        SettingsUtil.setString(parent, getName(), (String) value());
    }

    public boolean to(JTextComponent component)
            throws ConfigurationException
    {
        return setValue(component.getText());
    }

    public void from(JTextComponent component)
    {
        component.setText((String) value());
    }
}