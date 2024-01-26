package server.watchlist.layouts;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import server.watchlist.views.ReloadableList;

public class SortingDialog extends Dialog {
	private RadioButtonGroup<String> radios;
	private SortingModes[] sortingModes = SortingModes.values();
	private String[] sortingModeStrings;
	private ReloadableList reloadableList;
	
	public SortingDialog(ReloadableList reloadableList) {
		super();
		this.reloadableList = reloadableList;
		this.setHeaderTitle("Sorting");
		this.addDialogCloseActionListener(e -> closeDialog());
		sortingModeStrings = new String[sortingModes.length];
		for(int i = 0; i < sortingModes.length; i++) {
			sortingModeStrings[i] = sortingModes[i].toString();
		}
		radios = new RadioButtonGroup<String>();
		radios.setItems(sortingModeStrings);
		radios.setValue(reloadableList.getSelected().toString());
		
		VerticalLayout vl = new VerticalLayout();
		vl.add(radios);
		this.add(vl);
	}
	
	private void closeDialog() {
		this.close();
		reloadableList.reloadList(getSortingMode());
	}
	
	private SortingModes getSortingMode() {
		return SortingModes.valueOf(radios.getValue());
	}
}
