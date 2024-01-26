package server.watchlist.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import server.watchlist.views.ReloadableList;

public class FiSoBar extends HorizontalLayout {
	private Button filterBtn;
	private Button sortBtn;
	private FilterDialog filterDialog;
	private SortingDialog sortingDialog;
	
	public FiSoBar(ReloadableList reloadableList) {
		filterBtn = new Button("Filters", e -> clickFilterBtn());
		sortBtn = new Button("Sorting", e -> clickSortBtn());
		
		filterDialog = new FilterDialog(reloadableList);
		sortingDialog = new SortingDialog(reloadableList);
		
		this.add(filterBtn, sortBtn, filterDialog, sortingDialog);
	}
	
	public void clickFilterBtn() {
		filterDialog.open();
	}
	
	public void clickSortBtn() {
		sortingDialog.open();
	}
}
