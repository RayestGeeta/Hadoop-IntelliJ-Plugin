package com.fangyuzhong.intelliJ.hadoop.core.option;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.options.setting.SettingsUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * Created by fangyuzhong on 17-7-21.
 * Update by YUDOUFU123 on 2020/5/18
 */

public class ConfirmationOptionHandler
        implements DialogWrapper.DoNotAskOption, PersistentConfiguration
{
    private String configName;
    private String title;
    private String message;
    private boolean confirm;

    public ConfirmationOptionHandler(String configName, String title,
                                     String message, boolean defaultKeepAsking)
    {
        this.configName = configName;
        this.title = title;
        this.message = message;
        this.confirm = defaultKeepAsking;
    }

    @Override
    public boolean isToBeShown()
    {
        return true;
    }

    @Override
    public void setToBeShown(boolean keepAsking, int selectedIndex)
    {
        this.confirm = keepAsking;
    }

    public void setConfirm(boolean confirm)
    {
        this.confirm = confirm;
    }

    public boolean isConfirm()
    {
        return this.confirm;
    }

    @Override
    public boolean canBeHidden()
    {
        return true;
    }

    @Override
    public boolean shouldSaveOptionsOnCancel()
    {
        return false;
    }

    @Override
    @NotNull
    public String getDoNotShowMessage()
    {
        return  "Do not ask again";
    }

    public boolean resolve(Object... messageArgs)
    {
        if (this.confirm)
        {
            int optionIndex = Messages.showDialog(MessageFormat.format(this.message, messageArgs),
                    "Hadoop Navigator - " + this.title, new String[]{"Yes", "No"}, 0,
                    Icons.DIALOG_QUESTION, this);

            return optionIndex == 0;
        }
        return true;
    }

    @Override
    public void readConfiguration(Element element)
    {
        this.confirm = SettingsUtil.getBoolean(element, this.configName, this.confirm);
    }

    @Override
    public void writeConfiguration(Element element)
    {
        SettingsUtil.setBoolean(element, this.configName, this.confirm);
    }
}
