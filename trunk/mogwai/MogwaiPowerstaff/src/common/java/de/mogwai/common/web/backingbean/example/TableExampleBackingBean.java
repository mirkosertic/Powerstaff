/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.web.backingbean.example;

import java.util.Vector;

import de.mogwai.common.command.MessageBoxResultCommand;
import de.mogwai.common.web.backingbean.BackingBean;
import de.mogwai.common.web.backingbean.messagebox.MessageBoxBackingBean;
import de.mogwai.common.web.navigation.ModalPageDescriptor;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.mogwai.common.web.utils.UpdateModelInfo;

public class TableExampleBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6908810875772856940L;

	public static class TableRow {

        private String wert1;

        private String wert2;

        private String wert3;

        public String getWert1() {
            return wert1;
        }

        public void setWert1(String wert1) {
            this.wert1 = wert1;
        }

        public String getWert2() {
            return wert2;
        }

        public void setWert2(String wert2) {
            this.wert2 = wert2;
        }

        public String getWert3() {
            return wert3;
        }

        public void setWert3(String wert3) {
            this.wert3 = wert3;
        }

    }

    private CollectionDataModel<TableRow> data = new CollectionDataModel<TableRow>(new Vector<TableRow>());

    public TableExampleBackingBean() {
        for (int i = 0; i < 10; i++) {
            TableRow theRow = new TableRow();
            theRow.setWert1("Wert1 - " + i);
            theRow.setWert2("Wert2 - " + i);
            theRow.setWert3("Wert1 - " + i);

            data.add(theRow);
        }
    }

    public CollectionDataModel<TableRow> getData() {
        return data;
    }

    public void setData(CollectionDataModel<TableRow> data) {
        this.data = data;
    }

    public String submit() {
        return null;
    }

    public String rowClicked() {

        TableRow theRow = (TableRow) data.getRowData();

        launchMessageDialog(messageBox, MessageBoxBackingBean.class, "Info ", theRow.getWert1());
        return null;
    }

    private ModalPageDescriptor messageBox = new ModalPageDescriptor("/modal/messagebox.jsp", null);

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);

        if (aInfo.getCommand() instanceof MessageBoxResultCommand) {
            shutdownModalDialog();
        }
    }

}
