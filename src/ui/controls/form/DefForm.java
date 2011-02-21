/*
 * DefForm.java
 *
 * Created on 21.05.2008, 9:40
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * You can also redistribute and/or modify this program under the
 * terms of the Psi License, specified in the accompanied COPYING
 * file, as published by the Psi Project; either dated January 1st,
 * 2005, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ui.controls.form;

//import Client.Config;
import java.util.Vector;
import Menu.MenuListener;
import Menu.MenuCommand;
import Menu.MyMenu;
import images.RosterIcons;
import locale.SR;
import ui.MainBar;
import ui.VirtualElement;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class DefForm
        extends VirtualList
        implements MenuListener {

    public final Vector itemsList = new Vector();
    public static MenuCommand cmdOk = new MenuCommand(SR.MS_OK, MenuCommand.OK, 1);
    public static MenuCommand cmdCancel = new MenuCommand(SR.MS_BACK, MenuCommand.BACK, 99);

    /**
     * Creates a new instance of DefForm
     * @param caption
     */
    public DefForm(String caption) {
        this(caption, true);
    }

    public DefForm(String caption, boolean show) {
        setMainBarItem(new MainBar(caption));

        enableListWrapping(false);
        if (show) {
            show();
        }
    }

    protected int getItemCount() {
        synchronized (itemsList) {
            return itemsList.size();
        }
    }

    public VirtualElement getItemRef(int index) {
        if (index >= getItemCount())
            return null;
        synchronized (itemsList) {
            return (VirtualElement) itemsList.elementAt(index);
        }
    }

    public void menuAction(MenuCommand command, VirtualList displayable) {
        if (command == cmdCancel) {
            cmdCancel();
        }
        if (command == cmdOk) {
            cmdOk();
        }
    }

    public final void loadItemsFrom(Vector items) {
        synchronized (itemsList) {
            if (items == null) {
                return;
            }
            int count = items.size();
            itemsList.removeAllElements();
            for (int i = 0; i < count; i++) {
                itemsList.addElement(items.elementAt(i));
            }
            redraw();
        }
    }

    public void cmdOk() {
        destroyView();
    }

    public final Vector menuCommands = new Vector();
    public String menuName = "";

    public void addMenuCommand(MenuCommand command) {
        if (menuCommands.indexOf(command) < 0)
            menuCommands.addElement(command);
    }

    public void removeMenuCommand(MenuCommand command) {
        menuCommands.removeElement(command);
    }

    public void commandState() {
        menuCommands.removeAllElements();
        addMenuCommand(cmdOk);
        if (!cf.swapMenu)
            addMenuCommand(cmdCancel);
    }

    public void showMenu() {
        new MyMenu(this, menuName, RosterIcons.getInstance(), menuCommands);
    }

    MenuCommand findCommand(int type) {        
        for (int i = 0; i < menuCommands.size(); i++) {
            if (((MenuCommand)menuCommands.elementAt(i)).map == type)
                return (MenuCommand)menuCommands.elementAt(i);
        }
        return null;
    }

    public void captionPressed() {}
    
    public String selectCommand() {
        MenuCommand commandOk = findCommand(MenuCommand.OK);
        return commandOk == null ? SR.MS_OK : commandOk.name;
    }

    public String menuCommand() {
            if (menuCommands.size() > 2) {
                return SR.MS_MENU;
            }
            if (menuCommands.size() == 2) {
                return ((MenuCommand)menuCommands.elementAt(1)).name;
            }
            return "";
    }

    public void menuPressed() {
        if (menuCommands.size() > 2) {
            showMenu();
            return;
        }
        if (menuCommands.size() == 2) {
            menuAction((MenuCommand) menuCommands.elementAt(1), this);
        } else {
            cmdOk();
        }
    }
    public void selectPressed() {
        MenuCommand commandOk = findCommand(MenuCommand.OK);
        menuAction(commandOk == null ? cmdOk : commandOk, this);
    }    
}
